package client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class CommunicationThread extends Thread {

    private static DataOutputStream outToServer;
    private String name;

    public CommunicationThread(String name) {
        this.name = name;
    }

    public static void writeToServer(String command) {
        if (outToServer != null) {
            try {
                outToServer.writeBytes(command + "\n"); // TODO: command needs to have a name
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void disconnect() {
        if (outToServer != null) {
            writeToServer("logout");
            outToServer = null;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            Socket clientSocket = new Socket("localhost", 6789);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("login " + name + '\n');
            while (outToServer != null) {
                String updateFromServer = inFromServer.readLine();
                System.out.println(updateFromServer);
                JSONObject json = new JSONObject(updateFromServer);
                JSONArray players = json.getJSONArray("players");
                JSONObject loggedOutPlayer = null;
                try {
                    loggedOutPlayer = json.getJSONObject("loggedOutPlayer");
                } catch (JSONException e) {
                    System.out.println("Ingen loggedOutPlayer");
                }
                System.out.println(loggedOutPlayer);
                Gui.updateGame(players, loggedOutPlayer);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            System.out.println("Connection to server lost");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
