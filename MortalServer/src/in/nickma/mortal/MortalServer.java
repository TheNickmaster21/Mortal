package in.nickma.mortal;


import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.dtos.ResultDTO;

import java.io.*;
import java.util.Stack;


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

            new ClientCommunicator(this, 8081);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public WorkDTO getWork() {
        if (!workStack.empty()) {
            return workStack.pop();
        }
        return null;
    }

    public void receiveResult(final ResultDTO resultDTO) {

    }

    private void buildWorkDTOsFromInputStream(final InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.startsWith("\t\t\t<param name=\"FlashVars\"")) continue;
            line = line.split("\"")[3]; // get just the value
            String[] ss = line.split("(=|&)"); // split up the name value pairs
            Integer boardX = Integer.parseInt(ss[1]);
            Integer boardY = Integer.parseInt(ss[3]);
            String boardString = ss[5];
            for (int x = 1; x <= boardX; x++) {
                for (int y = 1; y <= boardY; y++) {
                    workStack.push(new WorkDTO(boardString, x, y));
                }
            }
        }
        bufferedReader.close();
    }

    private WorkDTO receiveWorkDTO(final ObjectInputStream inputStream) {
        try {
            return (WorkDTO) (inputStream.readObject());
        } catch (IOException exception) {
            return null;
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    public Boolean isActive() {
        return active;
    }
}
