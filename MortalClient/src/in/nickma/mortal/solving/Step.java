package in.nickma.mortal.solving;

import in.nickma.mortal.enums.Direction;

import java.util.List;

public class Step {

    private final boolean[][] grid;
    private final Integer spacesLeft;
    private final String directionHistory;
    private final Integer x;
    private final Integer y;

    public Step(
            final boolean[][] grid,
            final Integer spacesLeft,
            final String directionHistory,
            final Integer x,
            final Integer y) {
        this.grid = grid;
        this.spacesLeft = spacesLeft;
        this.directionHistory = directionHistory;
        this.x = x;
        this.y = y;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public Integer getSpacesLeft() {
        return spacesLeft;
    }

    public String getDirectionHistory() {
        return directionHistory;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
