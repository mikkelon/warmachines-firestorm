package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server listening on port " + welcomeSocket.getLocalPort() + "...");
        (new ShellThread()).start();
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("New connection " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort());
            (new PlayerThread(connectionSocket)).start();

        }
    }
}
