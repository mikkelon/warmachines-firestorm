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
                outToServer.writeBytes(command + "\n"); // TODO: command needs to have a name
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

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            clientSocket = new Socket("localhost", 6789);
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
//                    System.out.println("Ingen loggedOutPlayer");
                }
                Gui.updateGame(players, loggedOutPlayer);
            }
        } catch (SocketException e) {
            System.out.println("Connection to server lost");
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
