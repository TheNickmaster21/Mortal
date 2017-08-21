package in.nickma.mortal.dtos;

import java.io.Serializable;

public class WorkDTO implements Serializable {

    private final String gridData;
    private final Integer sizeX;
    private final Integer sizeY;
    private final Integer startX;
    private final Integer startY;

    public WorkDTO(
            final String gridData,
            final Integer sizeX,
            final Integer sizeY,
            final Integer startX,
            final Integer startY) {
        this.gridData = gridData;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.startX = startX;
        this.startY = startY;
    }

    public String getGridData() {
        return gridData;
    }

    public Integer getSizeX() {
        return sizeX;
    }

    public Integer getSizeY() {
        return sizeY;
    }

    public Integer getStartX() {
        return startX;
    }

    public Integer getStartY() {
        return startY;
    }
}
