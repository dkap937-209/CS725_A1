import util.Keyboard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Idea maybe: It takes keyboard input, and we send it to the server where it does operations accordingly
//            Keyboard.prompt("Enter a command: ");

            System.out.println("Enter a command");
            while(true){
                if(Keyboard.readInput() != null){
                    //Do stuff

                    System.out.println("Enter a command");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
