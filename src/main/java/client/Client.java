package client;

import Conn_Info.Connection_Information;
import util.Keyboard;
import util.ReadChars;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        String response = "";
        String prevInput = "";
        String input ="";
        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String greeting = ReadChars.readStringIn(in);
            System.out.println(greeting);

            while(true){

                if(prevInput.equalsIgnoreCase("SEND")){

                    char[] chars = response.toCharArray();
                    StringBuilder size = new StringBuilder();

                    for(char c: chars){
                        if(Character.isDigit(c)){
                            size.append(c);
                        }
                    }
//                    int fileSize = (int)Math.pow(2, 30);

//                    int fileSize = 20;
//                    byte [] b  = new byte [fileSize];
//                    InputStream is = socket.getInputStream();
//                    System.out.println("point 1");
                    FileOutputStream fos = new FileOutputStream("src/main/resources/client_files/file3.txt");
//                    System.out.println("point 2");
//                    is.read(b, 0, b.length);
//                    System.out.println("point 3");
//                    fos.write(b, 0, b.length);
//                    System.out.println("Over here");

                    int r;
                    while((r=in.read())!=-1){
                        fos.write((char)r);
                    }



                }
                else{
                    /* Send Command to the Server **/
                    System.out.print(">");
                    input = Keyboard.readInput();
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
//                /* Send Command to the Server **/
//                System.out.print(">");
//                String input = Keyboard.readInput();
//                //Keep polling the input until we receive an input
//                while(input == null){
//                    input = Keyboard.readInput();
//                }
//                int inputLength = input.length();
//                out.writeInt(inputLength);
//                out.writeChars(input);
//                out.flush();
//
//
////                if(prevInput.equalsIgnoreCase("SEND")){
////
////                    char[] chars = response.toCharArray();
////                    StringBuilder size = new StringBuilder();
////
////                    for(char c: chars){
////                        if(Character.isDigit(c)){
////                            size.append(c);
////                        }
////                    }
////
////                    int fileSize = Integer.parseInt(size.toString());
////                    byte [] b  = new byte [fileSize];
////                    InputStream is = socket.getInputStream();
////                    FileOutputStream fos = new FileOutputStream("src/main/resources/client_files/file3.txt");
////                    is.read(b, 0, b.length);
////                    fos.write(b, 0, b.length);
////
////                }
//
//                //Receiving response from Server
//                response = ReadChars.readStringIn(in);
//                System.out.println(response);
//
//
//
//                if(input.equalsIgnoreCase("DONE")){
//                    break;
//                }

                prevInput = input;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}

