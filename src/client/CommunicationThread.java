package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommunicationThread extends Thread {

    private static DataOutputStream outToServer;
    private final String ip;
    private String name;

    public CommunicationThread(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            Socket clientSocket = new Socket(ip, 6789);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("login " + name + '\n');
            while (true) {
                String updateFromServer = inFromServer.readLine();
                System.out.println(updateFromServer);
                JSONObject json = new JSONObject(updateFromServer);
                JSONArray players = json.getJSONArray("players");
                Gui.updateGame(players);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
