package client;

import javafx.application.Application;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameClient {

    private static DataOutputStream outToServer;

    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indtast spillernavn");
        String navn = inFromUser.readLine();

        new Thread(() -> {
            try {
                Thread.sleep(5000);

            Socket clientSocket = new Socket("localhost", 6789);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("login " + navn + '\n');
            while (true) {
                String updateFromServer = inFromServer.readLine();
                System.out.println(updateFromServer);
                JSONObject json = new JSONObject(updateFromServer);
                JSONArray players = json.getJSONArray("players");
                System.out.println(players);
                Gui.updateGame(players);
            }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Application.launch(Gui.class);
    }

    public static void writeToServer(String command) {
        if (outToServer != null) {
            try {
                outToServer.writeBytes(command + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
