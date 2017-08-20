package in.nickma.mortal;


import in.nickma.mortal.dto.WorkDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MortalServer {

    public static void main(String args[]) throws Exception {
        MortalServer mortalServer = new MortalServer();
        mortalServer.start();
    }

    public void start() throws IOException {
        System.out.println("Server is starting...");

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(8081);
            System.out.println("Server is ready!");
        } catch (Exception e) {
            System.out.println("Initializing error. Try changing port number!" + e);
            return;
        }

        Socket clientSocket = serverSocket.accept();

        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

        outputStream.writeObject(new WorkDTO("Give me some text!"));

        while (true) {

            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                System.out.println("<New message from client> " + workDTO.getMessage());
                outputStream.writeObject(new WorkDTO(new StringBuilder(workDTO.getMessage()).reverse().toString()));

                if (workDTO.getMessage().equalsIgnoreCase("exit")) {
                    System.out.println("Session closed!");
                    outputStream.close();
                    inputStream.close();
                    clientSocket.close();
                    serverSocket.close();
                    break;
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
