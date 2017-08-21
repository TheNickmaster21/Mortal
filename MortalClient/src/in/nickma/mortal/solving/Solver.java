package in.nickma.mortal.solving;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;

import java.util.ArrayList;
import java.util.Stack;

public class Solver {

    private final Stack<Step> stepStack = new Stack<>();

    public Solver(final WorkDTO workDTO) {
        Boolean[][] grid = new Boolean[workDTO.getSizeX()][workDTO.getSizeY()];

        for (int x = 0; x < workDTO.getSizeX(); x++) {
            for (int y = 0; y < workDTO.getSizeY(); y++) {
                grid[x][y] = workDTO.getGridData().charAt(x + workDTO.getSizeY() * y) == 'X';
            }
        }

        stepStack.push(new Step(grid, new ArrayList<>(), workDTO.getStartX(), workDTO.getStartY()));
    }

    public ResultDTO solve() {
        while (!stepStack.empty()) {
            Step step = stepStack.pop();

            System.out.println(step.getX());
        }
        return ResultDTO.getUnsuccessfullResultDTO();
    }
}
