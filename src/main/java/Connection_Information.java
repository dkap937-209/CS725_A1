import java.net.InetAddress;
import java.net.UnknownHostException;

public class Connection_Information {
    public static final int PORT_NUMBER = 115;
    public static final InetAddress SERVER_ADDRESS;

    static {
        try {
            SERVER_ADDRESS = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
