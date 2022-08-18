package client;

import Conn_Info.Connection_Information;
import util.Keyboard;
import util.ReadChars;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

public class Client {

    private static Stack<String> outputStack = new Stack<String>();
    private static Stack<String> inputStack = new Stack<String>();
    public static void main(String[] args){

        String response = "";
        String input ="";
        BufferedInputStream buffIn;
        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            buffIn = new BufferedInputStream(socket.getInputStream());
            inputStack.push(input);

            String greeting = ReadChars.readStringIn(in);
            System.out.println(greeting);


            while(true){

                //If the previous input was SEND, then prepare to receive a file from the server
                if(inputStack.peek().equalsIgnoreCase("SEND")){
                    String filename = getFilename();
                    FileOutputStream fos = new FileOutputStream("src/main/resources/client_files/"+filename);
                    BufferedOutputStream bufferedOutStream = new BufferedOutputStream(fos);
                    buffIn = new BufferedInputStream(socket.getInputStream());

                    int fileSize = getFileSize();
                    for (int i = 0; i < fileSize; i++) {
                        int r = buffIn.read();
                        bufferedOutStream.write(r);
                    }
                    bufferedOutStream.close();
                }
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
                inputStack.push(input);

                //If the command is "size" then prepare to send the file to the server
                if(input.toUpperCase().startsWith("SIZE") && input.split(" ").length<3){
                    inputStack.pop();
                    String storCmd = inputStack.pop();
                    String fileName = storCmd.substring(9);
                    String size = input.substring(5);
                    int fileSize = Integer.parseInt(input.substring(5));

                    String filePath = "src/main/resources/client_files/" + fileName;
                    File fileToSend = new File(filePath);

                    byte[] bytes = new byte[fileSize];
                    FileInputStream fis = new FileInputStream(fileToSend);
                    BufferedInputStream bufferedInStream = new BufferedInputStream(new FileInputStream(fileToSend));
                    int content = 0;
                    //read the file and write to buffer byte by byte
                    while ((content = bufferedInStream.read(bytes)) >= 0) {
                        out.write(bytes, 0, content);
                    }
                    bufferedInStream.close();
                    fis.close();
                    out.flush();
                }

                //Receiving response from Server
                response = ReadChars.readStringIn(in);
                System.out.println(response);
                outputStack.push(response);

                if(input.equalsIgnoreCase("DONE")){
                    break;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the size of the file to send from the previous messages
     *
     * @return {@link Integer}: Size of the file in bytes
     */
    public static int getFileSize(){
        outputStack.pop();
        String outputSizeMsg = outputStack.pop();
        String[] split = outputSizeMsg.split(" ");
        return Integer.parseInt(split[0]);
    }

    /**
     * Retrieve the name of the file to send to the server from previous inputs
     *
     * @return  {@link String}: Name of the file
     */
    public static String getFilename(){
        inputStack.pop();
        String fileNameCmd = inputStack.pop();
        String fileNameDir = fileNameCmd.split(" ")[1];
        String[] s = fileNameDir.split("/");
        return s[s.length-1];
    }


}

