package in.nickma.mortal;

import in.nickma.mortal.dtos.ResultDTO;
import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.solving.SimpleSolver;

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

        System.out.println("Server found!");

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        List<Long> timeHistory = new LinkedList<>();

        while (!socket.isClosed())

        {
            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                SimpleSolver simpleSolver = new SimpleSolver(workDTO);

                long start = System.nanoTime();
                ResultDTO resultDTO = simpleSolver.solve();
                timeHistory.add(System.nanoTime() - start);
                if (timeHistory.size() % 10 == 0) {
                    System.out.println("Solving took "
                            + timeHistory.stream().collect(Collectors.averagingLong(l -> l))
                            + " nanoseconds on average.");
                    timeHistory = new LinkedList<>();
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
