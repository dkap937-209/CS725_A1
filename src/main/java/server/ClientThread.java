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
    private  final String BASE_DIR = "src/main/resources/server_files/user_files/";
    private static String user = null;
    private static String password = null;
    private boolean loggedIn = false;
    private  boolean pendingDirChange = false;
    private  JSONArray usersAccounts = null;
    private  String selectedAccount = null;
    private  int numAccounts = 0;
    private  String currDir = "src/main/resources/server_files/user_files/";
    private  boolean userEntered = false;
    private  boolean passEntered = false;
    private  String renamePath = "";
    private  String filePath = "";

    private  String str;
    private  JSONParser parser;
    private  String res;

    private  String cmd;

    public ClientThread(Socket clientSocket) {
        System.out.println("New client thread made");
        this.socket = clientSocket;

        String delteFileData = "DELETE THIS FILE";
        String renameFileData = "RENAME THIS FILE";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("src/main/resources/server_files/user_files/user1/delete.txt");
            fos.write(delteFileData.getBytes());
            fos.flush();

            fos = new FileOutputStream("src/main/resources/server_files/user_files/user1/rename.txt");
            fos.write(delteFileData.getBytes());
            fos.flush();

            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

//        String user = null;
//        String password = null;
//        boolean loggedIn = false;
//        boolean pendingDirChange = false;
//        JSONArray usersAccounts = null;
//        String selectedAccount = null;
//        int numAccounts = 0;
//        String currDir = "src/main/resources/server_files/user_files/";
//        boolean userEntered = false;
//        boolean passEntered = false;
//        String renamePath = "";
//        String filePath = "";

        sendMessageToClient("Successfully connected to localhost on port 6789\n" +
                "+RFC 913 SFTP Server", out);


        while(true){
            //Reading the input sent from the client
            str = ReadChars.readStringIn(in);
            parser = new JSONParser();
            res = "";

            cmd = str.substring(0, 4);
            cmd = cmd.toUpperCase();

            /** User Command **/
            if(cmd.startsWith("USER")){
                performUserCommand();
            }

            else if(cmd.startsWith("ACCT")){
                performAccountCommand();
            }

            else if (cmd.startsWith("PASS")){
                performPasswordCommand();
            }

            else if (cmd.startsWith("LIST")){
                performListCommand();
            }

            else if (cmd.startsWith("CDIR")){
                performCDIRCommand();
            }

            else if(cmd.startsWith("KILL")){
                performKillCommand();
            }

            else if(cmd.startsWith("NAME")){
                performNameCommand();
            }

            else if(cmd.startsWith("TOBE")){
                performTOBECommand();
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
                performTypeCommand();
//                if(!loggedIn){
//                    res = "-Please log in first";
//                }
//                else if(str.length()>4){
//                    String type = str.substring(5);
//
//                    switch(type){
//                        case "a":
//                            res = "+Using Ascii mode";
//                            break;
//
//                        case "b":
//                            res = "+Using Binary mode";
//                            break;
//
//                        case "c":
//                            res = "+Using Continuous mode";
//                            break;
//                        default:
//                            if(isValidInput(type)){
//                                res = "-Type not valid";
//
//                            }else{
//                                res = "ERROR: Invalid Arguments\n" +
//                                        "Usage: TYPE { A | B | C }";
//                            }
//
//                    }
//                }
            }
            else{
                res = "ERROR: Invalid Command\n" +
                        "Available Commands: \"USER\", \"ACCT\", \"PASS\", \"TYPE\", \"LIST\", \"CDIR\", \"KILL\", \"NAME\", \"TOBE\", \"DONE\", \"RETR\", \"SEND\", \"STOP\", \"STOR\", \"SIZE\"";
            }

            sendMessageToClient(res, out);
        }
    }

    /**
     * Retrieve the cummulative size of the files within a folder
     *
     * @param path {@link String}Path of the folder to find the size of
     * @return Size of the folder
     */
    private static Long getFolderSize(String path) {
        long size = 0L;
        File[] files = new File(path).listFiles();
        if (files != null) {
            for(File f: files){
                size+=f.length();
            }
        }
        return size;
    }

    /**
     * Send a message to the client
     * @param res {@link String} Response to be sent to the client
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

    /**
     * Checks if a filename is for a file or folder
     *
     * @param fileName {@link String} object to check
     * @return {@link Boolean} object if the name is a folder or not
     */
    public static boolean isAFolder(String fileName){
        return !fileName.contains(".");
    }

    /**
     * Handles the user command input
     */
    public void performUserCommand(){
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
                        res = "-Invalid user-id, try again";
                    }
                } catch (ParseException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void performAccountCommand(){
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

    public void performPasswordCommand(){
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

    public void performListCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length() > 4){

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
                                resBuilder.append(String.format("Name: %s Path: %s/%s Size: %d Bytes\n", fileName,currDir,fileName, fileSize));
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

    public void performCDIRCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length()>4){

            //Creating string for directory to change to
            String dir = str.substring(5);

            if(isValidInput(dir)){
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
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: CDIR new-directory";
            }
        }
    }
    public void performKillCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length() > 4){

            String fileName = str.substring(5);

            if(isValidInput(fileName)){
                String relDelPath = String.format("%s/%s", currDir, fileName);
                String deleteDir = String.format("%s%s", BASE_DIR, relDelPath);
                File fileToDelete = new File(deleteDir);

                if(Files.exists(Path.of(deleteDir))){
                    fileToDelete.delete(); //TODO: not deleting the file for some reason
                    res = String.format("+%s deleted", relDelPath);
                }
                else{
                    res = String.format("-Not deleted because %s does not exist", relDelPath);
                }

//                res = (fileToDelete.delete()) ? String.format("+%s deleted", relDelPath):
//                        String.format("-Not deleted because %s does not exist", relDelPath);
            }
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: KILL file-spec";
            }
        }
    }

    public void performNameCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length()>4){
            String fileName = str.substring(5);

            if(isValidInput(fileName)){

                //Probably need to make them in global scope of function
                renamePath = String.format("%s/%s", currDir, fileName);
                filePath = String.format("%s%s", BASE_DIR, renamePath);

                if(Files.exists(Path.of(filePath))){
                    res = "+File exists";
                }
                else{
                    res = String.format("Can't find %s", renamePath);
                }


            }
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: NAME old-file-spec";
            }

        }
    }

    public void performTOBECommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length()>4){
            String newFileName = str.substring(5);

            if(isValidInput(newFileName)){
                File file = new File(filePath);
                String newFilePathName = String.format("%s/%s", currDir, newFileName);
                String newPath = String.format("%s%s", BASE_DIR, newFilePathName);

                if(Files.exists(Path.of(newPath))){
                    //A file with the same name already exist
                    res = String.format("File wasn't renamed because %s already exists", newFilePathName);
                }
                else if(file.renameTo(new File(newPath))){
                    res = String.format("%s renamed to %s", renamePath, newFilePathName);
                }
            }
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: TOBE new-file-spec";
            }


        }

    }

    public void performTypeCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length()>4){
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





}
