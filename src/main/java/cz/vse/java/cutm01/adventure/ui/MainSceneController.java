package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.*;
import cz.vse.java.cutm01.adventure.main.Start;
import cz.vse.java.cutm01.adventure.main.SystemInfo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private SimpleObjectProperty actualGameRoomProperty;
    private MainSceneControllerUtils controllerUtils = new MainSceneControllerUtils();
    private final Map<String, Image> gameItemsImages = controllerUtils.loadGameItemsImages();
    private final Map<String, Image>  gameInteractableObjectsImages = controllerUtils.loadGameInteractableObjectsImages();
    private final Map<String, Image>  gameNonPlayerCharactersImages = controllerUtils.loadGameNonPlayerCharactersImages();
    private final Map<String, Image> gameRoomMapsImages = controllerUtils.loadGameRoomMapsImages();
    private final Map<String, Image> gameRoomsImages = controllerUtils.loadGameRoomsImages();

    //TextField to input game commands which player wants to execute
    public TextField gameConsole;

    //scene elements
    public BorderPane rootBorderPane;
    public ImageView actualRoomMiniMap;
    public Label actualGameRoomName;
    public Label actualGameRoomDescription;
    public ListView<String> inventoryItemsListView;
    public ListView<String> roomItemsListView;
    public ListView<String> roomInteractableObjectsListView;
    public ListView<String> roomNonPlayerCharactersListView;
    public ListView<String> roomExitsListView;
    public HBox roomExits;
    public Menu newGameMenu;
    public Menu helpMenu;
    public Menu textCommandsMenu;

    //text shown to user after his interaction with game (i.e. clicking on button)
    public Text gameInteractionOutput;

    //observable lists which are used to update GUI
    private ObservableList<String> itemsInActualRoom;
    private ObservableList<String> itemsInPlayerInventory;


    public void init(Game game) {
        this.game = game;
        makeMenuElementFireAction(newGameMenu);
        makeMenuElementFireAction(helpMenu);
        makeMenuElementFireAction(textCommandsMenu);

        itemsInActualRoom = game.getGamePlan().getActualRoom().getRoomItemsObservableList();
        itemsInActualRoom.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                updateRoomItemsListView();
                //roomItemsListView.refresh();
            }
        });

        itemsInPlayerInventory = game.getGamePlan().getPlayer().getInventory().getItemsInPlayerInventoryObservableList();
        itemsInPlayerInventory.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                updateInventoryItemsListView();
            }
        });

        initialGameSetUp();

        game.getGamePlan().actualRoomNameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                updateActualGameRoomNameLabel();
                updateActualGameRoomDescriptionLabel();
                updateActualRoomMiniMap();
                updateRoomExitsScrollPane();
                //updateRightPanel();

                itemsInActualRoom = game.getGamePlan().getActualRoom().getRoomItemsObservableList();
                itemsInActualRoom.addListener(new ListChangeListener<String>() {
                    @Override
                    public void onChanged(Change<? extends String> change) {
                        updateRoomItemsListView();
                        //roomItemsListView.refresh();
                    }
                });
            }
        });
    }

    /**
     * Method set up all necessary MainScene elements which are needed to start playing new game
     */
    private void initialGameSetUp() {
        updateActualGameRoomNameLabel();
        updateActualGameRoomDescriptionLabel();
        updateRoomExitsScrollPane();
        updateActualRoomMiniMap();
        updateGameInteractionOutput(game.getPrologueInGraphicalGameVersion());
    }

    private void updateActualRoomMiniMap() {
        String actualRoomName = getActualRoomName();
        String actualRoomNameAsEnum = RoomName.getEnumValueForRoomName(actualRoomName);
        //keys in gameRoomMapsImages are inserted in lowercase during loading of these images
        actualRoomMiniMap.setImage(gameRoomMapsImages.get(actualRoomNameAsEnum.toLowerCase()));
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

    /**
     * Method updates actualGameRoomName Label after player moves to another room
     */
    private void updateActualGameRoomNameLabel() {
        String actualRoomName = getActualRoomName();
        // following line get room name which is which is displayed in game GUI from room name used as argument for game commands, e.g.:
        // (room name to execute command) "toalety" --(Enum value)--> "TOILETS" --(room name displayed in GUI)--> "Toalety"
        String actualRoomNameToDisplay = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(actualRoomName));
        actualGameRoomName.setText(actualRoomNameToDisplay);
    }

    /**
     * Method updates actualGameRoomDescription Label after player moves to another room
     */
    private void updateActualGameRoomDescriptionLabel() {
        String actualRoomName = getActualRoomName();
        // following line get room description which is which is displayed in game GUI from room name used as argument for game commands, e.g.:
        // (room name to execute command) "toalety" --(Enum value)--> "TOILETS" --(room description displayed in GUI)--> "Trochu to tu zapácha, ale na to si si už za tie roky zvykol"
        String actualRoomNameDescriptionToDisplay = RoomDescriptionToDisplay.getRoomDescriptionToDisplay(RoomName.getEnumValueForRoomName(actualRoomName));
        actualGameRoomDescription.setText(actualRoomNameDescriptionToDisplay);
    }

    /**
     * Methods get actual room name
     * @return String representing actual room name in format defined in RoomName Enum
     */
    private String getActualRoomName() {
        return game.getGamePlan().getActualRoom().getName();
    }

    /**
     * Method returns boolean value based on whether actual game room was already examined or not
     * @return true if actual game room was already examined, false otherwise
     */
    private boolean wasActualRoomAlreadyExamined() {
        return game.getGamePlan().getActualRoom().wasRoomAlreadyExamined();
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

        helpWindow.showAndWait();
    }

    /**
     * Method shows user pop-up window with correct format of text game commands.
     * Player use these commands to interact with game instead of clicking on elements in GUI
     */
    public void showGameCommands() {
        Alert textCommandsWindow = new Alert(AlertType.INFORMATION);
        textCommandsWindow.setTitle("Textové príkazy");
        textCommandsWindow.getDialogPane().setPrefSize(910.0, 500.0);

        //disable header of pop-up alert and confirmation button
        textCommandsWindow.setHeaderText(null);
        textCommandsWindow.setGraphic(null);
        textCommandsWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        Label header = new Label("Počas hry môžeš zadávať nasledovné príkazy:" + SystemInfo.LINE_SEPARATOR);

        CommandsList gameTextCommandsList = ((GameImpl)game).getCommandsList();
        Label textCommands = new Label(gameTextCommandsList.getCommandsWithTheirUsage());

        VBox content = new VBox(10, header, textCommands);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        textCommandsWindow.getDialogPane().setContent(content);

        textCommandsWindow.showAndWait();
    }

    /**
     * Method shows big game map after user clicks on button
     * @param actionEvent
     */
    public void showGameMap(ActionEvent actionEvent) {
        Alert gameMapDialogWindow = new Alert(AlertType.INFORMATION);
        gameMapDialogWindow.setTitle("Herná mapa");
        gameMapDialogWindow.getDialogPane().setPrefSize(910.0, 500.0);

        //disable header of pop-up alert and confirmation button
        gameMapDialogWindow.setHeaderText(null);
        gameMapDialogWindow.setGraphic(null);
        gameMapDialogWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        //get actual game room name in format with diacritics (defined in RoomNameToDisplay)
        String actualGameRoomName = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(game.getGamePlan().getActualRoom().getName()));
        Label label = new Label("Aktuálne sa nachádzaš v miestnosti: " + actualGameRoomName);
        ImageView gameMapImage = new ImageView(gameRoomMapsImages.get("game_map"));
        VBox content = new VBox(10, label, gameMapImage);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        gameMapDialogWindow.getDialogPane().setContent(content);

        gameMapDialogWindow.showAndWait();
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

    public void showRoomItemExaminationResult(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromRoom = roomItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromRoom) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("preskumaj_predmet", gameCommandArguments));
    }

    /**
     * Method takes room item(s) selected by player and add them to his inventory
     * @param actionEvent
     */
    public void takeItemsFromRoom(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromRoom = roomItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromRoom) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("vezmi", gameCommandArguments));
    }

    public void showInventoryItemExaminationResult(ActionEvent actionEvent) {

    }

    public void useInventoryItem(ActionEvent actionEvent) {
    }

    public void dropInventoryItem(ActionEvent actionEvent) {
    }

    /**
     * Method takes user input from gameConsole TextField and execute this text input as game command
     * @param actionEvent
     */
    public void executeTypedInGameCommand(ActionEvent actionEvent) {
        String commandExecutionOutput = game.parseUserInput(gameConsole.getText());
        updateGameInteractionOutput(commandExecutionOutput);
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
     * Method update game's right panel (ListView with room items, interactable objects and NPCs) after player
     * moves to another room
     */
    private void updateRightPanel() {
        if (wasActualRoomAlreadyExamined()) {
            updateRoomItemsListView();
            updateRoomInteractableObjectsListView();
            updateRoomNonPlayerCharactersListView();
        }
        //clear right panel if actual game room was not examined yet
        else {
            roomItemsListView.setItems(null);
            roomInteractableObjectsListView.setItems(null);
            roomNonPlayerCharactersListView.setItems(null);
        }
    }

    /**
     * Method to update inventoryItemsListView with items which are currently placed in player's inventory.
     */
    private void updateInventoryItemsListView() {
        //Set<String> inventoryItemsNames = game.getGamePlan().getPlayer().getInventory().getInventoryItemsNames();
        //ObservableList<String> inventoryItems = FXCollections.observableArrayList();
/*
        for (String s : inventoryItemsNames) {
            // following line get item name which will be displayed in GUI from item name in format used for game command execution, e.g.:
            // (item name for game command execution) "flasa" --(Enum value)--> "BOTTLE" --(item name to display in GUI)--> "Fľaša s vodou"
            inventoryItems.add(ItemNameToDisplay.getItemNameToDisplay(ItemName.getEnumValueForItemName(s)));
        }
*/
        inventoryItemsListView.getItems().clear();
        inventoryItemsListView.getItems().addAll(itemsInPlayerInventory);
        //inventoryItemsListView.setItems(inventoryItems);
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

    /**
     * Method to update roomItemsListView with items which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomItemsListView() {
        //Set<String> roomItemsNames = game.getGamePlan().getActualRoom().getRoomItemsNames();
        //ObservableList<String> roomItems = game.getGamePlan().getActualRoom().getRoomItemsObservableList();
/*
        for (String s : roomItemsNames) {
            // following line get item name which will be displayed in GUI from item name in format used for game command execution, e.g.:
            // (item name for game command execution) "flasa" --(Enum value)--> "BOTTLE" --(item name to display in GUI)--> "Fľaša s vodou"
            roomItems.add(ItemNameToDisplay.getItemNameToDisplay(ItemName.getEnumValueForItemName(s)));
        }
*/
        roomItemsListView.getItems().clear();
        roomItemsListView.getItems().addAll(itemsInActualRoom);
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

    /**
     * Method to update roomInteractableObjectsListView with interactable objects which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomInteractableObjectsListView() {
        Set<String> roomInteractableObjectNames = game.getGamePlan().getActualRoom().getRoomInteractableObjectsNames();
        ObservableList<String> roomInteractableObjects = FXCollections.observableArrayList();

        for (String s : roomInteractableObjectNames) {
            // following line get interactable object name which will be displayed in GUI from interactable object name in format used for game command execution, e.g.:
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
                    displayImage.setImage(gameInteractableObjectsImages.get(itemNameToDisplay));
                    setText(itemNameToDisplay);
                    setGraphic(displayImage);
                }
            }
        });
    }

    /**
     * Method to update roomNonPlayerCharactersListView with NPCs which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomNonPlayerCharactersListView() {
        Set<String> roomNonPlayerCharacterNames = game.getGamePlan().getActualRoom().getRoomNonPlayerCharactersNames();
        ObservableList<String> roomNonPlayerCharacters = FXCollections.observableArrayList();

        for (String npc : roomNonPlayerCharacterNames) {
            // following line get non-player character name which will be displayed in GUI from non-player character object name in format used for game command execution, e.g.:
            // (non-player character name for game command execution) "upratovacka" --(Enum value)--> "CLEANING_LADY" --(item name to display in GUI)--> "Upratovačka"
            roomNonPlayerCharacters.add(NonPlayerCharacterNameToDisplay.getNonPlayerCharacterNameToDisplay(NonPlayerCharacterName.getEnumValueForNonPlayerCharacterName(npc)));
        }

        roomNonPlayerCharactersListView.setItems(roomNonPlayerCharacters);
        roomNonPlayerCharactersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        roomNonPlayerCharactersListView.setCellFactory(param -> new ListCell<>() {
            private ImageView displayImage = new ImageView();

            @Override
            public void updateItem(String itemNameToDisplay, boolean empty) {
                super.updateItem(itemNameToDisplay, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    displayImage.setImage(gameNonPlayerCharactersImages.get(itemNameToDisplay));
                    setText(itemNameToDisplay);
                    setGraphic(displayImage);
                }
            }
        });
    }

    /**
     * Method to update ScrollPane with all possible exits from actual game room
     */
    private void updateRoomExitsScrollPane(){
        List<VBox> content = new LinkedList();
        Set<String> roomExitsNames = game.getGamePlan().getActualRoom().getNeighboringRoomsNames();
        String roomNameToDisplay;
        //TODO: move to separate CSS file later
        String cssLayout = "-fx-border-color: black;\n"
                            /*+ "-fx-border-insets: 5;\n"*/
                            + "-fx-border-width: 1;\n"
                            + "-fx-border-style: solid;\n"
                            + "";

        for(String s : roomExitsNames) {
            roomNameToDisplay = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(s));
            Label roomName = new Label(roomNameToDisplay);
            ImageView roomImage = new ImageView(gameRoomsImages.get(roomNameToDisplay));
            VBox roomExit = new VBox(10, roomName, roomImage);
            roomExit.setMinWidth(130.0);
            roomExit.setMaxHeight(130.0);
            roomExit.setPadding(new Insets(5.0));
            roomExit.setStyle(cssLayout);
            roomExit.setAlignment(Pos.CENTER);
            roomExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    updateGameInteractionOutput(executeGameCommand("chod", new ArrayList<>(){{ add(s); }}));
                }
            });

            content.add(roomExit);
        }

        roomExits.getChildren().clear();
        roomExits.getChildren().addAll(content);
        roomExits.setAlignment(Pos.CENTER);
    }

    /**
     * Method is used to center pop-up Alert elements in game window
     * @param alert element to center
     * @param width desired width of element to center
     * @param height desired height of element to center
     */
    private void centerAlert(Alert alert, double width, double height) {
        double applicationWindowXCoordinate = rootBorderPane.getScene().getWindow().getX();
        double applicationWindowYCoordinate = rootBorderPane.getScene().getWindow().getY();
        double applicationWindowWidth = rootBorderPane.getScene().getWindow().getWidth();
        double applicationWindowHeight = rootBorderPane.getScene().getWindow().getHeight();

        //get X and Y coordinates which ensures that alert window will be centered in current application window
        double alertXCoordinate = applicationWindowXCoordinate + ((applicationWindowWidth - width) / 2);
        double alertYCoordinate = applicationWindowYCoordinate + ((applicationWindowHeight - height) / 2);

       alert.setResizable(true);
       alert.getDialogPane().setPrefSize(width, height);

        alert.setX(alertXCoordinate);
        alert.setY(alertYCoordinate);
    }
}
