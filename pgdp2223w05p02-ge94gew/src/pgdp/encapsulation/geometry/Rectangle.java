package pgdp.encapsulation.geometry;

public class Rectangle {
    public Point bottomLeftCorner;
    public Point topRightCorner;

    public Rectangle(Point bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.topRightCorner = new Point(bottomLeftCorner.x + width, bottomLeftCorner.y + height);
    }

    public int getCircumference() {
        return 2 * (this.topRightCorner.x - this.bottomLeftCorner.x) + 2 * (this.topRightCorner.y - this.bottomLeftCorner.y);
    }

    public int getArea() {
        return (this.topRightCorner.x - this.bottomLeftCorner.x) * (this.topRightCorner.y - this.bottomLeftCorner.y);
    }

    public int getHeight() {
        return (this.topRightCorner.y - this.bottomLeftCorner.y);
    }

    public int getWidth() {
        return (this.topRightCorner.x - this.bottomLeftCorner.x);
    }
}
