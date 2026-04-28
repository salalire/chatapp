package serverClient;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try {

            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
            out.println("Welcome To Chat!");
            String message;
            while ((message=in.readLine())!=null){
                System.out.println("Client says: "+message);
                out.println("Your Messages"+message);
            }
            socket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
