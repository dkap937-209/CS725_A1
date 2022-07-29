//import org.json.simple.JSONArray;
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

import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;
        import util.ReadChars;

        import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.FileReader;
        import java.io.IOException;
        import java.net.InetAddress;
        import java.net.ServerSocket;
        import java.net.Socket;
import java.net.SocketException;

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
//            }
//            try(Socket clientConnection = socket.accept()){
//
//                DataInputStream in = new DataInputStream(clientConnection.getInputStream());
//                DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
////                out.writeChars("+localhost -XX SFTP Service");
//                StringBuilder builder =  new StringBuilder();;
//                int inputLength;
//                String user = null;
//                String password = null;
//                String account = null;
//                new ClientThread(clientConnection).start();


//                while(true){
//                    //Reading the input sent from the client
//                    String str = ReadChars.readStringIn(in);
//                    JSONParser parser = new JSONParser();
//                    String res;
//
//                    /** User Command **/
//                    if(str.startsWith("USER")){
//                        System.out.println("Perform User command");
//
//                        if(str.length() > 4){
//
//                            String userID =  str.substring(5);
//
//                            try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
//                                Object obj = parser.parse(reader);
//                                JSONArray list = (JSONArray) obj;
//                                boolean found = false;
//
//
//                                for(Object o: list){
//                                    JSONObject object = (JSONObject) o;
//
//                                    if(object.get("username").equals(userID)){
//                                        found = true;
//                                        user = userID;
//                                        password = (String) object.get("password");
//
//                                        //Account has no password
//                                        if(object.get("password").equals("")){
//                                            res = String.format("!%s logged in", userID);
//                                        }
//                                        else{
//                                            res ="+User-id valid, send account and password";
//                                        }
//                                        sendMessageToClient(res, out);
//                                        break;
//                                    }
//                                }
//
//                                if(!found){
//                                    res = "-Invalid user-id, try again";
//                                    sendMessageToClient(res, out);
//                                }
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//
//                    else if(str.startsWith("ACCT")){
//                        System.out.println("Perform account command");
//                    }
//                    else if (str.startsWith("PASS")){
//                        System.out.println("Perform password command");
//
//                        if(str.length() > 4){
//                            String pass =  str.substring(5);
//
//                            System.out.println("Value of pass: "+ pass);
//                            if(pass.equals(password)){
//                                System.out.println("Password is right");
//                                res = "! Logged in";
//                                sendMessageToClient(res, out);
//                            }
//                            else if(password == null){
//                                //When no USER command wasn't used before
//                                try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
//                                    Object obj = parser.parse(reader);
//                                    JSONArray list = (JSONArray) obj;
//                                    boolean found = false;
//
//
//                                    for(Object o: list){
//                                        JSONObject object = (JSONObject) o;
//
//                                        if(object.get("password").equals(pass)){
//                                            found = true;
//                                            password = (String) object.get("password");
//                                            res = "+Send Account";
//                                            sendMessageToClient(res, out);
//                                            break;
//                                        }
//                                    }
//
//                                    if(!found){
//                                        res = "-Invalid user-id, try again";
//                                        sendMessageToClient(res, out);
//                                    }
//                                } catch (ParseException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                            else{
//                                //Password is wrong
//                                res = "-Wrong password, try again";
//                                sendMessageToClient(res, out);
//                            }
//
//                        }
//                    }
//
//                    else if(str.startsWith("DONE")){
//                        res = "+LOCALHOST-XX closing connection";
//                        sendMessageToClient(res, out);
//                        clientConnection.close();
//                        break;
//                    }
//                    else{
//                        res = "ERROR: Invalid Command\n" +
//                                "Available Commands: \"USER\", \"ACCT\", \"PASS\", \"TYPE\", \"LIST\", \"CDIR\", \"KILL\", \"NAME\", \"TOBE\", \"DONE\", \"RETR\", \"SEND\", \"STOP\", \"STOR\", \"SIZE\"";
//                        sendMessageToClient(res, out);
//                    }
//
//
//
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("IOException");
//            e.printStackTrace();
//        }

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

