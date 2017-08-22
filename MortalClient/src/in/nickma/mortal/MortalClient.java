package in.nickma.mortal;

import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.solving.SimpleSolver;

import java.io.*;
import java.net.Socket;

public class MortalClient {

    public static void main(String[] args) throws IOException {
        MortalClient mortalClient = new MortalClient();
        mortalClient.start();
    }

    public void start() throws IOException {
        System.out.println("Mortal Client is starting...");

        Socket socket;

        try {
            socket = new Socket("localhost", 8081);
        } catch (Exception e) {
            System.out.println("Initializing error. Make sure that server is alive!\n" + e);
            return;
        }

        System.out.println("Socket opened");

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        while (!socket.isClosed()) {
            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                System.out.println("Starting work");
                SimpleSolver simpleSolver = new SimpleSolver(workDTO);
                outputStream.writeObject(simpleSolver.solve());
            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
