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

//            String greeting = ReadChars.readStringIn(in);
//            System.out.println("Greeting: "+greeting);
            //TODO: Need to convert it to bytes
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

//                System.out.println("Receving res from server");
//                //Receiving response from Server
////                response = ReadChars.readStringIn(in);
//                StringBuilder builder = new StringBuilder();
////                inputLength = in.readInt();
//                for(int i=0; i<14; i++){
//                    builder.append(in.readChar());
//                }
//                System.out.println(builder);

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
