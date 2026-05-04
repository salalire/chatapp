package serverClient;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private MessageListener listener;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    public void startClient(String username) {
        try {
            socket = new Socket("localhost", 5000);

            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(
                    socket.getOutputStream(), true);

            // read welcome
            if (listener != null) {
                String line1 = input.readLine();
                if (line1 != null) listener.onMessageReceived(line1);
            }

            // send username
            output.println("USERNAME:" + username);

            startListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendPrivateMessage(String targetUser, String message) {
        if (output != null) {
            output.println("PRIVATE:" + targetUser + ":" + message);
        }
    }



    public void sendMessage(String message) {
        if (output != null) {
            output.println("MSG:" + message);
        }
    }

    private void startListening() {
        new Thread(() -> {
            try {
                String response;
                while ((response = input.readLine()) != null) {


                    if (response.startsWith("USERLIST:")) {

                        String users = response.substring(9);

                        if (listener != null) {
                            listener.onMessageReceived("USERLIST:" + users);
                        }

                        continue;
                    }

                    String cleanMessage = response;

                    // remove protocol if any future use
                    if (response.startsWith("MSG:")) {
                        cleanMessage = response.substring(4);
                    }

                    if (listener != null) {
                        listener.onMessageReceived(cleanMessage);
                    }
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onMessageReceived("[SYSTEM]: Disconnected");
                }
            }
        }).start();
    }
}