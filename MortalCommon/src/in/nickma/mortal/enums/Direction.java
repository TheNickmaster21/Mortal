package in.nickma.mortal.enums;

public enum Direction {

    UP('U'),
    DOWN('D'),
    LEFT('L'),
    RIGHT('R');

    private char code;

    Direction(final char code) {
        this.code = code;
    }

    public static Direction getDirection(final Integer x, final Integer y) {
        if (Integer.signum(x) == 1) {
            return LEFT;
        }
        if (Integer.signum(x) == -1) {
            return RIGHT;
        }
        if (Integer.signum(y) == 1) {
            return UP;
        }
        if (Integer.signum(y) == -1) {
            return DOWN;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
