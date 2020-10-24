/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package cz.vse.java.cutm01.adventure.main;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import cz.vse.java.cutm01.adventure.gamelogic.GameImpl;
import cz.vse.java.cutm01.adventure.ui.GameTextInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// TODO: translate and change author

/*******************************************************************************
 * Třída  Start je hlavní třídou projektu,
 * který představuje jednoduchou textovou adventuru určenou k dalším úpravám a rozšiřování
 *
 * @author Jarmila Pavlíčková
 * @version ZS 2016/2017
 */
public class Start extends Application {

    /***************************************************************************
     * Metoda, prostřednictvím níž se spouští celá aplikace.
     *
     * @param args Parametry příkazového řádku
     */
    public static void main(String[] args) {
        // TODO: add check if user pass multiple command-line arguments
        if (args[0].equals("-text")) {
            Game game = new GameImpl();
            GameTextInterface ui = new GameTextInterface(game);
            ui.play();
        } else if (args[0].equals("-gui")) {
            launch();
        }
    }

    // TODO: add comment/java doc to following method
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label =
            new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
