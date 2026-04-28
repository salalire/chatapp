package serverClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public void requestClient()  {

        try {


            Socket socket = new Socket("localhost", 5000);

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true);

           Scanner scanner=new Scanner(System.in);
           new Thread(()->{
               try{

                   String response;
                   while((response= input.readLine())!=null){
                       System.out.println(response);

                   }
               }
               catch(Exception e){
                   e.printStackTrace();
               }

           }
           ).start();
           while (true){
               String message=scanner.nextLine();
               output.println(message);
           }

        }
        catch(Exception e){
           e.printStackTrace();
        }
    }


}