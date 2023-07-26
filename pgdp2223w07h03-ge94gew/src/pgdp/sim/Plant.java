package pgdp.sim;

public class Plant implements Cell {
    private long growth;

    public Plant() {
        growth = 0;
    }

    @Override
    public CellSymbol getSymbol() {
        return CellSymbol.PLANT;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void tick(Cell[] cells, Cell[] newCells, int width, int height, int x, int y) {
        newCells[(y * width) + x] = this;
        growth += RandomGenerator.nextInt(SimConfig.plantMinGrowth, SimConfig.plantMaxGrowth);

        while (growth >= SimConfig.plantReproductionCost && new Plant().place(cells, newCells, width, height, x, y))
            growth -= SimConfig.plantReproductionCost;
    }

    public long getGrowth() {
        return growth;
    }

    public void setGrowth(long growth) {
        this.growth = growth;
    }
}
