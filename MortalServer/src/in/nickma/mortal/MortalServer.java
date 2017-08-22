package in.nickma.mortal;


import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.enums.Direction;

import java.io.*;
import java.util.Stack;
import java.util.stream.Collectors;


public class MortalServer {

    public static void main(String args[]) throws Exception {
        PropertyHandler propertyHandler = new PropertyHandler();
        ExternalCommunicationHandler externalCommunicationHandler = new ExternalCommunicationHandler(
                propertyHandler.getUsername(),
                propertyHandler.getPassword());

        MortalServer mortalServer = new MortalServer(externalCommunicationHandler);

        while (mortalServer.isActive()) {
            Thread.sleep(100);
        }
    }

    private ExternalCommunicationHandler externalCommunicationHandler;
    private Stack<WorkDTO> workStack = new Stack<>();
    private Boolean active = true;

    private MortalServer(ExternalCommunicationHandler externalCommunicationHandler) {
        this.externalCommunicationHandler = externalCommunicationHandler;
        this.initialize();
    }

    private void initialize() {
        InputStream inputStream = externalCommunicationHandler.getInitialStream();
        if (inputStream == null) {
            return;
        }
        try {
            buildWorkDTOsFromInputStream(inputStream);

            ClientCommunicator clientCommunicator = new ClientCommunicator(this, 8081);
            clientCommunicator.run();
        } catch (IOException e) {
            closeProgramWithException(e);
        }
    }

    public WorkDTO getWork() {
        if (!workStack.empty()) {
            return workStack.pop();
        }
        return null;
    }

    public void receiveResult(final ResultDTO resultDTO) {
        if (resultDTO.isSuccessfull()) {
            String path = resultDTO.getDirections()
                    .stream()
                    .map(Direction::toString)
                    .collect(Collectors.joining(""));
            InputStream inputStream = externalCommunicationHandler.getSubmissionsStream(
                    path,
                    resultDTO.getStartX(),
                    resultDTO.getStartY());
            buildWorkDTOsFromInputStream(inputStream);
        }
    }

    private void buildWorkDTOsFromInputStream(final InputStream inputStream) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (!line.startsWith("\t\t\t<param name=\"FlashVars\"")) continue;
                workStack.removeAllElements();
                line = line.split("\"")[3];
                String[] ss = line.split("(=|&)");
                Integer sizeX = Integer.parseInt(ss[1]);
                Integer sizeY = Integer.parseInt(ss[3]);
                String boardString = ss[5];
                for (int x = 0; x < sizeX; x++) {
                    for (int y = 0; y < sizeY; y++) {
                        //TODO Handle level
                        workStack.push(new WorkDTO(boardString, sizeX, sizeY, x, y, 1));
                    }
                }
                //workStack.push(new WorkDTO(boardString, sizeX, sizeY, 3, 6, 1));

                break;
            }
        } catch (IOException e) {
            closeProgramWithException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    closeProgramWithException(e);
                }
            }
        }
    }

    private void closeProgramWithException(Exception e) {
        e.printStackTrace();
        this.active = false;
    }

    public Boolean isActive() {
        return active;
    }
}
