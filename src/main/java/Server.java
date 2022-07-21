import util.ReadChars;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

//        DataInputStream in;
//        DataOutputStream out;

        try (ServerSocket socket = new ServerSocket(Connection_Information.PORT_NUMBER)) {
            InetAddress serverHost = InetAddress.getLocalHost();
            System.out.println("Server destination: " + serverHost.getHostAddress() + ":" + socket.getLocalPort());

            try(Socket clientConnection = socket.accept()){

                DataInputStream in = new DataInputStream(clientConnection.getInputStream());
                DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
                out.writeChars("+localhost -XX SFTP Service");
                new StringBuilder();
                StringBuilder builder;
                int inputLength;

                while(true){
                    //Reading the input sent from the client
                    String str = ReadChars.readStringIn(in);

                    if(str.startsWith("USER")){
                        System.out.println("Perform User command");
                    }
                    else if(str.startsWith("ACCT")){
                        System.out.println("Perform account command");
                    }
                    else if (str.startsWith("PASS")){
                        System.out.println("Perform password command");
                    }


                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

    }
}
