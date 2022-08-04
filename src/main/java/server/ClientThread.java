package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.ReadChars;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientThread extends Thread {
//public class ClientThread implements Runnable {
    protected Socket socket;
    private final String BASE_DIR = "src/main/resources/server_files/user_files/";

    public ClientThread(Socket clientSocket) {
        System.out.println("New client thread made");
        this.socket = clientSocket;
    }


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
        boolean pendingDirChange = false;
        JSONArray usersAccounts = null;
        String selectedAccount = null;
        int numAccounts = 0;
        String currDir = "src/main/resources/server_files/user_files/";

        boolean userEntered = false;
        boolean passEntered = false;
        sendMessageToClient("Successfully connected to localhost on port 6789\n" +
                "+RFC 913 SFTP Server", out);


        while(true){
            //Reading the input sent from the client
            String str = ReadChars.readStringIn(in);
            JSONParser parser = new JSONParser();
            String res = "";

            String cmd = str.substring(0, 4);
            cmd = cmd.toUpperCase();

            /** User Command **/
            if(cmd.startsWith("USER")){

                if(str.length() > 4){

                    String userID =  str.substring(5);

                    if(!isValidInput(userID)){
                        res = "ERROR: Invalid Arguments\n" +
                                "Usage: USER user-id";
                    }
                    else{
                        try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){

                            //Retrieving value for userID key in json file
                            Object obj = parser.parse(reader);
                            JSONArray list = (JSONArray) obj;
                            JSONObject ids = (JSONObject) list.get(0);
                            list = (JSONArray) ids.get("userIDs");
                            boolean found = false;

                            //Looping through all objects in list to find the one that matches the input
                            for(Object o: list){
                                JSONObject object = (JSONObject) o;

                                if(object.get("username").equals(userID)){
                                    found = true;
                                    userEntered = true;
                                    user = userID;
                                    currDir = user;
                                    password = (String) object.get("password");
                                    usersAccounts = (JSONArray) object.get("accts");
                                    numAccounts = usersAccounts.size();

                                    //Account has no password
                                    if(object.get("password").equals("") && usersAccounts.size() == 0){
                                        res = String.format("!%s logged in", userID);
                                        loggedIn = true;
                                    }
                                    else{
                                        res ="+User-id valid, send account and password";
                                    }
                                    break;
                                }
                            }

                            if(!found){
                                System.out.println("Id received: "+ userID);
                                System.out.println(userID.length());
                                res = "-Invalid user-id, try again";
                            }
                        } catch (ParseException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //Looping through db.json to find the user

                }
            }

            else if(cmd.startsWith("ACCT")){
                if(str.length() > 4){
                    String accountName =  str.substring(5);

                    if(!isValidInput(accountName)){
                        res = "ERROR: Invalid Arguments\n" +
                                "Usage: ACCT account";
                    }
                    else{
                        if(usersAccounts != null){
                            boolean found = false;
                            for(Object o: usersAccounts){
                                String acctName= o.toString();
                                if(acctName.equals(accountName)){
                                    found = true;
                                    selectedAccount = acctName;
                                    res = passEntered ? "! Account valid, logged-in" : "+Account valid, send password";
                                    assert password != null;

                                    if(password.equals("")){
                                        loggedIn = true;
                                        res = "!Account valid, logged-in";

                                        if(pendingDirChange){
                                            res += String.format("\nChanged working dir to %s", currDir);
                                            pendingDirChange = false;
                                        }
                                    }
                                    break;
                                }
                            }

                            if(!found){
                                System.out.println("Account is invalid");
                                res = "-Invalid account, try again";
                            }

                        }
                        //No user has been specified
                        else{
                            //Move other code into here, would need to modify is slightly since some it gets
                            // taken care in the previous part

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
                                        res = "+This message";

                                        break;
                                    }
                                }

                                if(!found){
                                    res = "-Invalid account, try again";
                                }

                            } catch (ParseException | IOException e) {
                                throw new RuntimeException(e);
                            }


                        }
                    }

                }
            }
            else if (cmd.startsWith("PASS")){

                if(str.length() > 4){

                    String pass =  str.substring(5);
                    if(!isValidInput(pass)){
                        res = "ERROR: Invalid Arguments\n" +
                                "Usage: PASS password";
                    }
                    else if(pass.equals(password)){

                        passEntered = true;
                        if(numAccounts != 0 && selectedAccount == null){
                            res = "+Send account";
                        }
                        else{
                            loggedIn = true;
                            res = "! Logged in";
                            if(pendingDirChange){
                                res += String.format("\nChanged working dir to %s", currDir);
                                pendingDirChange = false;
                            }
                        }

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
                                    passEntered = true;
                                    password = (String) object.get("password");
                                    res = "+Send Account";
                                    break;
                                }
                            }

                            if(!found){
                                res = "-Invalid user-id, try again";
                            }
                        } catch (ParseException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        //Password is wrong
                        res = "-Wrong password, try again";
                    }

                }
            }

            else if (cmd.startsWith("LIST")){

                if(str.length() > 4){

                    String mode = str.substring(5);
                    if(mode.split(" ").length > 2){
                        res = "ERROR: Invalid Arguments\n" +
                                "Usage: LIST { F | V } directory-path";
                    }
                    else{
                        String specifiedDir = "";
                        //Creating directory string
                        specifiedDir += currDir + ((mode.length()>1) ? String.format("/%s",mode.substring(2)) :"");
                        String dirPath = String.format("%s%s", BASE_DIR, specifiedDir);
                        try{
                            if(Files.exists(Path.of(dirPath))){
                                res = String.format("+%s/\n", specifiedDir);
                                File[] files = new File(dirPath).listFiles();
                                StringBuilder resBuilder = new StringBuilder(res);

                                //Formatted directory listing
                                if(mode.startsWith("f")){
                                    assert files != null;
                                    for(File file: files){
                                        resBuilder.append(file.getName()).append("\n");
                                    }
                                    resBuilder.deleteCharAt(resBuilder.length()-1);
                                }
                                //Verbose directory listing
                                else if(mode.startsWith("v")){
                                    assert files != null;
                                    for(File file: files){
                                        String fileName = file.getName();
                                        Long fileSize = isAFolder(fileName) ? getFolderSize(dirPath+"/"+fileName) : file.length();
                                        resBuilder.append(String.format("Name: %s Path: %s/%s Size: %d\n", fileName,currDir,fileName, fileSize));
                                    }
                                    resBuilder.deleteCharAt(resBuilder.length()-1);
                                }
                                else{
                                    resBuilder.append("-Argument error");
                                }
                                res = resBuilder.toString();
                            }
                            else{
                                res = String.format("-Cant list directory because: %s does not exist", currDir);
                            }
                        }catch(Exception e){
                            res = String.format("-Cant list directory because: %s is not a directory", currDir);
                        }
                    }

                }

            }

            else if (cmd.startsWith("CDIR")){
                if(str.length()>4){

                    //Creating string for directory to change to
                    String dir = str.substring(5);
                    String chkDir = currDir;
                    chkDir +=  (dir.startsWith("/") || currDir.endsWith("/") || dir.equals("/"))? dir : ("/"+dir);

                    if(isAFolder(dir)){
                        if(Files.exists(Path.of(BASE_DIR+chkDir))){
                            currDir = chkDir;
                            res = (loggedIn) ? String.format("!Changed working dir to %s", currDir) :
                                                "+Directory exists, send account/password";
                            pendingDirChange = true;
                        }
                        else{
                            res = String.format("-Cant connect to directory because: %s does not exist", chkDir);
                        }
                    }
                    else{
                        res = String.format("-Cant list directory because: %s is not a directory", chkDir);
                    }

                }
            }

            else if(cmd.startsWith("DONE")){

                if(isValidInput(str)){
                    res = "+Closing connection";
                    sendMessageToClient(res, out);
                    return;
                }
                else{
                    res = "ERROR: Invalid Arguments\n" +
                            "Usage: DONE";
                }

            }
            else if(cmd.startsWith("TYPE")){

                if(str.length()>4){
                    String type = str.substring(5);

                    switch(type){
                        case "a":
                            res = "+Using Ascii mode";
                            break;

                        case "b":
                            res = "+Using Binary mode";
                            break;

                        case "c":
                            res = "+Using Continuous mode";
                            break;
                        default:
                            if(isValidInput(type)){
                                res = "-Type not valid";

                            }else{
                                res = "ERROR: Invalid Arguments\n" +
                                        "Usage: TYPE { A | B | C }";
                            }

                    }
                }
            }
            else{
                res = "ERROR: Invalid Command\n" +
                        "Available Commands: \"USER\", \"ACCT\", \"PASS\", \"TYPE\", \"LIST\", \"CDIR\", \"KILL\", \"NAME\", \"TOBE\", \"DONE\", \"RETR\", \"SEND\", \"STOP\", \"STOR\", \"SIZE\"";
            }

            sendMessageToClient(res, out);



        }


    }

    private Long getFolderSize(String path) {
        long size = 0L;
        File[] files = new File(path).listFiles();
        System.out.println("Path is "+path);
        if (files != null) {
            System.out.println("There are files");
            for(File f: files){
                size+=f.length();
            }
        }
        System.out.println("Value of size: "+ size);
        return size;
    }

    /**
     * Send a message to the client
     * @param res Response to be sent to the client
     * @param out {@link DataOutputStream} object to send response through
     */
    public static void sendMessageToClient(String res, DataOutputStream out){
        try{
            int resLength = res.length();
            out.writeInt(resLength);
            out.writeChars(res);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Check that the input is valid for a command by checking two parameters were not provided.
     *
     * @param input {@link String} object to check
     * @return {@link Boolean} if the input is valid or not
     */
    public static boolean isValidInput(String input){
        return !(input.split(" ").length > 1);
    }

    public static boolean isAFolder(String fileName){
        return !fileName.contains(".");
    }

}
