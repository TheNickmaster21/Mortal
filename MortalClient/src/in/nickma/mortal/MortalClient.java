package in.nickma.mortal;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.solving.SimpleSolver;
import in.nickma.mortal.solving.SmartSolver;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Long> timeHistory = new LinkedList<>();
        List<Long> timeHistory2 = new LinkedList<>();

        while (!socket.isClosed()) {
            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                SimpleSolver simpleSolver = new SimpleSolver(workDTO);
                long start = System.nanoTime();
                ResultDTO resultDTO = simpleSolver.solve();
                timeHistory.add(System.nanoTime() - start);
                if (timeHistory.size() % 10 == 0) {
                    System.out.println("Simple solving took "
                            + String.valueOf(timeHistory.stream().collect(Collectors.averagingLong(l -> l)) / 1000000000)
                            + " seconds on average.");
                    timeHistory = new LinkedList<>();
                }

                SmartSolver smartSolver = new SmartSolver(workDTO);
                long start2 = System.nanoTime();
                smartSolver.solve();
                timeHistory2.add(System.nanoTime() - start2);
                if (timeHistory2.size() % 10 == 0) {
                    System.out.println("Smart solving took "
                            + String.valueOf(timeHistory2.stream().collect(Collectors.averagingLong(l -> l)) / 1000000000)
                            + " seconds on average.");
                    timeHistory2 = new LinkedList<>();
                }

                outputStream.writeObject(resultDTO);
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
