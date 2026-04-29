package serverClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<ClientHandler> clients = new ArrayList<>();
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler=new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
//                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}