package template;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
	public static void main(String[] args) throws Exception{
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Indtast spillernavn");
		String navn = inFromUser.readLine();
		GameLogic.makePlayers(navn);
		Application.launch(Gui.class);
	}
}
;