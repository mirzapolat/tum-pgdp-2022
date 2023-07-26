package pgdp.pingunetwork;

public class Picture {
    private String location;
    private int width;
    private int height;
    private int[][] data;
    private Picture[] thumbnails = new Picture[0];

    public Picture(String location, int[][] data) {
        this.location = location;

        if (data == null) data = new int[0][0];

        this.data = data;
        this.height = data.length;
        if (data.length != 0)
            this.width = data[0].length;
        else this.width = 0;
    }

    public void setThumbnails(Picture[] thumbnails) {
        this.thumbnails = thumbnails;
    }

    public Picture[] getThumbnails() {
        return thumbnails;
    }
    public String getLocation() {return location;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int[][] getData() {return data;}
}
