package cz.vse.java.cutm01.adventure.main;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import cz.vse.java.cutm01.adventure.gamelogic.GameImpl;
import cz.vse.java.cutm01.adventure.ui.GameTextInterface;
import cz.vse.java.cutm01.adventure.ui.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class Start is the main class of whole adventure project.<br>
 * It is used to start adventure in either text or graphical user interface
 *
 * @author Michal Cúth
 * @version 1.0.0
 */
public class Start extends Application {

    /**
     * Method to start new game according to command line arguments passed
     *
     * @param args -text to start game with text user interface, -gui for graphical user interface
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Vyber si užívateľské rozhranie:" + SystemInfo.LINE_SEPARATOR
                               + "Spusti hru opätovne s argumentom -text pre textové užívateľské rozhranie" + SystemInfo.LINE_SEPARATOR
                               + "alebo argumenntom -gui pre grafické užívateľské rozhranie");
            System.exit(0);
        }

        if (args.length >= 2) {
            System.out.println("Vyber si jedno z nasledujúcich užívateľských rozhraní:" + SystemInfo.LINE_SEPARATOR
                               + "Spusti hru opätovne s argumentom -text pre textové užívateľské rozhranie" + SystemInfo.LINE_SEPARATOR
                               + "alebo argumenntom -gui pre grafické užívateľské rozhranie");
            System.exit(0);
        }

        //check which type of user interface user choosed
        if (args[0].equals("-text")) {
            Game game = new GameImpl();
            GameTextInterface ui = new GameTextInterface(game);
            ui.play();
        } else if (args[0].equals("-gui")) {
            launch();
        } else {
            System.out.println("Argument " + args[0] + " nie je platný!"
                               + "Vyber si jedno z nasledujúcich užívateľských rozhraní:" + SystemInfo.LINE_SEPARATOR
                               + "Spusti hru opätovne s argumentom -text pre textové užívateľské rozhranie" + SystemInfo.LINE_SEPARATOR
                               + "alebo argumenntom -gui pre grafické užívateľské rozhranie");
            System.exit(0);
        }
    }

    /**
     * Method to start new JavaFX application if user choosed to start new game with graphical user interface<br>
     * It loads main scene and init new game
     * @param primaryStage top level JavaFX container containing additional application objects
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //get main scene resources
        FXMLLoader fxmlLoader = new FXMLLoader();
        InputStream mainSceneInputStream = getClass().getClassLoader().getResourceAsStream("main_scene.fxml");
        Parent rootSceneElement = fxmlLoader.load(mainSceneInputStream);
        try {
            rootSceneElement = fxmlLoader.load(mainSceneInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //build main scene
        Scene mainScene = new Scene(rootSceneElement);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Útek z VŠE");
        primaryStage.setFullScreen(true);

        //init FXML loader
        MainSceneController mainSceneController = fxmlLoader.getController();
        Game newGame = new GameImpl();
        mainSceneController.init(newGame);

        primaryStage.show();
        /*
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label =
            new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
         */
    }
}
