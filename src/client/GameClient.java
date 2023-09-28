package client;

import javafx.application.Application;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indtast spillernavn");
        String navn = inFromUser.readLine();
        new Thread(() -> {
            try {
                Application.launch(Gui.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(2000);
        Socket clientSocket = new Socket("localhost", 6789);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("login " + navn + '\n');
        while(true){
            String updateFromServer = inFromServer.readLine();
            JSONObject json = new JSONObject(updateFromServer);

        }

    }
}
