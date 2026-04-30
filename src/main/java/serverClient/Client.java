package serverClient;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    //  Listener for UI
    private MessageListener listener;

    //  Interface (callback)
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    //  Start connection (NO console input anymore)
    public void startClient(String username) {
        try {
            socket = new Socket("localhost", 5000);

            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(
                    socket.getOutputStream(), true);

            // 🔹 Read welcome message
            if (listener != null) {
                listener.onMessageReceived(input.readLine());
                listener.onMessageReceived(input.readLine());
            }

            // 🔹 Send username
            output.println(username);

            // 🔹 Start listening thread
            startListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Send message (called from UI)
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    // Listen to server (background thread)
    private void startListening() {
        new Thread(() -> {
            try {
                String response;
                while ((response = input.readLine()) != null) {

                    //  Send message to UI
                    if (listener != null) {
                        listener.onMessageReceived(response);
                    }
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onMessageReceived("[SYSTEM]: Disconnected from server.");
                }
            }
        }).start();
    }
}