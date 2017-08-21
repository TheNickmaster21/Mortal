package in.nickma.mortal.solving;

import in.nickma.mortal.enums.Direction;

import java.util.List;

public class Step {

    private final Boolean[][] grid;
    private final List<Direction> directionHistory;
    private final Integer x;
    private final Integer y;

    public Step(final Boolean[][] grid, List<Direction> directionHistory, final Integer x, final Integer y) {
        this.grid = grid;
        this.directionHistory = directionHistory;
        this.x = x;
        this.y = y;
    }

    public Boolean[][] getGrid() {
        return grid;
    }

    public List<Direction> getDirectionHistory() {
        return directionHistory;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
