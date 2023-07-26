package pgdp.sim;

public abstract class MovingCell implements Cell {

    private long food;

    public MovingCell() {
        this.food = this.initialFood();
    }

    public abstract boolean canEat(Cell other);

    public abstract int foodConsumption();

    public abstract int consumedFood();

    public abstract int reproductionCost();

    public abstract int initialFood();

    public abstract Cell getNew();

    public void move(Cell[] cells, Cell[] newCells, int width, int height, int x, int y) {
        int place = RandomGenerator.nextInt(0, 9);

        int newIndex = calcIndex(y*width+x, place, width);
        if (x == 0          && (place == 0 || place == 3 || place == 6)) newIndex = y*width+x;
        if (x == width-1    && (place == 2 || place == 5 || place == 8)) newIndex = y*width+x;
        if (y == 0          && (place == 0 || place == 1 || place == 2)) newIndex = y*width+x;
        if (x == height-1   && (place == 6 || place == 7 || place == 8)) newIndex = y*width+x;

        newCells[newIndex] = this;
    }

    private int calcIndex(int index, int place, int width) {
        return switch (place) {
            case 3 -> index-1;
            case 5 -> index+1;
            case 4 -> index;
            case 1 -> index-width;
            case 7 -> index+width;
            case 0 -> index-width-1;
            case 2 -> index-width+1;
            case 6 -> index+width-1;
            case 8 -> index+width+1;
            default -> -1;
        };
    }

    public void eat(Cell[] cells, Cell[] newCells, int width, int height, int x, int y) {

    }

    public void tick(Cell[] cells, Cell[] newCells, int width, int height, int x, int y) {

    }

    public int priority() {
        return 0;
    }
}
