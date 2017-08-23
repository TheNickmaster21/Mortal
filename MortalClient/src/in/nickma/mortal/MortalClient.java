package in.nickma.mortal;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.solving.SimpleSolver;
import in.nickma.mortal.solving.SmartSolver;

import java.io.*;
import java.net.Socket;

public class MortalClient {

    public static void main(String[] args) throws IOException {
        MortalClient mortalClient = new MortalClient();
        mortalClient.start();
    }

    public void start() throws IOException {
        System.out.println("Mortal Client is starting...");
        Socket socket = connectWithServer();
        System.out.println("Server found!");

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        while (!socket.isClosed()) {
            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                SimpleSolver simpleSolver = new SimpleSolver(workDTO);
                ResultDTO resultDTO = simpleSolver.solve();
                if (resultDTO.isSuccessfull()) {
                    SmartSolver smartSolver = new SmartSolver(workDTO);
                    outputStream.writeObject(smartSolver.solve());
                } else {
                    outputStream.writeObject(resultDTO);
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Socket connectWithServer() {
        Socket socket = null;
        System.out.println("Looking for the server");
        while (socket == null || socket.isClosed()) {
            try {
                socket = new Socket("localhost", 8081);
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    System.out.println(ie.getMessage());
                }
            }
        }
        return socket;
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
}
