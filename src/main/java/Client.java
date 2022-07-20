import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args){

        try(Socket socket = new Socket(Connection_Information.SERVER_ADDRESS, Connection_Information.PORT_NUMBER)){
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
