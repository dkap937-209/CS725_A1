import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.ReadChars;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    protected Socket socket;

    public ClientThread(Socket clientSocket) {
        System.out.println("New client thread made");
        this.socket = clientSocket;
    }


    @Override
    public synchronized void start(){
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        String user = null;
        String password = null;
        boolean loggedIn = false;
        JSONArray usersAccounts = null;
        while(true){
            //Reading the input sent from the client
            String str = ReadChars.readStringIn(in);
            JSONParser parser = new JSONParser();
            String res;



            /** User Command **/
            if(str.startsWith("USER")){

                if(str.length() > 4){

                    String userID =  str.substring(5);

                    try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
                        Object obj = parser.parse(reader);
                        JSONArray list = (JSONArray) obj;
                        JSONObject ids = (JSONObject) list.get(0);
                        list = (JSONArray) ids.get("userIDs");
                        boolean found = false;


                        for(Object o: list){
                            JSONObject object = (JSONObject) o;

                            if(object.get("username").equals(userID)){
                                found = true;
                                user = userID;
                                password = (String) object.get("password");
                                usersAccounts = (JSONArray) object.get("accts");


                                //Account has no password
                                if(object.get("password").equals("") && usersAccounts.size() == 0){
                                    res = String.format("!%s logged in", userID);
                                    loggedIn = true;
                                }
                                else{
                                    res ="+User-id valid, send account and password";
                                }
                                sendMessageToClient(res, out);
                                break;
                            }
                        }

                        if(!found){
                            res = "-Invalid user-id, try again";
                            sendMessageToClient(res, out);
                        }
                    } catch (ParseException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            else if(str.startsWith("ACCT")){
                if(str.length() > 4){
                    String accountName =  str.substring(5);

                    //TODO: need it to account for when a user command has been used previously
                    if(usersAccounts != null){

                    }
                    else{

                    }

                    //TODO: make a function that can look through db that takes an argument of the field
                    //For users if they have no pass but an account different msg, also add accounts to user
                    try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
                        Object obj = parser.parse(reader);
                        JSONArray list = (JSONArray) obj;
                        JSONObject ids = (JSONObject) list.get(1);
                        list = (JSONArray) ids.get("accounts");
                        boolean found = false;


                        for(Object o: list){
                            JSONObject object = (JSONObject) o;
                            JSONArray acctUsers = (JSONArray) object.get("users");
                            if(object.get("accountName").equals(accountName)){
                                found = true;
                                if(acctUsers.contains(user) && loggedIn){
                                    res = "! Account valid, logged-in";
                                    sendMessageToClient(res, out);
                                }
                                else{
                                    res = "+Account valid, send password";
                                    sendMessageToClient(res, out);
                                }

                                break;
                            }
                        }

                    } catch (ParseException | IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            else if (str.startsWith("PASS")){

                if(str.length() > 4){
                    String pass =  str.substring(5);
                    if(pass.equals(password) && usersAccounts != null){
                        loggedIn = true;
                        res = "! Logged in";
                        sendMessageToClient(res, out);
                    }

                    else if(password == null){
                        //When no USER command wasn't used before
                        try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
                            Object obj = parser.parse(reader);
                            JSONArray list = (JSONArray) obj;
                            boolean found = false;


                            for(Object o: list){
                                JSONObject object = (JSONObject) o;
                                //TODO: need to handle it when the user has accounts
                                if(object.get("password").equals(pass)){
                                    found = true;
                                    password = (String) object.get("password");
                                    res = "+Send Account";
                                    sendMessageToClient(res, out);
                                    break;
                                }
                            }

                            if(!found){
                                res = "-Invalid user-id, try again";
                                sendMessageToClient(res, out);
                            }
                        } catch (ParseException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        //Password is wrong
                        res = "-Wrong password, try again";
                        sendMessageToClient(res, out);
                    }

                }
            }

            else if(str.startsWith("DONE")){
                res = "+LOCALHOST-XX closing connection";
                sendMessageToClient(res, out);
                return;
            }
            else{
                res = "ERROR: Invalid Command\n" +
                        "Available Commands: \"USER\", \"ACCT\", \"PASS\", \"TYPE\", \"LIST\", \"CDIR\", \"KILL\", \"NAME\", \"TOBE\", \"DONE\", \"RETR\", \"SEND\", \"STOP\", \"STOR\", \"SIZE\"";
                sendMessageToClient(res, out);
            }



        }


    }

    public static void sendMessageToClient(String res, DataOutputStream out){
        try{
            int resLength = res.length();
            out.writeInt(resLength);
            out.writeChars(res);
            out.flush();
        } catch (IOException ignored) {
        }
    }
}
