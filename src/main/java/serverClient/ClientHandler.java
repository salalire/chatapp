package serverClient;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private String userName;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }


    private void broadcast(String message){
        for(ClientHandler client:Server.clients){
            if(client!=this&client.out!=null){
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
            out.println("Enter Username:");

            userName = in.readLine();

            System.out.println("[SERVER] " + userName + " joined");

            //  notify others
            broadcast("[SERVER]: " + userName + " joined");

            String message;

            while ((message = in.readLine()) != null) {
                System.out.println(userName + ": " + message);

                broadcast(userName + ": " + message);
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Connection issue with " + userName);
        } finally {
            try {
                //  remove client
                Server.clients.remove(this);


                if (userName != null) {
                    System.out.println("[SERVER] " + userName + " left");
                    broadcast("[SERVER]: " + userName + " left");
                }

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
