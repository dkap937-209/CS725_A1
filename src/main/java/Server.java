import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(Connection_Information.PORT_NUMBER)) {
            InetAddress serverHost = InetAddress.getLocalHost();
            System.out.println("Server destination: " + serverHost.getHostAddress() + ":" + socket.getLocalPort());

            try(Socket clientConnection = socket.accept()){
                DataInputStream in = new DataInputStream(clientConnection.getInputStream());
                DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
                new StringBuilder();
                StringBuilder builder;
                int inputLength;

                while(true){
                    //Reading the input sent from the client
                    builder = new StringBuilder();
                    inputLength = in.readInt();
                    for(int i=0; i<inputLength; i++){
                        builder.append(in.readChar());
                    }
                    String str = builder.toString();
//                    System.out.println("String received: "+ str);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

    }
}
