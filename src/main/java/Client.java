import util.Keyboard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());


            while(true){
                // Send command to Server
                System.out.println("Enter a command");
                String input = Keyboard.readInput();
                while(input == null){
                    input = Keyboard.readInput();
                }
                int inputLength = input.length();
                out.writeInt(inputLength);
                out.writeChars(input);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
