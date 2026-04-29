package serverClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void startClient() {
        try {
            socket = new Socket("localhost", 5000);

            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println(input.readLine());
            System.out.print(input.readLine() + " ");
            String username = scanner.nextLine();
            output.println(username);
            startListening();
            while (true) {
                String message = scanner.nextLine();
                displayOwnMessage(message);
                output.println(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayOwnMessage(String message) {
        System.out.println("[Me]: " + message);
    }


    private void startListening() {
        new Thread(() -> {
            try {
                String response;
                while ((response = input.readLine()) != null) {
                    displayIncomingMessage(response);
                }
            } catch (Exception e) {
                System.out.println("Disconnected from server.");
            }
        }).start();
    }

    private void displayIncomingMessage(String message) {
        System.out.println(message);
    }
}