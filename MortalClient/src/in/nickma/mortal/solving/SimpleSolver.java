package in.nickma.mortal.solving;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.enums.Direction;

import java.util.*;

public class SimpleSolver implements Solver {

    private ResultDTO result = null;
    private WorkDTO workDTO;

    public SimpleSolver(final WorkDTO workDTO) {
        this.workDTO = workDTO;
    }

    @Override
    public ResultDTO solve() {
        Step step = buildInitialStep();

        if (step != null) {
            buildNextSteps(step);
        }
        if (result != null) {
            return result;
        } else {
            return ResultDTO.getUnsuccessfullResultDTO();
        }
    }

    private Step buildInitialStep() {
        final boolean[][] grid = new boolean[workDTO.getSizeX()][workDTO.getSizeY()];
        Integer spacesLeft = workDTO.getSizeX() * workDTO.getSizeY() - 1;
        for (int y = 0; y < workDTO.getSizeY(); y++) {
            for (int x = 0; x < workDTO.getSizeX(); x++) {
                boolean open = workDTO.getGridData().charAt(x + workDTO.getSizeX() * y) == '.';
                grid[x][y] = open;
                if (!open) {
                    spacesLeft--;
                }
            }
        }
        if (grid[workDTO.getStartX()][workDTO.getStartY()]) {
            grid[workDTO.getStartX()][workDTO.getStartY()] = false;
            return new Step(grid, spacesLeft, "", workDTO.getStartX(), workDTO.getStartY());
        } else {
            result = ResultDTO.getUnsuccessfullResultDTO();
            return null;
        }
    }

    private void printGrid(boolean[][] grid) {
        printGrid(grid, -1, -1);
    }

    private void printGrid(boolean[][] grid, int xx, int yy) {
        for (int y = 0; y < workDTO.getSizeY(); y++) {
            for (int x = 0; x < workDTO.getSizeX(); x++) {
                if (x == xx && y == yy) {
                    System.out.print('O');
                } else if (grid[x][y]) {
                    System.out.print('-');
                } else {
                    System.out.print('X');
                }
            }
            System.out.println();
        }
    }


    private void buildNextSteps(final Step step) {
        for (Direction direction : Direction.values()) {
            int nextX = step.getX() + direction.getX();
            int nextY = step.getY() + direction.getY();
            if (isSpaceValid(nextX, nextY, step.getGrid())) {
                //printGrid(step.getGrid(), step.getX(), step.getY());
                final boolean[][] newGrid = deepCopy(step.getGrid());
                Integer newSpacesLeft = step.getSpacesLeft();
                do {
                    newGrid[nextX][nextY] = false;
                    newSpacesLeft--;
                    nextX += direction.getX();
                    nextY += direction.getY();
                } while (isSpaceValid(nextX, nextY, newGrid));
                //System.out.print(step.getX());
                //System.out.print(',');
                //System.out.print(step.getY());
                //System.out.println(direction.toString());
                //printGrid(newGrid, nextX - 1, nextY - 1);
                //System.out.println();
                if (newSpacesLeft == 0) {
                    result = ResultDTO.getSuccessfullResultDTO(
                            step.getDirectionHistory() + direction.toString(),
                            workDTO.getStartX(),
                            workDTO.getStartY(),
                            workDTO.getLevel());
                    return;
                }

                buildNextSteps(new Step(
                        newGrid,
                        newSpacesLeft,
                        step.getDirectionHistory() + direction.toString(),
                        nextX - direction.getX(),
                        nextY - direction.getY()));
            }
        }
    }

    private static boolean[][] deepCopy(final boolean[][] original) {
        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    private boolean isSpaceValid(final int x, final int y, final boolean[][] grid) {
        return x >= 0 && x < grid.length
                && y >= 0 && y < grid[0].length
                && grid[x][y];
    }
}
