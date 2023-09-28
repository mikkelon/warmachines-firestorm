package server;

import model.Player;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PlayerThread extends Thread {
    private final Socket connenctionSocket;
    private final BufferedReader inFromClient;
    private final DataOutputStream outToClient;
    private Player player;


    public PlayerThread(Socket connectionSocket) throws IOException {
        this.connenctionSocket = connectionSocket;
        inFromClient = new BufferedReader(new InputStreamReader(connenctionSocket.getInputStream()));
        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String login = inFromClient.readLine();
            if (login.startsWith("login ")) {
                player = Game.addThread(this, login.substring(6));
                while(true) {

                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}