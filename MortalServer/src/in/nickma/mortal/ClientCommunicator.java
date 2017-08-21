package in.nickma.mortal;

import in.nickma.mortal.dtos.WorkDTO;
import in.nickma.mortal.dtos.ResultDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientCommunicator implements Runnable {

    private MortalServer mortalServer;
    private ServerSocket serverSocket;

    public ClientCommunicator(final MortalServer mortalServer, final Integer port) throws IOException {
        this.mortalServer = mortalServer;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server is ready!");
    }

    @Override
    public void run() {
        while (true) {
            try {
                new SocketThread();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class SocketThread implements Runnable {

        private final Socket socket;

        public SocketThread() throws IOException {
            this.socket = serverSocket.accept();
            System.out.println("Started new socket!");
            this.run();
        }

        @Override
        public void run() {
            ObjectOutputStream outputStream;
            ObjectInputStream inputStream;
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    WorkDTO workDTO = mortalServer.getWork();
                    if (workDTO != null) {
                        outputStream.writeObject(workDTO);
                        try {
                            ResultDTO resultDTO = (ResultDTO) inputStream.readObject();
                            mortalServer.receiveResult(resultDTO);
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    } else {
                        System.out.println("Client waiting for work..");
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
