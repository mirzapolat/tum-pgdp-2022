package pgdp.sim;

import pgdp.sim.Cell;
import pgdp.sim.CellSymbol;
import pgdp.sim.MovingCell;

public class Hamster extends MovingCell {

    public Hamster() {

    }

    @Override
    public CellSymbol getSymbol() {
        return null;
    }

    @Override
    public boolean canEat(Cell other) {
        return false;
    }

    @Override
    public int foodConsumption() {
        return 0;
    }

    @Override
    public int consumedFood() {
        return 0;
    }

    @Override
    public int reproductionCost() {
        return 0;
    }

    @Override
    public int initialFood() {
        return 0;
    }

    @Override
    public Cell getNew() {
        return null;
    }
}
