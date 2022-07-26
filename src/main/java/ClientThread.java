import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.ReadChars;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    protected Socket socket;

    public ClientThread(Socket clientSocket) {
        System.out.println("New client thread made");
        this.socket = clientSocket;
    }


    @Override
    public synchronized void start(){
        DataInputStream in = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            brinp = new BufferedReader(new InputStreamReader(in));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        String user = null;
        String password = null;
        String account = null;
        while(true){
            //Reading the input sent from the client
            String str = ReadChars.readStringIn(in);
            JSONParser parser = new JSONParser();
            String res;



            /** User Command **/
            if(str.startsWith("USER")){
                System.out.println("Perform User command");

                if(str.length() > 4){

                    String userID =  str.substring(5);

                    try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
                        Object obj = parser.parse(reader);
                        JSONArray list = (JSONArray) obj;
                        boolean found = false;


                        for(Object o: list){
                            JSONObject object = (JSONObject) o;

                            if(object.get("username").equals(userID)){
                                found = true;
                                user = userID;
                                password = (String) object.get("password");

                                //Account has no password
                                if(object.get("password").equals("")){
                                    res = String.format("!%s logged in", userID);
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
                System.out.println("Perform account command");
            }
            else if (str.startsWith("PASS")){
                System.out.println("Perform password command");

                if(str.length() > 4){
                    String pass =  str.substring(5);

                    if(pass.equals(password)){
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
