package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GameConstants;
import model.Location;
import model.Player;
import model.Shell;

import java.util.*;

public class Gui extends Application {

    public static final int size = 30;
    public static final int scene_height = size * 20 + 50;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;
    public static Image shell;


    private static Label[][] fields;
    private static GridPane boardGrid;
    private static TextArea scoreList;


    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    private static void drawMap() throws Exception {
        fields = new Label[20][20];
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                switch (GameConstants.board[j].charAt(i)) {
                    case 'w':
                        fields[i][j] = new Label("", new ImageView(image_wall));
                        break;
                    case ' ':
                        fields[i][j] = new Label("", new ImageView(image_floor));
                        break;
                    default:
                        throw new Exception("Illegal field value: " + GameConstants.board[j].charAt(i));
                }
                boardGrid.add(fields[i][j], i, j);
            }
        }
    }

    public static void updateGame(Map<String, Player> players, Map<Integer, Shell> shells) {
        updatePlayers(players);
        updateShells(shells);
        updateScoreTable();
    }

    public static void updatePlayers(Map<String, Player> players) {
        Map<String, Player> cachedPlayers = Cache.getPlayers();
        for (Player p : players.values()) {
            if (!cachedPlayers.containsKey(p.getName())) {
                // Add new player
                Cache.updatePlayer(p);
                placePlayerOnScreen(p.getLocation(), p.getDirection());
            }
        }
        for (Player cachedPlayer : cachedPlayers.values()) {
            Player player = players.get(cachedPlayer.getName());
            if (player == null) {
                // Remove player
                Cache.removePlayer(cachedPlayer.getName());
                removeGameObjectFromScreen(cachedPlayer.getLocation());
            } else if (!player.getLocation().equals(cachedPlayer.getLocation())) {
                // Move player
                Location newPlayerLocation = player.getLocation();
                String direction = player.getDirection();
                movePlayerOnScreen(cachedPlayer.getLocation(), newPlayerLocation, direction);
                Cache.updatePlayer(player);
            }
        }
    }

    public static void updateShells(Map<Integer, Shell> shells) {
        Map<Integer, Shell> cachedShells = Cache.getShells();
        for (Shell s : shells.values()) {
            if (!cachedShells.containsKey(s.getId())) {
                // Add new shell
                Cache.updateShell(s);
                placeShellOnScreen(s.getLocation());
            }
        }
        for (Shell cachedShell : cachedShells.values()) {
            Shell shell = shells.get(cachedShell.getId());
            if (shell == null) {
                // Remove shell
                Cache.removeShell(cachedShell.getId());
                removeGameObjectFromScreen(cachedShell.getLocation());
            } else if (!shell.getLocation().equals(cachedShell.getLocation())) {
                // Move shell
                Location newShellLocation = shell.getLocation();
                moveShellOnScreen(cachedShell.getLocation(), newShellLocation);
                Cache.updateShell(shell);
            }
        }
    }

    public static void removeGameObjectFromScreen(Location oldpos) {
        Platform.runLater(() -> {
            fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(image_floor));
        });
    }

    public static void placePlayerOnScreen(Location newpos, String direction) {
        Platform.runLater(() -> {
            int newx = newpos.getX();
            int newy = newpos.getY();
            if (direction.equals("right")) {
                fields[newx][newy].setGraphic(new ImageView(hero_right));
            }
            ;
            if (direction.equals("left")) {
                fields[newx][newy].setGraphic(new ImageView(hero_left));
            }
            ;
            if (direction.equals("up")) {
                fields[newx][newy].setGraphic(new ImageView(hero_up));
            }
            ;
            if (direction.equals("down")) {
                fields[newx][newy].setGraphic(new ImageView(hero_down));
            }
            ;
        });
    }

    public static void movePlayerOnScreen(Location oldpos, Location newpos, String direction) {
        removeGameObjectFromScreen(oldpos);
        placePlayerOnScreen(newpos, direction);
    }

    public static void placeShellOnScreen(Location newpos) {
        Platform.runLater(() -> {
            int newx = newpos.getX();
            int newy = newpos.getY();
            fields[newx][newy].setGraphic(new ImageView(shell));
        });
    }

    public static void moveShellOnScreen(Location oldpos, Location newpos) {
        removeGameObjectFromScreen(oldpos);
        placeShellOnScreen(newpos);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("War Machines - Firestorm");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            boardGrid = new GridPane();

            image_wall = new Image(getClass().getResourceAsStream("assets/wall-brick2.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("assets/wall-stone.png"), size, size, false, false);

            hero_right = new Image(getClass().getResourceAsStream("assets/tank1-right.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("assets/tank1-left.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("assets/tank1-up.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("assets/tank1-down.png"), size, size, false, false);

            shell = new Image(getClass().getResourceAsStream("assets/image/fireDown.png"), size, size, false, false);

            drawMap();
            scoreList.setEditable(false);


            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case UP:    CommunicationThread.writeToServer("move up");    break;
                    case DOWN:  CommunicationThread.writeToServer("move down");  break;
                    case LEFT:  CommunicationThread.writeToServer("move left");  break;
                    case RIGHT: CommunicationThread.writeToServer("move right"); break;
                    case SPACE: CommunicationThread.writeToServer("fire");      break;
                    case ESCAPE:System.exit(0);
                    default: break;
                }
            });

            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateScoreTable() {
        Platform.runLater(() -> {
            scoreList.setText(getScoreList());
        });
    }

    public static String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : Cache.getPlayers().values()) {
			b.append(p+"\r\n");
		}
		return b.toString();
    }
}

