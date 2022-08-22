package server;

import Conn_Info.Connection_Information;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        ServerSocket server = null;

        try {
            server = new ServerSocket(6789);
            server.setReuseAddress(true);


            while(true){
                Socket client = server.accept();
                System.out.println("New client connected: "
                        + client.getInetAddress()
                        .getHostAddress());

                ClientThread clientSock
                        = new ClientThread(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }

        }catch(IOException e){}

    }
}

