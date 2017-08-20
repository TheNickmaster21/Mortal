package in.nickma.mortal.dtos;

import java.io.Serializable;

public class WorkDTO implements Serializable {

    private final String gridData;
    private final Integer startX;
    private final Integer startY;

    public WorkDTO(
            final String gridData,
            final Integer startX,
            final Integer startY) {
        this.gridData = gridData;
        this.startX = startX;
        this.startY = startY;
    }

    public String getGridData() {
        return gridData;
    }

    public Integer getStartX() {
        return startX;
    }

    public Integer getStartY() {
        return startY;
    }
}
