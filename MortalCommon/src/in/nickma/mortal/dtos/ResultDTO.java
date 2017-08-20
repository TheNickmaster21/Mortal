package in.nickma.mortal.dtos;

import in.nickma.mortal.enums.Direction;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultDTO implements Serializable {

    private final ArrayList<Direction> directions;
    private final Integer startX;
    private final Integer startY;
    private final Integer level;

    public ResultDTO(
            final ArrayList<Direction> directions,
            final Integer startX,
            final Integer startY,
            final Integer level) {
        this.directions = directions;
        this.startX = startX;
        this.startY = startY;
        this.level = level;
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public Integer getStartX() {
        return startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public Integer getLevel() {
        return level;
    }
}
