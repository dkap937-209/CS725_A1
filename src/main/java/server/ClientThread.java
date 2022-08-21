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
    private final String USER_FILES = "src/main/resources/client_files/";
    private static String user = null;
    private static String password = null;
    private boolean loggedIn = false;
    private  boolean pendingDirChange = false;
    private String pendingDirToChangeTo = "";
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
    private FileOutputStream fos = null;
    private File retrievedFile;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private boolean sendFile = false;
    private boolean appendToFile = false;
    private Path path;

    public ClientThread(Socket clientSocket) {
        System.out.println("New client thread made");
        this.socket = clientSocket;

        String delteFileData = "DELETE THIS FILE";
        String renameFileData = "RENAME THIS FILE";
        String file2txt = "THIS IS FILE 2";

        try {
            fos = new FileOutputStream("src/main/resources/server_files/user_files/user1/delete.txt");
            fos.write(delteFileData.getBytes());
            fos.flush();

            fos = new FileOutputStream("src/main/resources/server_files/user_files/user1/rename.txt");
            fos.write(renameFileData.getBytes());
            fos.flush();

            fos = new FileOutputStream("src/main/resources/server_files/user_files/user1/file2.txt");
            fos.write(file2txt.getBytes());
            fos.flush();

            File file = new File("src/main/resources/server_files/user_files/user1/fromClient.txt");
            file.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public synchronized void start(){
//    public void run(){
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }


        sendMessageToClient("Successfully connected to localhost on port 6789\n" +
                "+RFC 913 SFTP Server", out);


        while(true){

            //Send the file if 'send' was the previous command
            if(sendFile){
                try {
                    byte[] bytes = new byte[(int)retrievedFile.length()];
                    FileInputStream fis = new FileInputStream(retrievedFile);
                    BufferedInputStream bufferedInStream = new BufferedInputStream(new FileInputStream(retrievedFile));
                    int content = 0;

                    //read the file and write to buffer byte by byte
                    while ((content = bufferedInStream.read(bytes)) >= 0) {
                        out.write(bytes, 0, content);
                    }

                    out.flush();
                    sendFile = false;
                    continue;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //Reading the input sent from the client
            str = ReadChars.readStringIn(in);
            parser = new JSONParser();
            res = "";

            cmd = str.substring(0, 4);
            cmd = cmd.toUpperCase();

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

            else if(cmd.startsWith("RETR")){
                performRetrieveCommand();
            }

            else if(cmd.startsWith("SEND")){

                if(isValidInput(str)){
                    res = "+File sent";
                    sendFile = true;
                }
                else{
                    res = "ERROR: Invalid Arguments\n" +
                            "Usage: SEND";
                }

            }

            else if(cmd.startsWith("STOR")){
                performStoreCommand();
            }

            else if(cmd.startsWith("SIZE")){
                performSizeCommand();
            }

            else if(cmd.startsWith("STOP")){
                res = (isValidInput(str)) ? "+File will not be sent" : "ERROR: Invalid Arguments\n Usage: STOP";
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

                //Read the JSON file and try to find the username that the user is searching for
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

    /**
     * Handles selecting the account input
     */
    public void performAccountCommand(){
        if(str.length() > 4){
            String accountName =  str.substring(5);

            if(!isValidInput(accountName)){
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: ACCT account";
            }
            else{

                //If a username has been specified
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
                                    currDir = pendingDirToChangeTo;
                                    res += String.format("\nChanged working dir to %s", currDir);
                                    pendingDirChange = false;
                                }
                            }
                            break;
                        }
                    }

                    if(!found){
                        res = "-Invalid account, try again";
                    }

                }
                //No user has been specified
                else{
                    try(FileReader reader = new FileReader("src/main/resources/server_files/db.json")){
                        Object obj = parser.parse(reader);
                        JSONArray list = (JSONArray) obj;
                        JSONObject ids = (JSONObject) list.get(1);
                        list = (JSONArray) ids.get("accounts");
                        boolean found = false;

                        //Loop through list of all accounts in json file
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


    /**
     * Handle the password command input
     */
    public void performPasswordCommand(){
        if(str.length() > 4){

            String pass =  str.substring(5);
            if(!isValidInput(pass)){
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: PASS password";
            }
            else if(pass.equals(password)){

                //Password entered equals the password enetered for a previously specified username
                passEntered = true;
                if(numAccounts != 0 && selectedAccount == null){
                    res = "+Send account";
                }
                else{
                    loggedIn = true;
                    res = "! Logged in";
                    if(pendingDirChange){
                        currDir = pendingDirToChangeTo;
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

    /**
     * Handle the list command input
     */
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

    /**
     * Handle changing the directory command input
     */
    public void performCDIRCommand(){
        if(user == null){
            res = "-Please log in first";
        }
        else if(str.length()>4){

            //Creating string for directory to change to
            String dir = str.substring(5);

            if(isValidInput(dir)){

                if(dir.equals("/")){
                    currDir = user +"/";
                    res = String.format("!Changed working dir to %s", currDir);
                    return;
                }

                String chkDir = currDir;
                chkDir +=  (dir.startsWith("/") || currDir.endsWith("/") || dir.equals("/"))? dir : ("/"+dir);


                if(isAFolder(dir)){
                    if(Files.exists(Path.of(BASE_DIR+chkDir))){

                        if(loggedIn){
                            currDir = chkDir;
                            res =String.format("!Changed working dir to %s", currDir);
                        }
                        else{
                            pendingDirChange = true;
                            pendingDirToChangeTo = chkDir;
                            res =  "+Directory exists, send account/password";
                        }
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

    /**
     * Handles deleting an account
     */
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
                    fileToDelete.delete();
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

    /**
     * Handles renaming a file
     */
    public void performNameCommand(){
        if(!loggedIn){
            res = "-Please log in first";
        }
        else if(str.length()>4){
            String fileName = str.substring(5);

            if(isValidInput(fileName)){

                renamePath = String.format("%s/%s", currDir, fileName);
                filePath = String.format("%s%s", BASE_DIR, renamePath);
                res = (Files.exists(Path.of(filePath))) ? "+File exists" :
                                                           String.format("Can't find %s", renamePath);
            }
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: NAME old-file-spec";
            }

        }
    }

    /**
     * Handles the new name that a file shoudl be renamed to
     */
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

    /**
     * Handles the type of communication that should be use
     */
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
                    res = isValidInput(type) ? "-Type not valid" :
                                                "ERROR: Invalid Arguments\n" +
                                                "Usage: TYPE { A | B | C }";
            }
        }
    }

    /**
     * Handles the retrieve command and finding the file that the user wishes to retrieve from
     * the server
     */
    private void performRetrieveCommand() {

        if(str.length() > 4){

            String fileToRetrieve = str.substring(5);

            if(isValidInput(fileToRetrieve)){
                String retrievedFilePath = String.format("%s%s/%s", BASE_DIR, currDir, fileToRetrieve);

                if(isAFolder(fileToRetrieve)){
                    res = "-Specifier is not a file";
                }
                else if(Files.exists(Path.of(retrievedFilePath))){
                    retrievedFile = new File(retrievedFilePath);
                    res = String.format("+%d bytes will be sent", retrievedFile.length());
                }
                else{
                    res = "-File doesn't exist";
                }
            }
            else{
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: RETR file-spec";
            }
        }
    }

    /**
     * Handles retrieving and storing a file from the client side
     */
    private void performStoreCommand() {

        if(str.length()>4){

            try{
                //Get what type of GEN the file should be and check it is valid
                String gen = str.substring(5, 8).toUpperCase();
                String fileName = str.substring(9);

                if(!isValidInput(fileName) || !isValidInput(gen)){
                    res = "ERROR: Invalid Arguments\n" +
                            "Usage: STOR { NEW | OLD | APP } file-spec";
                }
                else if(isAFolder(fileName)){
                    res = "ERROR: Specifier is not a file";
                }
                else{

                    //Check if the user has the file they wish to store
                    String userFilePath = String.format("%s%s", USER_FILES, fileName);

                    if(Files.exists(Path.of(userFilePath))){

                        //Creating filepath string to store file on server side
                        filePath = String.format("%s/%s", currDir, fileName);
                        String fileDir = String.format("%s%s/%s", BASE_DIR, currDir, fileName);
                        path = Path.of(fileDir);

                        switch(gen){

                            case "NEW":
                                appendToFile = false;
                                if(Files.exists(path)){
                                    res = "+File exists, will create new generation of file";
                                    String folderDir = String.format("%s%s", BASE_DIR, currDir);
                                    boolean unique = false;

                                    //Get the file extension including .
                                    int len = fileName.length();
                                    StringBuilder fileExtensionBuilder = new StringBuilder();
                                    for(int i=len-1; i>len-5; i--){
                                        fileExtensionBuilder.append(fileName.charAt(i));
                                    }

                                    String fileExtension = fileExtensionBuilder.reverse().toString();
                                    String file = fileName.split("\\.")[0];

                                    if(!Character.isDigit(file.charAt(file.length()-1))){
                                        file += "1";
                                    }

                                    //Creating a unique filename
                                    while(!unique){
                                        String checkDir = String.format("%s/%s", folderDir, file+fileExtension);
                                        if(Files.exists(Path.of(checkDir))){
                                            //Increment file number
                                            String num = file.replaceAll("[^0-9]", "");
                                            int newFileNum = Integer.parseInt(num) + 1;
                                            StringBuilder builder = new StringBuilder(file);
                                            builder.deleteCharAt(builder.length()-1); //TODO: This shouldd be removing the numbers
                                            builder.append(newFileNum);
                                            file = builder.toString();
                                        }
                                        else{
                                            //Make new file
                                            filePath = checkDir;
                                            path = Path.of(filePath);
                                            File newFile = new File(checkDir);
                                            newFile.createNewFile();
                                            unique = true;
                                        }
                                    }
                                }
                                else{
                                    res = "+File does not exist, will create new file";
                                    path = Path.of(fileDir);
                                    createNewFile(fileDir, fos);
                                }

                                break;

                            case "OLD":
                                appendToFile = false;
                                if(Files.exists(path)){
                                    filePath = String.valueOf(path);
                                    res = "+Will write over old file";
                                }
                                else{
                                    res = "+Will create new file";
                                    createNewFile(fileDir, fos);
                                }

                                break;

                            case "APP":
                                if(Files.exists(path)){
                                    filePath = String.valueOf(path);
                                    res = "+Will append to file";
                                    appendToFile = true;
                                }
                                else{
                                    res = "+Will create new file";
                                    createNewFile(fileDir, fos);
                                }
                                break;

                            default:
                                res = "ERROR: Invalid Arguments\n" +
                                        "Usage: STOR { NEW | OLD | APP } file-spec";
                        }
                    }
                    else{
                        res = "ERROR: File doesn't exist";
                    }


                }

            }
            catch(IndexOutOfBoundsException | IOException e){
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: STOR { NEW | OLD | APP } file-spec";
            }
        }
    }

    /**
     * Handles the size command that works in tandem with the store command
     */
    private void performSizeCommand() {

        if(str.length()>4){
            String size = str.substring(5);

            if(!isValidInput(size)){
                res = "ERROR: Invalid Arguments\n" +
                        "Usage: SIZE number-of-bytes-in-file";
            }
            else{

                //Retrieve the file from the client side
                try{
                    fos = new FileOutputStream(String.valueOf(path), appendToFile);
                    BufferedOutputStream bufferedOutStream = new BufferedOutputStream(fos);
                    BufferedInputStream buffIn = new BufferedInputStream(in);

                    //Write the information to the new file on the server side
                    for (int i = 0; i < Integer.parseInt(size); i++) {
                        int r = buffIn.read();
                        bufferedOutStream.write(r);
                    }
                    bufferedOutStream.flush();

                    String stringPath = String.valueOf(path);
                    res = String.format("+Saved %s", stringPath.substring(BASE_DIR.length()));
                    appendToFile = false;
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void createNewFile(String fileDir, FileOutputStream fos){
        try {
            fos = new FileOutputStream(fileDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
