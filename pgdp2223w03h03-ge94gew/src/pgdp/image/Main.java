package pgdp.image;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {

    public static void printHelp(){
        System.out.println(
                """
                Usage: <program> [options] input_file output_file new_width
                
                    -h, --help: show this help message
                    -m <file>, --mask <file>: set mask to this file
                """);
    }

    public static void main(String[] args) throws IOException {
        String inputFile=null,outputFile=null,maskFile=null,widthString=null;
        int index=0;
        var maskPattern=Pattern.compile("--mask=(.*)");
        var helpPattern=Pattern.compile("--help|-[a-zA-Z]*h[a-zA-Z]*\\s*\\z");
        var maskPatternSplit=Pattern.compile("(-m|--mask)\\s*\\z");
        var optionPattern=Pattern.compile("-.*");
        for(int i=0;i< args.length;i++){
            if (helpPattern.matcher(args[i]).matches()){
                printHelp();
                System.exit(0);
            }
            else {
                var maskMatcher=maskPattern.matcher(args[i]);
                if(maskMatcher.matches())maskFile= maskMatcher.group(1);
                else if (maskPatternSplit.matcher(args[i]).matches()&&args.length>i+1) {
                    maskFile=args[++i];
                }else if (optionPattern.matcher(args[i]).matches()){
                    printHelp();
                    System.exit(1);
                }else{
                    switch (index){
                        case 0->{inputFile=args[i];index++;}
                        case 1->{outputFile=args[i];index++;}
                        case 2->{widthString=args[i];index++;}
                        default -> {
                            printHelp();
                            System.exit(1);
                        }
                    }
                }
            }
            
        }

        if(inputFile==null||outputFile==null||widthString==null){
            printHelp();
            System.exit(1);
        }
        var inputDecoders= ImageIO.getImageReadersBySuffix(inputFile.substring(inputFile.lastIndexOf('.')+1));
        if (!inputDecoders.hasNext()){
            System.out.println("Unable to decode "+inputFile);
            System.exit(1);
        }
        var outputEncoders=ImageIO.getImageWritersBySuffix(outputFile.substring(outputFile.lastIndexOf('.')+1));
        if (!outputEncoders.hasNext()){
            System.out.println("Unable to encode "+outputFile);
            System.exit(1);
        }

        var inputDecoder=inputDecoders.next();
        inputDecoder.setInput(new FileImageInputStream(new File(inputFile)));
        var inputImage=inputDecoder.read(0);
        var input=inputImage.getRGB(0,0,inputImage.getWidth(),inputImage.getHeight(),null,0,inputImage.getWidth());

        var outputEncoder=outputEncoders.next();
        var outputF=new File(outputFile);
        if(!outputF.isFile())Files.createFile(outputF.toPath());
        outputEncoder.setOutput(new FileImageOutputStream(outputF));

        int[] mask=null;
        if(maskFile==null){
            mask=new int[input.length];
            Arrays.fill(mask,-1);
        }else{
            var maskDecoders=ImageIO.getImageReadersBySuffix(maskFile.substring(maskFile.lastIndexOf('.')+1));
            if(!maskDecoders.hasNext()){
                System.out.println("Unable to decode "+maskFile);
                System.exit(1);
            }
            var maskDecoder=maskDecoders.next();
            maskDecoder.setInput(new FileImageInputStream(new File(maskFile)));
            var maskImage=maskDecoder.read(0);
            if(maskImage.getWidth()!=inputImage.getWidth()||maskImage.getHeight()!=inputImage.getHeight()){
                System.out.println("Mismatched image size between mask and input image");
                System.exit(1);
            }
            mask=maskImage.getRGB(0,0,maskImage.getWidth(),maskImage.getHeight(),null,0,maskImage.getWidth());
        }
        int newWidth=Integer.parseInt(widthString);
        var output=new SeamCarving().shrink(input,mask,inputImage.getWidth(),inputImage.getHeight(),newWidth);
        var outputImage=new BufferedImage(newWidth,inputImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        outputImage.setRGB(0,0,newWidth,inputImage.getHeight(),output,0,newWidth);
        outputEncoder.write(outputImage);

    }
}
