package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import javafx.scene.control.Label;

/**
 * MainSceneController class contains methods to handle changes made in main scene
 * caused by user interaction with graphical user interface
 *
 * @author Michal Cúth
 * @version 1.0.0
 */
public class MainSceneController {
    private Game game;

    //scene elements
    public Label actualGameRoomName;
    public Label actualGameRoomDescription;

    public void init(Game game) {
        this.game = game;
        update();
    }

    private void update() {
        actualGameRoomName.setText("Actual Game Rooom");
        actualGameRoomDescription.setText("Random Description");
    }

    private String getActualGameRoomName() {
        //return game.getGamePlan().getActualRoom().getName();
        return "";
    }

    private String getActualGameRoomDescription() {
        return "";
    }
}
