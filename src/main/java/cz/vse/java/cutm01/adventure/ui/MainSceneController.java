package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import cz.vse.java.cutm01.adventure.gamelogic.InteractableObjectName;
import cz.vse.java.cutm01.adventure.gamelogic.ItemName;
import cz.vse.java.cutm01.adventure.main.Start;
import cz.vse.java.cutm01.adventure.main.SystemInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private final Map<String, Image> gameItemsImages = loadGameItemsImages();
    private final Map<String, Image>  gameInteractableObjectsImages = loadInteractableObjectsImages();

    //scene elements
    public BorderPane rootBorderPane;
    public Label actualGameRoomName;
    public Label actualGameRoomDescription;
    public ListView<String> inventoryItemsListView;
    public ListView<String> roomItemsListView;
    public ListView<String> roomInteractableObjectsListView;
    public ListView<String> roomNonPlayerCharactersListView;

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
        ObservableList<String> items = FXCollections.observableArrayList("Pero", "Lano", "Bunda");

        inventoryItemsListView.setItems(items);
        inventoryItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        inventoryItemsListView.setCellFactory(param -> new ListCell<>() {
            private ImageView displayImage = new ImageView();

            @Override
            public void updateItem(String itemNameToDisplay, boolean empty) {
                super.updateItem(itemNameToDisplay, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    displayImage.setImage(gameItemsImages.get(itemNameToDisplay));
                    setText(itemNameToDisplay);
                    setGraphic(displayImage);
                }
            }
        });
    }

    private void update() {
        actualGameRoomName.setText(getActualGameRoomName());
        actualGameRoomDescription.setText(getActualGameRoomDescription());
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

    public void lookAroundRoom(ActionEvent actionEvent) {
        updateGameInteractionOutput(executeGameCommand("rozhliadnut_sa", null));
        if (game.getGamePlan().getActualRoom().wasRoomAlreadyExamined()) {
            updateRoomItemsListView();
            updateRoomInteractableObjectsListView();
            updateRoomNonPlayerCharactersListView();
        }
    }

    public void showInventoryItemDescription(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromInventory = inventoryItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromInventory) {
            // following line get item name which is used as argument for game commands from item name which is displayed in game GUI, e.g.:
            // (item name in GUI) "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("prehliadnut_si", gameCommandArguments));
    }

    public void showRoomItemDescription(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromRoom = roomItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromRoom) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("prehliadnut_si", gameCommandArguments));
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
        if (commandParameters != null && commandParameters.size() > 0) {
            String parameters = String.join(" ", commandParameters);
            return game.parseUserInput(commandName + " " + parameters);
        }

        //get command execution output for commands without parameters
        return game.parseUserInput(commandName);
    }

    /**
     * Method to load images for all game items
     * @return Map where key is name of game item and value is corresponding image
     */
    private Map<String, Image> loadGameItemsImages() {
        Map<String, Image> loadedImages = new HashMap<>();

        Object[] itemNames = ItemName.values();
        for(Object o : itemNames) {
            String itemName = o.toString().toLowerCase();

            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(itemName + ".png");
            Image itemImage = new Image(imageStream);

            loadedImages.put(ItemNameToDisplay.getItemNameToDisplay(o.toString()), itemImage);
        }

        return loadedImages;
    }

    /**
     * Method to load images for all game items
     * @return Map where key is name of game item and value is corresponding image
     */
    private Map<String, Image> loadInteractableObjectsImages() {

    }

    private void updateRoomItemsListView() {
        Set<String> roomItemsNames = game.getGamePlan().getActualRoom().getRoomItemsNames();
        ObservableList<String> roomItems = FXCollections.observableArrayList();

        for (String s : roomItemsNames) {
            // following line get item name which will be displayed in GUI from item name in format used for game command execution, e.g.:
            // (item name for game command execution) "flasa" --(Enum value)--> "BOTTLE" --(item name to display in GUI)--> "Fľaša s vodou"
            roomItems.add(ItemNameToDisplay.getItemNameToDisplay(ItemName.getEnumValueForItemName(s)));
        }

        roomItemsListView.setItems(roomItems);
        roomItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        roomItemsListView.setCellFactory(param -> new ListCell<>() {
            private ImageView displayImage = new ImageView();

            @Override
            public void updateItem(String itemNameToDisplay, boolean empty) {
                super.updateItem(itemNameToDisplay, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    displayImage.setImage(gameItemsImages.get(itemNameToDisplay));
                    setText(itemNameToDisplay);
                    setGraphic(displayImage);
                }
            }
        });
    }

    private void updateRoomInteractableObjectsListView() {
        Set<String> roomInteractableObjectNames = game.getGamePlan().getActualRoom().getRoomInteractableObjectsNames();
        ObservableList<String> roomInteractableObjects = FXCollections.observableArrayList();

        for (String s : roomInteractableObjectNames) {
            // following line get interactable objectc name which will be displayed in GUI from interactable object name in format used for game command execution, e.g.:
            // (interactable object name for game command execution) "lavicka" --(Enum value)--> "BENCH" --(item name to display in GUI)--> "Lavička"
            roomInteractableObjects.add(InteractableObjectNameToDisplay.getInteractableObjectNameToDisplay(InteractableObjectName.getEnumValueForInteractableObjectName(s)));
        }

        roomInteractableObjectsListView.setItems(roomInteractableObjects);
        roomInteractableObjectsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        roomInteractableObjectsListView.setCellFactory(param -> new ListCell<>() {
            private ImageView displayImage = new ImageView();

            @Override
            public void updateItem(String itemNameToDisplay, boolean empty) {
                super.updateItem(itemNameToDisplay, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    displayImage.setImage(gameItemsImages.get(itemNameToDisplay));
                    setText(itemNameToDisplay);
                    setGraphic(displayImage);
                }
            }
        });
    }

    private void updateRoomNonPlayerCharactersListView() {

    }

}
