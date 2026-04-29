package serverClient;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }


    private void broadcast(String message){
        for(ClientHandler client:Server.clients){
            client.out.println(message);
        }
    }


    public void run()

    {
        try {

            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out=new PrintWriter(socket.getOutputStream(),true);
            out.println("Welcome To Chat!");
            String message;
            while ((message=in.readLine())!=null){
                System.out.println("Client says: "+message);
                broadcast(message);
            }
            Server.clients.remove(this);
            socket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
