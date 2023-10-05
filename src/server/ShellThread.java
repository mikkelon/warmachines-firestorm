package server;

import model.Shell;

import java.util.ArrayList;
import java.util.Iterator;


public class ShellThread extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                ArrayList<Shell> shells= new ArrayList<>(Game.getShells());
                for(Shell shell : shells){
                    Game.moveShell(shell);
                }
                Thread.sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
