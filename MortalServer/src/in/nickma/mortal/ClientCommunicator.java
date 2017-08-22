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
                new ClientSocketThread();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class ClientSocketThread implements Runnable {

        private final Socket socket;
        private WorkDTO workDTO;

        public ClientSocketThread() throws IOException {
            this.socket = serverSocket.accept();
            System.out.println("Connected with new client!");
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
                    workDTO = mortalServer.getWork();
                    if (workDTO != null) {
                        outputStream.writeObject(workDTO);
                        try {
                            ResultDTO resultDTO = (ResultDTO) inputStream.readObject();
                            mortalServer.receiveResult(resultDTO);
                        } catch (ClassNotFoundException e) {
                            System.out.println(e.getMessage());
                            System.out.println("Client sent garbage result; giving back work.");
                            giveBackWork();
                        }
                    } else {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Client connection died; giving back work and cleaning up socket");
                giveBackWork();
                if (socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException ioE) {
                        System.out.println(ioE.getMessage());
                        System.out.println("Issue closing socket");
                    }
                }
            }
        }

        private void giveBackWork() {
            if (workDTO != null) {
                mortalServer.giveWork(workDTO);
            }
        }
    }
}
