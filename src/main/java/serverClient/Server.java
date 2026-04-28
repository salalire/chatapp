package serverClient;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandiler=new ClientHandler(socket);
                new Thread(clientHandiler).start();
//                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}