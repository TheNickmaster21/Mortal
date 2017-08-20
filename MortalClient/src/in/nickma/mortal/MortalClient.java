package in.nickma.mortal;

import in.nickma.mortal.dto.WorkDTO;

import java.io.*;
import java.net.Socket;

public class MortalClient {

    public static void main(String[] args) throws IOException {
        MortalClient mortalClient = new MortalClient();
        mortalClient.start();
    }

    public void start() throws IOException {
        System.out.println("MortalClient is starting...");

        Socket socket;

        try {
            socket = new Socket("localhost", 8081);
        } catch (Exception e) {
            System.out.println("Initializing error. Make sure that server is alive!\n" + e);
            return;
        }

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        while (true) {
            //receive message
            WorkDTO workDTO = receiveWorkDTO(inputStream);
            if (workDTO != null) {
                System.out.println("<New message from server> " + workDTO.getMessage());

                //get text from user
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                String message = data.readLine();

                //send message
                outputStream.writeObject(new WorkDTO(message));

                //receive response
                System.out.println("<Response from server> " + workDTO.getMessage());

                //close socket when receive exit
                if (message.equalsIgnoreCase("exit")) {
                    outputStream.close();
                    inputStream.close();
                    data.close();
                    socket.close();
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
