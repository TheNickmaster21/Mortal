package in.nickma.mortal.enums;

import java.util.EnumSet;

public enum Direction {

    UP('U', 0, -1),
    DOWN('D', 0, 1),
    LEFT('L', -1, 0),
    RIGHT('R', 1, 0);

    private char code;
    private int x;
    private int y;

    Direction(final char code, final int x, final int y) {
        this.code = code;
        this.x = x;
        this.y = y;
    }

    public static EnumSet<Direction> getDirections() {
        return EnumSet.allOf(Direction.class);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction[] getSides() {
        switch (this) {
            case UP:
                return new Direction[]{RIGHT, LEFT};
            case DOWN:
                return new Direction[]{RIGHT, LEFT};
            case LEFT:
                return new Direction[]{UP, DOWN};
            case RIGHT:
                return new Direction[]{UP, DOWN};
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }

}
