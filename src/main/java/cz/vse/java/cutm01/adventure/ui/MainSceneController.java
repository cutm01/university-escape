package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import cz.vse.java.cutm01.adventure.main.Start;
import cz.vse.java.cutm01.adventure.main.SystemInfo;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.InputStream;
import java.util.*;

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
    public BorderPane rootBorderPane;
    public Label actualGameRoomName;
    public Label actualGameRoomDescription;
    public ListView<InventoryItem> inventoryItems;
    //text shown to user after his interaction with game (i.e. clicking on button)
    public Text gameInteractionOutput;

    public Menu newGameMenu;
    public Menu showHelpMenu;


    public void init(Game game) {
        this.game = game;

        makeMenuElementFireAction(newGameMenu);
        //makeMenuElementFireAction(showHelpMenu);
        update();
    }

    private void addTestItemsToInventory(){
        inventoryItems.getItems().add(new InventoryItem("BOTTLE"));
        inventoryItems.getItems().add(new InventoryItem("PEN"));
        inventoryItems.setCellFactory(CheckBoxListCell.forListView(InventoryItem::checkedProperty));
    }

    private void update() {
        actualGameRoomName.setText("Aktuálna herná miestnosť");
        actualGameRoomDescription.setText("Popis miestnosti");
        updateGameInteractionOutput(game.getPrologue());
        addTestItemsToInventory();
    }

    /**
     * Method updates gameInteractionOutput Text. This text are is used to inform user about
     * performed changes in game after he executes one of game command
     * @param newValue value to set
     */
    private void updateGameInteractionOutput(String newValue) {
        gameInteractionOutput.setText(newValue);
        //gameInteractionOutput.("");
    }

    /**
     * Method makes JavaFX Menu element able to fire action (default behaviour
     * is that only MenuItem inside Menu element can fire action on click).
     * Method ensures that first MenuItem action is triggered after user clicks on its parent Menu element,
     * this action is triggered without showing MenuItem's graphic to user and therefore parent Menu element
     * looks like it can fire action after clicking on it
     *
     * @param menu Menu element with one 'dummy' MenuItem in it which will be used to fire action
     */
    private void makeMenuElementFireAction(Menu menu) {
        if (menu.getItems().size() == 1) {
            menu.showingProperty().addListener(
                    (observableValue, oldValue, newValue) -> {
                        if (newValue) {
                            // the first MenuItem is triggered
                            menu.getItems().get(0).fire();
                        }
                    }
            );
        }
    }

    private String getActualGameRoomName() {
        //return game.getGamePlan().getActualRoom().getName();
        return "";
    }

    private String getActualGameRoomDescription() {
        return "";
    }

    /**
     * Method shows confirmation dialog to user where he can choose
     * if he wants to start a new game or continue in the actual one
     */
    public void startNewGame() {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Nová hra");
        confirmationDialog.setHeaderText("Naozaj si praješ začať novú hru?");

        ButtonType confirmButtonType = new ButtonType("Áno, táto je už dávno stratená...");
        ButtonType cancelButtonType = new ButtonType("Nie, nevzdávam to!", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmationDialog.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

        //change default button
        Button confirmButton = (Button) confirmationDialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDefaultButton(false);
        Button cancelButton = (Button) confirmationDialog.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setDefaultButton(true);

        //wait for user input
        Optional<ButtonType> userInput = confirmationDialog.showAndWait();
        if (userInput.get() == confirmButtonType){
            Start.setUpNewGame(new Stage());
            Stage currentStage = (Stage) rootBorderPane.getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * Method shows user pop-up window with tips how to finish adventure
     */
    public void showHelp() {
        Alert helpWindow = new Alert(AlertType.INFORMATION);
        helpWindow.setTitle("Nápoveda");

        //disable header of pop-up alert and confirmation button
        helpWindow.setHeaderText(null);
        helpWindow.setGraphic(null);
        helpWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        String helpText = "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!" + SystemInfo.LINE_SEPARATOR
                            + "V miestnostiach môžeš nájsť rôzne predmety či objekty. Nezabudni ich poriadne prehľadať,"
                            + "môžu skrývať mnohé ďalšie užitočné predmety!" + SystemInfo.LINE_SEPARATOR
                            + "V niektorých miestnotiach nie si sám a sú to okrem tebe ďalšie osoby, skús sa s nimi porozprávať alebo v ich blízkosti využiť " + SystemInfo.LINE_SEPARATOR
                            + "nejaký zo svojich predmetov z batohu, možno sa ti odmenia!" + SystemInfo.LINE_SEPARATOR
                            + "Veľa štastia!";
        helpWindow.setContentText(helpText);

        helpWindow.show();
    }

    public void showItemDescription(ActionEvent actionEvent) {
        updateGameInteractionOutput("testing function" + SystemInfo.LINE_SEPARATOR
                                            + "testing function" + SystemInfo.LINE_SEPARATOR
                                            + "testing function" + SystemInfo.LINE_SEPARATOR
                                            + "testing function" + SystemInfo.LINE_SEPARATOR
                                            + "testing function" + SystemInfo.LINE_SEPARATOR);
    }

    public void showItemExaminationResult(ActionEvent actionEvent) {
    }

    public void useInventoryItem(ActionEvent actionEvent) {
    }

    public void dropInventoryItem(ActionEvent actionEvent) {
    }

    /**
     * Method to execute one of game commands. Command execution will perform all necessary changes in actual state
     * of game (e.g. insert item to inventory in case of TakeCommand execution)
     * @param commandName command to execute
     * @param commandParameters parameters (arguments) to execute command with
     * @return String output of command execution which is later displayed to user to notify him about performed changes
     * in game
     */
    private String executeGameCommand(String commandName, List<String> commandParameters) {
        //when user tries to execute HelpCommand (i.e. write "napoveda" to text field in the bottom of GUI)
        //game shows pop-up window containing help info rather than displaying command execution output in bottom of screen
        if (commandName.equals("napoveda")) {
            //TODO: implement to show new pop up window with help to user instead of updating GUI with command output
            return "text napovedy";
        }

        //get command execution output for commands with parameters (such as "vezmi predmet1 predmet2")
        if (commandParameters.size() > 0) {
            String parameters = String.join(" ", commandParameters);
            return game.parseUserInput(commandName + " " + parameters);
        }

        //get command execution output for commands without parameters
        return game.parseUserInput(commandName);
    }
}
