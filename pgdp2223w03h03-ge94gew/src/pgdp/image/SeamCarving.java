package pgdp.image;

import java.util.Arrays;

public class SeamCarving {

	public int computeGradientMagnitude(int v1, int v2) {

		int magG = (((v1 & 0xFF) - (v2 & 0xFF)) * ((v1 & 0xFF) - (v2 & 0xFF)));
		int magB = (((v1 >> 8) & 0xFF) - ((v2 >> 8) & 0xFF)) * (((v1 >> 8) & 0xFF) - ((v2 >> 8) & 0xFF));
		int magR = (((v1 >> 16) & 0xFF) - ((v2 >> 16) & 0xFF)) * (((v1 >> 16) & 0xFF) - ((v2 >> 16) & 0xFF));

		return magG + magR + magB;
	}

	public void toGradientMagnitude(int[] picture, int[] gradientMagnitude, int width, int height) {
		for (int i = 0; i < width; i++) gradientMagnitude[i] = Integer.MAX_VALUE;
		for (int i = gradientMagnitude.length -1 -width; i < gradientMagnitude.length; i++) gradientMagnitude[i] = Integer.MAX_VALUE;

		for (int i = width; i < gradientMagnitude.length -1 -width; i++) {
			if (i % width == 0 || i % width == width - 1) {
				gradientMagnitude[i] = Integer.MAX_VALUE;
			}
			else {
				gradientMagnitude[i] = computeGradientMagnitude(picture[i-1], picture[i+1]) + computeGradientMagnitude(picture[i-width], picture[i+width]);
			}
		}
	}

	public void combineMagnitudeWithMask(int[] gradientMagnitude, int[] mask, int width, int height){
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				int index = h * width + w;
				if ((mask[index] & 0xFF) == 0x00 && ((mask[index] >> 8) & 0xFF) == 0x00 && ((mask[index] >> 16) & 0xFF) == 0x00) {
					gradientMagnitude[index] = Integer.MAX_VALUE;
				}
			}
		}
	}

	public void buildSeams(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height) {
		for (int x = 0; x < width; x++) {
			seamWeights[x] = gradientMagnitude[x];
			seams[x][0] = x;

			int current = x;
			for (int y = 0; y < height -1; y++) {
				int mitte = gradientMagnitude[y *width +x +width +current-x];

				int rechts;
				try {
					rechts = gradientMagnitude[y *width +x +width +1 +current-x];
				}
				catch (ArrayIndexOutOfBoundsException e) {
					rechts = Integer.MAX_VALUE;
				}

				int links = gradientMagnitude[y *width +x +width -1 +current-x];

				if (mitte <= links && mitte <= rechts)
					seamWeights[x] += mitte;
				else if (links <= rechts) {
					seamWeights[x] += links;
					current--;
				}
				else {
					seamWeights[x] += rechts;
					current++;
				}

				seams[x][y+1] = current;
			}
		}
	}

	public void removeSeam(int[] seam, int[] image, int height, int oldWidth) {
		for (int i = 0; i < height; i++) {
			for (int s = i * (oldWidth-1) + seam[i] + 1; s < image.length; s++) {
				image[s-1] = image[s];
			}
		}
	}

	public int[] shrink(int[] image,int[] mask, int width, int height, int newWidth) {

		if (newWidth >= width) return image;

		for (int i = 0; i < (width-newWidth); i++) {
			int[] gradientMagnitude = new int[(width-i) * height];
			toGradientMagnitude(image, gradientMagnitude, (width-i), height);
			combineMagnitudeWithMask(gradientMagnitude, mask, (width-i), height);

			int[][] seams = new int[width-i][height];
			long[] weight = new long[width-i];

			buildSeams(seams, weight, gradientMagnitude, width-i, height);
			removeSeam(seams[seamToRemove(weight)], image, height, width-1);
			removeSeam(seams[seamToRemove(weight)], mask, height, width-1);
		}

		int[] newImage = Arrays.copyOf(image, newWidth * height);

		return newImage;
	}

	public int seamToRemove(long[] array) {
		int index = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[index]) index = i;
		}

		return index;
	}

}

