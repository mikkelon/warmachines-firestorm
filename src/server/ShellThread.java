package server;

import model.Shell;

import java.util.ArrayList;
import java.util.Iterator;


public class ShellThread extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                Game.moveShells();
                Thread.sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
