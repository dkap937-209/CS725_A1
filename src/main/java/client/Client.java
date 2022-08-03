package client;

import Conn_Info.Connection_Information;
import util.Keyboard;
import util.ReadChars;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        String response;

        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String greeting = ReadChars.readStringIn(in);
            System.out.println(greeting);
            while(true){
                /* Send Command to the Server **/
                System.out.print(">");
                String input = Keyboard.readInput();
                //Keep polling the input until we receive an input
                while(input == null){
                    input = Keyboard.readInput();
                }
                int inputLength = input.length();
                out.writeInt(inputLength);
                out.writeChars(input);
                out.flush();

                //Receiving response from Server
                response = ReadChars.readStringIn(in);
                System.out.println(response);

                if(input.equalsIgnoreCase("DONE")){
                    break;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
