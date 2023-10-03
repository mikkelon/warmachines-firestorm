package server;

import model.Player;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

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
            System.out.println(login);
            if (login.startsWith("login ")) {
                String name = login.substring(6);
                Game.addThread(this, name);
                this.setName("Thread-" + player.getName());
                while(true) {
                    String command = inFromClient.readLine();
                    System.out.println(command);
                    if (command.startsWith("move ")) {
                        String direction = command.substring(5);
                        Game.movePlayer(player, direction);
                    } else if (command.equalsIgnoreCase("logout")) {
                        throw new SocketException(player.getName() + " logged out");
                    } else {
                        System.out.println("Unknown command: " + command);
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println(player.getName() + " disconnected");
            Game.removePlayer(player, outToClient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public DataOutputStream getOutputStream() {
        return outToClient;
    }
}