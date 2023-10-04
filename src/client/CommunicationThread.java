package client;

import model.Location;
import model.Player;
import model.Shell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.DataTransferObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class CommunicationThread extends Thread {

    private static DataOutputStream outToServer;
    private final String ip;
    private String name;
    private static Socket clientSocket;

    public CommunicationThread(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public static void writeToServer(String command) {
        if (outToServer != null) {
            try {
                outToServer.writeBytes(command + "\n");
            } catch (SocketException e) {
                System.out.println("No connection to server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect(){
        if (outToServer != null) {
            try {
                outToServer.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Player> parsePlayers(String json) {
        Map<String, Player> players = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonPlayers = jsonObject.getJSONArray("players");

        for (int i = 0; i < jsonPlayers.length(); i++) {
            JSONObject jsonPlayer = jsonPlayers.getJSONObject(i);
            String name = jsonPlayer.getString("name");
            JSONObject location = jsonPlayer.getJSONObject("location");
            int x = location.getInt("x");
            int y = location.getInt("y");
            String direction = jsonPlayer.getString("direction");
            players.put(name, new Player(name, new Location(x, y), direction));
        }

        return players;
    }

    public static Map<Integer, Shell> parseShells(String json) {
        Map<Integer, Shell> shells = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonShells = jsonObject.getJSONArray("shells");

        for (int i = 0; i < jsonShells.length(); i++) {
            JSONObject jsonShell = jsonShells.getJSONObject(i);
            int id = jsonShell.getInt("id");
            JSONObject location = jsonShell.getJSONObject("location");
            int x = location.getInt("x");
            int y = location.getInt("y");
            String direction = jsonShell.getString("direction");
            shells.put(id, new Shell(id, new Location(x, y), direction));
        }

        return shells;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);

            clientSocket = new Socket(ip, 6789);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("login " + name + '\n');
            while (outToServer != null) {
                String updateFromServer = inFromServer.readLine();
                System.out.println(updateFromServer);
                Map<String, Player> players = parsePlayers(updateFromServer);
                Map<Integer, Shell> shells = parseShells(updateFromServer);
                Gui.updateGame(players, shells);
            }
        } catch (SocketException e) {
            System.out.println("Connection to server lost");
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
