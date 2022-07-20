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

            while(true){
                try(Socket clientConnection = socket.accept()){
                    DataInputStream in = new DataInputStream(clientConnection.getInputStream());
                    DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
