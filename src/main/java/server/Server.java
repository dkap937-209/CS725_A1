package server;

import Conn_Info.Connection_Information;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        while (true) {
            try (ServerSocket socket = new ServerSocket(Connection_Information.PORT_NUMBER)) {
                InetAddress serverHost = InetAddress.getLocalHost();
                System.out.println("Server destination: " + serverHost.getHostAddress() + ":" + socket.getLocalPort());

                try (Socket clientConnection = socket.accept()) {
                    new ClientThread(clientConnection).start();
                } catch (Exception ignored) {
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

