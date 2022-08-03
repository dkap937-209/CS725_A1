package server;//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import util.ReadChars;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Server {
//    public static void main(String[] args) {
//
//
//        try (ServerSocket socket = new ServerSocket(Connection_Information.PORT_NUMBER)) {
//            InetAddress serverHost = InetAddress.getLocalHost();
//            System.out.println("Server destination: " + serverHost.getHostAddress() + ":" + socket.getLocalPort());
//
//            try(Socket clientConnection = socket.accept()){
//
//                DataInputStream in = new DataInputStream(clientConnection.getInputStream());
//                DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
//                out.writeChars("Successfully connected to localhost on port 6789");
//                out.writeChars("+RFC 913 SFTP Server");
//                StringBuilder builder =  new StringBuilder();;
//                int inputLength;
//
//                while(true){
//                    //Reading the input sent from the client
//                    String str = ReadChars.readStringIn(in);
//                    JSONParser parser = new JSONParser();
//                    String res;
//                    /** User Commands **/
//                    if(str.startsWith("USER")){
//                        System.out.println("Perform User command");
//
//                        if(str.length() > 4){
//
//                            String userID =  str.substring(5, str.length());
//
//                            try(FileReader reader = new FileReader("src/main/resources/db.json")){
//                                Object obj = parser.parse(reader);
//                                JSONArray list = (JSONArray) obj;
//                                boolean found = false;
//                                for(Object o: list){
//                                    JSONObject object = (JSONObject) o;
//
//                                    if(object.get("username").equals(userID)){
//                                        found = true;
//                                        res = String.format("!%s logged in", userID); //Need different message if it requires password
//                                        System.out.println(res);
//                                        int resLength = res.length();
//                                        out.writeInt(resLength);
//                                        out.writeChars(res);
//                                        out.flush();
//                                        break;
//                                    }
//                                }
//
//                                if(!found){
//                                    res = "-Invalid user-id, try again";
//                                    int resLength = res.length();
//                                    out.writeInt(resLength);
//                                    out.writeChars(res);
//                                }
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
////                        String userID =  str.substring(5, str.length());
////                        System.out.println(userID);
////                        try(FileReader reader = new FileReader("src/main/resources/db.json")){
////                            Object obj = parser.parse(reader);
////                            JSONArray list = (JSONArray) obj;
////
////                            for(Object o: list){
////                                JSONObject object = (JSONObject) list.get(0);
////                                System.out.println(object.get("username"));
////                            }
////
////
////
////                        } catch (ParseException e) {
////                            throw new RuntimeException(e);
////                        }
//
//                    }
//                    else if(str.startsWith("ACCT")){
//                        System.out.println("Perform account command");
//                    }
//                    else if (str.startsWith("PASS")){
//                        System.out.println("Perform password command");
//                    }
//
//
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("IOException");
//            e.printStackTrace();
//        }
//
//    }
//}

import Conn_Info.Connection_Information;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        while (true) {
            try (ServerSocket socket = new ServerSocket(Connection_Information.PORT_NUMBER)) {
                InetAddress serverHost = InetAddress.getLocalHost();
                System.out.println("Server destination: " + serverHost.getHostAddress() + ":" + socket.getLocalPort());


//            while (true) {
                try (Socket clientConnection = socket.accept()) {
                    new ClientThread(clientConnection).start();
                } catch (Exception ignored) {
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendMessageToClient(String res, DataOutputStream out){
        try{
            int resLength = res.length();
            out.writeInt(resLength);
            out.writeChars(res);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

