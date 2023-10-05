package client;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Welcome to WAR MACHINES - FIRESTORM!");
        System.out.println("Please enter your call sign below to join the battle:");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String navn = inFromUser.readLine();
        System.out.println("Connecting to server...");

        new CommunicationThread(navn, "10.10.139.145").start();
        Application.launch(Gui.class);

        CommunicationThread.disconnect();
    }
}
