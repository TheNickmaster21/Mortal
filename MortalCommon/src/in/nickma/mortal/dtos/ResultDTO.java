package in.nickma.mortal.dtos;

import in.nickma.mortal.enums.Direction;

import java.io.Serializable;
import java.util.List;

public class ResultDTO implements Serializable {

    private final Boolean success;

    private final List<Direction> directions;
    private final Integer startX;
    private final Integer startY;
    private final Integer level;

    private ResultDTO(
            final List<Direction> directions,
            final Integer startX,
            final Integer startY,
            final Integer level) {
        this.success = true;
        this.directions = directions;
        this.startX = startX;
        this.startY = startY;
        this.level = level;
    }

    private ResultDTO() {
        this.success = false;
        this.directions = null;
        this.startX = null;
        this.startY = null;
        this.level = null;
    }

    public static ResultDTO getSuccessfullResultDTO(
            final List<Direction> directions,
            final Integer startX,
            final Integer startY,
            final Integer level) {
        return new ResultDTO(directions, startX, startY, level);
    }

    public static ResultDTO getUnsuccessfullResultDTO() {
        return new ResultDTO();
    }

    public Boolean isSuccessfull() {
        return success;
    }

    public List<Direction> getDirections() {
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
