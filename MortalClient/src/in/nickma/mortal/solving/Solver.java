package in.nickma.mortal.solving;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.enums.Direction;

import java.util.*;

public class Solver {

    private final Stack<Step> stepStack = new Stack<>();
    private ResultDTO result = null;
    private WorkDTO workDTO;

    public Solver(final WorkDTO workDTO) {
        this.workDTO = workDTO;
    }

    public ResultDTO solve() {
        buildInitialStep();

        while (!stepStack.empty() && result == null) {
            buildNextSteps(stepStack.pop());
        }
        if (result != null) {
            return result;
        } else {
            return ResultDTO.getUnsuccessfullResultDTO();
        }
    }

    private void buildInitialStep() {
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
            stepStack.push(new Step(grid, spacesLeft, new ArrayList<>(), workDTO.getStartX(), workDTO.getStartY()));
        } else {
            result = ResultDTO.getUnsuccessfullResultDTO();
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
                List<Direction> newDirectionHistory = new ArrayList<>(step.getDirectionHistory());
                newDirectionHistory.add(direction);
                //System.out.print(step.getX());
                //System.out.print(',');
                //System.out.print(step.getY());
                //System.out.println(direction.toString());
                //printGrid(newGrid, nextX - 1, nextY - 1);
                System.out.println();
                if (newSpacesLeft == 0) {
                    result = ResultDTO.getSuccessfullResultDTO(
                            newDirectionHistory,
                            workDTO.getStartX(),
                            workDTO.getStartY(),
                            workDTO.getLevel());
                    return;
                }

                stepStack.push(new Step(
                        newGrid,
                        newSpacesLeft,
                        newDirectionHistory,
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
