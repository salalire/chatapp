package serverClient;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private String userName;


    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private void sendToUser(String target, String message) {
        for (ClientHandler client : Server.clients) {
            if (client.userName.equals(target)) {
                client.out.println(message);
                return;
            }
        }
    }



    private void broadcast(String message) {
        for (ClientHandler client : Server.clients) {
            if (client != this && client.out != null) {
                client.out.println(message);
            }
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Welcome To Chat!");

            // 🔹 read username
            String firstLine = in.readLine();

            if (firstLine != null && firstLine.startsWith("USERNAME:")) {
                userName = firstLine.substring(9);
            } else {
                socket.close();
                return;
            }

            System.out.println(userName + " joined");

            broadcast("[SERVER]: " + userName + " joined");

            String message;

            while ((message = in.readLine()) != null) {

                if (message.startsWith("MSG:")) {
                    String realMsg = message.substring(4);
                    broadcast(userName + ": " + realMsg);
                }

                else if (message.startsWith("PRIVATE:")) {

                    String[] parts = message.split(":", 3);

                    String targetUser = parts[1];
                    String privateMsg = parts[2];

                    sendToUser(targetUser, "[PRIVATE] " + userName + ": " + privateMsg);
                }
            }

        } catch (Exception e) {
            System.out.println("Error with " + userName);
        } finally {
            try {
                Server.clients.remove(this);

                if (userName != null) {
                    broadcast("[SERVER]: " + userName + " left");
                }

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}