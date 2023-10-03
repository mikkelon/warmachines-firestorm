package server;

import model.Shell;

public class ShellThread extends Thread {
    private Shell shell;

    public ShellThread(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Game.moveShell(shell);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
