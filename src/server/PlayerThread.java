package server;

import model.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class PlayerThread extends Thread {
    private final Socket connenctionSocket;
    private final BufferedReader inFromClient;
    private final DataOutputStream outToClient;
    private final int MOVE_DELAY = 100;
    private final int FIRE_DELAY = 500;
    private Player player;
    private Date lastMove = new Date();
    private Date lastFire = new Date();

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
                while (true) {
                    String command = inFromClient.readLine();
                    System.out.println(command);
                    if (command != null) {
                        if (command.startsWith("move ") && checkDelay(lastMove, MOVE_DELAY)) {
                            String direction = command.substring(5);
                            Game.movePlayer(player, direction);
                            lastMove = new Date();
                        } else if (command.equals("fire") && checkDelay(lastFire, FIRE_DELAY)) {
                            System.out.println("SHOTS FIRED");
                            Game.fire(player);
                            lastFire = new Date();
                        } else {
                            System.out.println("Unknown command " + command + " from " + player.getName());
                        }
                    } else {
                        System.out.println("Command is null, probably lost connection to " + player.getName());
                        throw new SocketException("Lost connection to " + player.getName());
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

        public void setPlayer (Player player){
            this.player = player;
        }

        public DataOutputStream getOutputStream () {
            return outToClient;
        }

        private boolean checkDelay (Date lastCommand,int delay){
            return new Date().getTime() - lastCommand.getTime() > delay;
        }
    }