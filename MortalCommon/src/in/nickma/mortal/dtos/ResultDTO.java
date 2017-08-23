package in.nickma.mortal.dtos;

import java.io.Serializable;

public class ResultDTO implements Serializable {

    private final Boolean success;

    private final String path;
    private final Integer startX;
    private final Integer startY;
    private final Integer level;

    private ResultDTO(
            final String path,
            final Integer startX,
            final Integer startY,
            final Integer level) {
        this.success = true;
        this.path = path;
        this.startX = startX;
        this.startY = startY;
        this.level = level;
    }

    private ResultDTO() {
        this.success = false;
        this.path = null;
        this.startX = null;
        this.startY = null;
        this.level = null;
    }

    public static ResultDTO getSuccessfullResultDTO(
            final String path,
            final Integer startX,
            final Integer startY,
            final Integer level) {
        return new ResultDTO(path, startX, startY, level);
    }

    public static ResultDTO getUnsuccessfullResultDTO() {
        return new ResultDTO();
    }

    public Boolean isSuccessfull() {
        return success;
    }

    public String getPath() {
        return path;
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
