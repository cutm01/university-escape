package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.*;
import cz.vse.java.cutm01.adventure.main.Start;
import cz.vse.java.cutm01.adventure.main.SystemInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MainSceneController class contains methods to handle changes made in main scene
 * caused by user interaction with graphical user interface
 *
 * @author Michal Cúth
 * @version 1.0.0
 */
public class MainSceneController {
    // region Controller variables
    // --------------------------------------------------------------------------------
    private Game game;
    private StringProperty actualGameRoomProperty;
    private StringProperty playerActuallyStandsByProperty;
    private BooleanProperty playerFinishedGame;
    private int inventoryWeightCapacity;
    private MainSceneControllerUtils controllerUtils = new MainSceneControllerUtils();
    private final Map<String, Image> gameItemsImages = controllerUtils.loadGameItemsImages();
    private final Map<String, Image>  gameInteractableObjectsImages = controllerUtils.loadGameInteractableObjectsImages();
    private final Map<String, Image>  gameNonPlayerCharactersImages = controllerUtils.loadGameNonPlayerCharactersImages();
    private final Map<String, Image> gameRoomMapsImages = controllerUtils.loadGameRoomMapsImages();
    private final Map<String, Image> gameRoomsImages = controllerUtils.loadGameRoomsImages();
    //observable lists which are used to update GUI
    private ObservableList<String> itemsInActualRoom;
    private ObservableList<String> itemsInPlayerInventory;
    // --------------------------------------------------------------------------------
    // endregion Controller variables

    // region Scene elements
    // --------------------------------------------------------------------------------
    // text field to input game commands which player wants to execute
    public TextField gameConsole;

    //scene elements
    public BorderPane rootBorderPane;
    public ImageView actualRoomMiniMap;
    public Label actualGameRoomName;
    public Label actualGameRoomDescription;
    public Label playerActuallyStandsBy;
    public Label inventoryCapacity;
    public ListView<String> inventoryItemsListView;
    public ListView<String> roomItemsListView;
    public ListView<String> roomInteractableObjectsListView;
    public ListView<String> roomNonPlayerCharactersListView;
    public ListView<String> roomExitsListView;
    public HBox roomExits;
    public Menu newGameMenu;
    public Menu helpMenu;
    public Menu textCommandsMenu;

    // text shown to user after his interaction with game (i.e. clicking on button)
    public Text gameInteractionOutput;
    // --------------------------------------------------------------------------------
    // endregion Scene elements

    // region Game initialization
    // --------------------------------------------------------------------------------
    /**
     *  Method init main scene to start new game session
     * @param game Game instanc containing all data about current game session
     */
    public void init(Game game) {
        this.game = game;
        initializeChangeListeners(); //used to handle changes in game GUI during playing the game
        initialGameSetUp();

        //JavaFX Menu element does not contain MenuItem elements and fire action on its own
        makeMenuElementFireAction(newGameMenu);
        makeMenuElementFireAction(helpMenu);
        makeMenuElementFireAction(textCommandsMenu);
    }

    /**
     * Method set up all necessary MainScene elements which are needed to start playing new game
     */
    private void initialGameSetUp() {
        inventoryWeightCapacity = game.getGamePlan().getPlayer().getInventory().getInventoryCapacity();
        updateInventoryCapacityLabel();

        updateBackground();
        updateActualGameRoomNameLabel();
        updateActualGameRoomDescriptionLabel();
        updatePlayerActuallyStandsByLabel();
        updateRoomExitsScrollPane();
        updateActualRoomMiniMap();
        updateRightPanel();
        updateInventoryItemsListView();
        updateGameInteractionOutput(game.getPrologueInGraphicalGameVersion());
    }

    /**
     * Method initialize all necessary change listeners which are used to handle changes in game's GUI
     */
    private void initializeChangeListeners() {
        //listener for showing game ending after player finishes the game
        playerFinishedGame = game.getGamePlan().playerFinishedGame();
        playerFinishedGame.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                showGameEnding();
            }
        });

        //listener for updating GUI when player moves to another room
        actualGameRoomProperty = game.getGamePlan().actualRoomNameProperty();
        actualGameRoomProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //get ObservableList with room items for actual game room
                itemsInActualRoom = game.getGamePlan().getActualRoom().getRoomItemsObservableList();
                itemsInActualRoom.addListener(new ListChangeListener<String>() {
                    @Override
                    public void onChanged(Change<? extends String> change) {
                        updateRoomItemsListView();
                    }
                });

                updateBackground();
                updateActualGameRoomNameLabel();
                updateActualGameRoomDescriptionLabel();
                updateActualRoomMiniMap();
                updateRoomExitsScrollPane();
                updateRightPanel();
            }
        });

        //listener for updating GUI when player approaches new interactable object or NPC in room
        playerActuallyStandsByProperty = game.getGamePlan().playerActuallyStandsBy();
        playerActuallyStandsByProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                updatePlayerActuallyStandsByLabel();
            }
        });

        //listener to handle later changes in ListView with room items
        itemsInActualRoom = game.getGamePlan().getActualRoom().getRoomItemsObservableList();
        itemsInActualRoom.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                updateRoomItemsListView();
            }
        });

        //listener to handle later changes in ListView with inventory items
        itemsInPlayerInventory = game.getGamePlan().getPlayer().getInventory().getItemsInPlayerInventoryObservableList();
        itemsInPlayerInventory.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                updateInventoryItemsListView();
                updateInventoryCapacityLabel();
            }
        });
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
    // --------------------------------------------------------------------------------
    // endregion Game initialization

    // region Menu methods
    // --------------------------------------------------------------------------------
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
    public void showHelpForGraphicalVersionOfGame() {
        Alert helpWindow = new Alert(AlertType.INFORMATION);
        helpWindow.setTitle("Nápoveda");
        helpWindow.getDialogPane().setPrefSize(910.0, 500.0);

        //disable header of pop-up alert and confirmation button
        helpWindow.setHeaderText(null);
        helpWindow.setGraphic(null);
        helpWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        String textToShow = "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!" + SystemInfo.LINE_SEPARATOR
                            + "V miestnostiach môžeš nájsť rôzne predmety či objekty. Nezabudni ich poriadne prehľadať,"
                            + "môžu skrývať mnohé ďalšie užitočné predmety!" + SystemInfo.LINE_SEPARATOR
                            + "V niektorých miestnotiach nie si sám a sú to okrem tebe ďalšie osoby, skús sa s nimi porozprávať alebo v ich blízkosti využiť " + SystemInfo.LINE_SEPARATOR
                            + "nejaký zo svojich predmetov z batohu, možno sa ti odmenia!" + SystemInfo.LINE_SEPARATOR
                            + "Veľa šťastia!"
                            + SystemInfo.LINE_SEPARATOR;
        Label helpText = new Label(textToShow);
        helpText.setContentDisplay(ContentDisplay.CENTER);
        helpText.setTextAlignment(TextAlignment.CENTER);

        //get actual game room name in format with diacritics (defined in RoomNameToDisplay)
        String actualGameRoomName = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(game.getGamePlan().getActualRoom().getName()));
        Label actualGameRoom = new Label("Aktuálne sa nachádzaš v miestnosti: " + actualGameRoomName);

        ImageView gameMapImage = new ImageView(gameRoomMapsImages.get("game_map"));
        gameMapImage.setPreserveRatio(true);
        gameMapImage.setFitWidth(650.0);

        VBox content = new VBox(10, helpText, actualGameRoom, gameMapImage);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        helpWindow.getDialogPane().setContent(content);

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
        Label header = new Label("Názov miestnosti či iného herného objektu sa ti zobrazí pokiaľ naň prejdeš myškou, tento názov následne môžeš využiť v jednom z nasledovných príkazov:" + SystemInfo.LINE_SEPARATOR);

        CommandsList gameTextCommandsList = ((GameImpl)game).getCommandsList();
        Label textCommands = new Label(gameTextCommandsList.getCommandsWithTheirUsage());

        VBox content = new VBox(10, header, textCommands);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        textCommandsWindow.getDialogPane().setContent(content);

        textCommandsWindow.showAndWait();
    }
    // --------------------------------------------------------------------------------
    // endregion Menu methods

    // region Room buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method is used when player wants to look around actual game room in order to find items, interactable objects
     * and NPCs currently placed in game room
     * @param actionEvent
     */
    public void lookAroundRoom(ActionEvent actionEvent) {
        updateGameInteractionOutput(executeGameCommand("rozhliadnut_sa", null));
        if (game.getGamePlan().getActualRoom().wasRoomAlreadyExamined()) {
            updateRoomItemsListView();
            updateRoomInteractableObjectsListView();
            updateRoomNonPlayerCharactersListView();
        }
    }
    // --------------------------------------------------------------------------------
    // endregion Room buttons actions

    // region Map buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method shows big game map in new pop-up window
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
    // --------------------------------------------------------------------------------
    // endregion Scene buttons actions

    // region Inventory buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method updates GUI with text description of inventory item chosen by player
     * @param actionEvent
     */
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

    /**
     * Method updates GUI with text output from inventory item examination.
     * Player can find new items which are hidden in another game item this way
     * @param actionEvent
     */
    public void showInventoryItemExaminationResult(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromInventory = inventoryItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromInventory) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("preskumaj_predmet", gameCommandArguments));
    }

    /**
     * Method updates GUI with text output from usage of inventory item.
     * Some items can be used in game to perform hidden action (e.g. unlocking door)
     * @param actionEvent
     */
    public void useItemFromInventory(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromInventory = inventoryItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromInventory) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("pouzi", gameCommandArguments));
    }

    /**
     * Method remove item(s) from player's inventory and add it (them) to actual game room
     * @param actionEvent
     */
    public void dropItemsFromInventory(ActionEvent actionEvent) {
        ObservableList<String> selectedItemsFromInventory = inventoryItemsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedItemsFromInventory) {
            // following line get item name which is used as argument for game commands from item name which is displayed
            // in game GUI (e.q. "Fľaša s vodou" --(Enum value)--> "BOTTLE" --(item name to execute command)--> "flasa"
            gameCommandArguments.add(ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("odhod", gameCommandArguments));
    }
    // --------------------------------------------------------------------------------
    // endregion Inventory buttons actions

    // region Room items buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method updates GUI with text description of room item chosen by player
     * @param actionEvent
     */
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

    /**
     * Method updates GUI with text output from room item examination.
     * Player can find new items which are hidden in another game item this way
     * @param actionEvent
     */
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
    // --------------------------------------------------------------------------------
    // endregion Room items buttons actions

    // region Room interactable object buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method is used to approach interactable object chosen by player
     * @param actionEvent
     */
    public void approachInteractableObject(ActionEvent actionEvent) {
        ObservableList<String> selectedInteractableObjectFromRoom = roomInteractableObjectsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedInteractableObjectFromRoom) {
            // following line get interactable object name which is used as argument for game commands from interactable object name which is displayed
            // in game GUI (e.q. "Lavička" --(Enum value)--> "BENCH" --(interactable object name to execute command)--> "lavicka"
            gameCommandArguments.add(InteractableObjectName.getInteractableObjectName(InteractableObjectNameToDisplay.getEnumValueForInteractableObjectName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("pristup_k", gameCommandArguments));
    }

    /**
     * Method is used to examine interactable object which player currently stands by.
     * Player can find new hidden items this way
     * @param actionEvent
     */
    public void examineInteractableObject(ActionEvent actionEvent) {
        ObservableList<String> selectedInteractableObjectFromRoom = roomInteractableObjectsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedInteractableObjectFromRoom) {
            // following line get interactable object name which is used as argument for game commands from interactable object name which is displayed
            // in game GUI (e.q. "Lavička" --(Enum value)--> "BENCH" --(interactable object name to execute command)--> "lavicka"
            gameCommandArguments.add(InteractableObjectName.getInteractableObjectName(InteractableObjectNameToDisplay.getEnumValueForInteractableObjectName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("preskumaj_objekt", gameCommandArguments));
    }

    /**
     * Method is used to approach and examine interactable object chosen by player.
     * Player can find object's hidden items this way
     * @param actionEvent
     */
    public void approachAndExamineInteractableObject(ActionEvent actionEvent) {
        ObservableList<String> selectedInteractableObjectFromRoom = roomInteractableObjectsListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedInteractableObjectFromRoom) {
            // following line get interactable object name which is used as argument for game commands from interactable object name which is displayed
            // in game GUI (e.q. "Lavička" --(Enum value)--> "BENCH" --(interactable object name to execute command)--> "lavicka"
            gameCommandArguments.add(InteractableObjectName.getInteractableObjectName(InteractableObjectNameToDisplay.getEnumValueForInteractableObjectName(s)));
        }

        //two game commands will be executed
        updateGameInteractionOutput(executeGameCommand("pristup_k", gameCommandArguments)
                                            + SystemInfo.LINE_SEPARATOR
                                            + executeGameCommand("preskumaj_objekt", gameCommandArguments));
    }
    // --------------------------------------------------------------------------------
    // endregion Room interactable object buttons actions

    // region Room NPCs buttons actions
    // --------------------------------------------------------------------------------
    /**
     * Method is used to approach NPC chosen by player
     * @param actionEvent
     */
    public void approachNonPlayerCharacter(ActionEvent actionEvent) {
        ObservableList<String> selectedNonPlayerCharacterFromRoom = roomNonPlayerCharactersListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedNonPlayerCharacterFromRoom) {
            // following line get non player character name which is used as argument for game commands from non player character name which is displayed
            // in game GUI (e.q. "Upratovačka" --(Enum value)--> "CLEANING_LADY" --(non player character object name to execute command)--> "upratovacka"
            gameCommandArguments.add(NonPlayerCharacterName.getNonPlayerCharacterName(NonPlayerCharacterNameToDisplay.getEnumValueForNonPlayerCharacterName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("pristup_k", gameCommandArguments));
    }

    /**
     * Method is uset to talk with NPC which player currently stands by.
     * NPC can give player usefull hint in order to finish game
     * @param actionEvent
     */
    public void talkToNonPlayerCharacter(ActionEvent actionEvent) {
        ObservableList<String> selectedNonPlayerCharacterFromRoom = roomNonPlayerCharactersListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedNonPlayerCharacterFromRoom) {
            // following line get non player character name which is used as argument for game commands from non player character name which is displayed
            // in game GUI (e.q. "Upratovačka" --(Enum value)--> "CLEANING_LADY" --(non player character object name to execute command)--> "upratovacka"
            gameCommandArguments.add(NonPlayerCharacterName.getNonPlayerCharacterName(NonPlayerCharacterNameToDisplay.getEnumValueForNonPlayerCharacterName(s)));
        }

        updateGameInteractionOutput(executeGameCommand("prihovor_sa", gameCommandArguments));
    }

    /**
     * Method is used to approach and talk to NPC chosen by player.
     * NPC can give player useful hint
     * @param actionEvent
     */
    public void approachAndTalkToNonPlayerCharacter(ActionEvent actionEvent) {
        ObservableList<String> selectedNonPlayerCharacterFromRoom = roomNonPlayerCharactersListView.getSelectionModel().getSelectedItems();
        //used to store item names in format which can be used as game command argument
        List<String> gameCommandArguments = new ArrayList<>();

        for (String s : selectedNonPlayerCharacterFromRoom) {
            // following line get non player character name which is used as argument for game commands from non player character name which is displayed
            // in game GUI (e.q. "Upratovačka" --(Enum value)--> "CLEANING_LADY" --(non player character object name to execute command)--> "upratovacka"
            gameCommandArguments.add(NonPlayerCharacterName.getNonPlayerCharacterName(NonPlayerCharacterNameToDisplay.getEnumValueForNonPlayerCharacterName(s)));
        }

        //two game commands will be executed
        updateGameInteractionOutput(executeGameCommand("pristup_k", gameCommandArguments)
                                            + SystemInfo.LINE_SEPARATOR
                                            + executeGameCommand("prihovor_sa", gameCommandArguments));
    }
    // --------------------------------------------------------------------------------
    // endregion Room NPCs buttons actions

    // region Methods for updating game scene
    // --------------------------------------------------------------------------------
    /**
     * Method updates background of root element depending on which room the player
     * is currently in
     */
    private void updateBackground() {
        String actualGameRoom = game.getGamePlan().getActualRoomName().toLowerCase();
        rootBorderPane.setStyle("-fx-background-image: url(\"" + actualGameRoom + "_background.png" + "\");");
    }

    /**
     * Method updates room mini map placed in top left corner based on actual game room
     */
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
     * Method updates playerActuallyStandsBy Label after player approach new interactable object
     * or NPC in room
     */
    private void updatePlayerActuallyStandsByLabel() {
        String text = "Aktuálne stojíš vedľa objektu (osoby):   ";
        text += game.getGamePlan().getPlayerActuallyStandsBy();

        playerActuallyStandsBy.setText(text);
    }

    /**
     * Method updates information about actual weight of items in inventory
     * and inventory capacity
     */
    private void updateInventoryCapacityLabel() {
        inventoryCapacity.setText("Kapacita batohu: " + getActualInventoryWeight() + "/" + inventoryWeightCapacity);
    }

    /**
     * Method to update ScrollPane with all possible exits from actual game room
     */
    private void updateRoomExitsScrollPane(){
        List<VBox> content = new LinkedList();
        Set<String> roomExitsNames = game.getGamePlan().getActualRoom().getNeighboringRoomsNames();
        String roomNameToDisplay;

        for(String s : roomExitsNames) {
            roomNameToDisplay = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(s));
            Label roomName = new Label(roomNameToDisplay);
            ImageView roomImage = new ImageView(gameRoomsImages.get(roomNameToDisplay));
            VBox roomExit = new VBox(10, roomName, roomImage);
            roomExit.setPadding(new Insets(5.0));
            roomExit.setAlignment(Pos.CENTER);
            roomExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    updateGameInteractionOutput(executeGameCommand("chod", new ArrayList<>(){{ add(s); }}));
                }
            });

            Tooltip.install(roomExit, new Tooltip("Prejsť do miestnosti " + s));
            content.add(roomExit);
        }

        roomExits.getChildren().clear();
        roomExits.getChildren().addAll(content);
        roomExits.setAlignment(Pos.CENTER);
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
        //clear right panel if actual game room was not examined yet and set placeholders for ListViews above
        else {
            roomItemsListView.getItems().clear();
            roomInteractableObjectsListView.getItems().clear();
            roomNonPlayerCharactersListView.getItems().clear();

            Label itemsListViewPlaceholder = new Label("Najprv sa poriadne rozhliadni po miestnosti pokiaľ v nej chceš nájsť nejaké predmety");
            Label interactableObjectsListViewPlaceholder = new Label("Najprv sa poriadne rozhliadni po miestnosti aby si objavil objekty, ktoré sa v nej nachádzajú");
            Label nonPlayerCharactersListViewPlaceholder = new Label("Najprv sa poriadne rozhliadni po miestnosti aby si zistil, či tu nie si sám");
            roomItemsListView.setPlaceholder(itemsListViewPlaceholder);
            roomInteractableObjectsListView.setPlaceholder(interactableObjectsListViewPlaceholder);
            roomNonPlayerCharactersListView.setPlaceholder(nonPlayerCharactersListViewPlaceholder);
        }
    }

    /**
     * Method to update inventoryItemsListView with items which are currently placed in player's inventory.
     */
    private void updateInventoryItemsListView() {
        inventoryItemsListView.getItems().clear();

        if (itemsInPlayerInventory.size() == 0) {
            inventoryItemsListView.setPlaceholder(new Label("Tvoj batoh zíva prázdnotou"));
        }
        else {
            inventoryItemsListView.getItems().addAll(itemsInPlayerInventory);
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
                        setTooltip(new Tooltip("váha predmetu: "
                                               + ItemWeight.getItemWeight(ItemNameToDisplay.getEnumValueForItemName(itemNameToDisplay))
                                               + ", pre využitie v textovom príkaze použi názov: "
                                               + ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(itemNameToDisplay))));
                    }
                }
            });
        }
    }

    /**
     * Method to update roomItemsListView with items which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomItemsListView() {
        roomItemsListView.getItems().clear();

        if (itemsInActualRoom.size() == 0) {
            roomItemsListView.setPlaceholder(new Label("V miestnosti sa aktuálne nenachádza žiadny predmet"));
        }
        else {
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
                        setTooltip(new Tooltip("váha predmetu: "
                                                       + ItemWeight.getItemWeight(ItemNameToDisplay.getEnumValueForItemName(itemNameToDisplay))
                                                       + ", pre využitie v textovom príkaze použi názov: "
                                                       + ItemName.getItemName(ItemNameToDisplay.getEnumValueForItemName(itemNameToDisplay))));
                    }
                }
            });
        }
    }

    /**
     * Method to update roomInteractableObjectsListView with interactable objects which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomInteractableObjectsListView() {
        roomInteractableObjectsListView.getItems().clear();

        if (getNumberOfInteractableObjectsInRoom() == 0) {
            roomInteractableObjectsListView.setPlaceholder(new Label("V miestnosti sa nenachádza žiadny objekt, ktorý by šiel využiť alebo preskúmať"));
        }
        else {
            Set<String> roomInteractableObjectNames = game.getGamePlan().getActualRoom().getRoomInteractableObjectsNames();
            ObservableList<String> roomInteractableObjects = FXCollections.observableArrayList();

            for (String s : roomInteractableObjectNames) {
                // following line get interactable object name which will be displayed in GUI from interactable object name in format used for game command execution, e.g.:
                // (interactable object name for game command execution) "lavicka" --(Enum value)--> "BENCH" --(item name to display in GUI)--> "Lavička"
                roomInteractableObjects.add(InteractableObjectNameToDisplay.getInteractableObjectNameToDisplay(InteractableObjectName.getEnumValueForInteractableObjectName(s)));
            }

            roomInteractableObjectsListView.getItems().addAll(roomInteractableObjects);
            roomInteractableObjectsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            roomInteractableObjectsListView.setCellFactory(param -> new ListCell<>() {
                private ImageView displayImage = new ImageView();

                @Override
                public void updateItem(String interactableObjectNameToDisplay, boolean empty) {
                    super.updateItem(interactableObjectNameToDisplay, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        displayImage.setImage(gameInteractableObjectsImages.get(interactableObjectNameToDisplay));
                        setText(interactableObjectNameToDisplay);
                        setGraphic(displayImage);
                        setTooltip(new Tooltip("pre využitie v textovom príkaze použi názov: "
                                               + InteractableObjectName.getInteractableObjectName(InteractableObjectNameToDisplay.getEnumValueForInteractableObjectName(interactableObjectNameToDisplay))));
                    }
                }
            });
        }
    }

    /**
     * Method to update roomNonPlayerCharactersListView with NPCs which are currently placed in actual game room.
     * Method is used when player is looking around the room in order to examine it
     */
    private void updateRoomNonPlayerCharactersListView() {
        roomNonPlayerCharactersListView.getItems().clear();

        if (getNumberOfNonPlayerCharactersInRoom() == 0) {
            roomNonPlayerCharactersListView.setPlaceholder(new Label("V miestnosti okrem teba nie je nikto iný"));
        }
        else {
            Set<String> roomNonPlayerCharacterNames = game.getGamePlan().getActualRoom().getRoomNonPlayerCharactersNames();
            ObservableList<String> roomNonPlayerCharacters = FXCollections.observableArrayList();

            for (String npc : roomNonPlayerCharacterNames) {
                // following line get non-player character name which will be displayed in GUI from non-player character object name in format used for game command execution, e.g.:
                // (non-player character name for game command execution) "upratovacka" --(Enum value)--> "CLEANING_LADY" --(item name to display in GUI)--> "Upratovačka"
                roomNonPlayerCharacters.add(NonPlayerCharacterNameToDisplay.getNonPlayerCharacterNameToDisplay(NonPlayerCharacterName.getEnumValueForNonPlayerCharacterName(npc)));
            }

            roomNonPlayerCharactersListView.getItems().addAll(roomNonPlayerCharacters);
            roomNonPlayerCharactersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            roomNonPlayerCharactersListView.setCellFactory(param -> new ListCell<>() {
                private ImageView displayImage = new ImageView();

                @Override
                public void updateItem(String nonPlayerCharacterNameToDisplay, boolean empty) {
                    super.updateItem(nonPlayerCharacterNameToDisplay, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        displayImage.setImage(gameNonPlayerCharactersImages.get(nonPlayerCharacterNameToDisplay));
                        setText(nonPlayerCharacterNameToDisplay);
                        setGraphic(displayImage);
                        setTooltip(new Tooltip("pre využitie v textovom príkaze použi názov: "
                                               + NonPlayerCharacterName.getNonPlayerCharacterName(NonPlayerCharacterNameToDisplay.getEnumValueForNonPlayerCharacterName(nonPlayerCharacterNameToDisplay))));
                    }
                }
            });
        }
    }
    // --------------------------------------------------------------------------------
    // endregion Methods for updating game scene

    // region Help methods used in another controller's methods
    // --------------------------------------------------------------------------------
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
     * Method returns total weight of items which are currently in player's inventory
     * @return int representing total weight of items in inventory
     */
    private int getActualInventoryWeight() {
        return game.getGamePlan().getPlayer().getInventory().getInventoryWeight();
    }

    /**
     * Method shows help pop up window for text version of game.
     * This window contains text game commands with their format and usage,
     * actual game room and map
     */
    private void showHelpForTextVersionOfGame() {
        Alert helpWindow = new Alert(AlertType.INFORMATION);
        helpWindow.setTitle("Nápoveda");
        helpWindow.getDialogPane().setPrefSize(910.0, 500.0);

        //disable header of pop-up alert and confirmation button
        helpWindow.setHeaderText(null);
        helpWindow.setGraphic(null);
        helpWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        Label gameGoal = new Label("Tvojou úlohou je dostať sa von na ulicu a zachrániť si tak život!");
        Label possibleCommandsTitle = new Label("Názov miestnosti či iného herného objektu sa ti zobrazí pokiaľ naň prejdeš myškou, tento názov následne môžeš využiť v jednom z nasledovných príkazov:");

        //label containing all possible text commands which player can use during the game
        ScrollPane textCommandsScrollPane = new ScrollPane();
        CommandsList gameTextCommandsList = ((GameImpl)game).getCommandsList();
        Label textCommands = new Label(gameTextCommandsList.getCommandsWithTheirUsage());
        textCommandsScrollPane.setContent(textCommands);
        textCommandsScrollPane.setMinViewportHeight(120.0);

        //get actual game room name in format with diacritics (defined in RoomNameToDisplay)
        String actualGameRoomName = RoomNameToDisplay.getRoomNameToDisplay(RoomName.getEnumValueForRoomName(game.getGamePlan().getActualRoom().getName()));
        Label actualGameRoom = new Label("Aktuálne sa nachádzaš v miestnosti: " + actualGameRoomName);

        //game map
        ImageView gameMapImage = new ImageView(gameRoomMapsImages.get("game_map"));
        gameMapImage.setPreserveRatio(true);
        gameMapImage.setFitWidth(500.0);

        VBox content = new VBox(10, gameGoal, possibleCommandsTitle, textCommandsScrollPane, actualGameRoom, gameMapImage);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        helpWindow.getDialogPane().setContent(content);

        helpWindow.showAndWait();
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
     * Method returns number of interactable objects which are placed in actual game room
     * and player can interact with them
     * @return number of interactable objects which are placed in actual game room
     */
    private int getNumberOfInteractableObjectsInRoom() {
        return game.getGamePlan().getActualRoom().getRoomInteractableObjectsNames().size();
    }

    /**
     * Method returns number of non-player characters which are in actual game room
     * and player can interact with them
     * @return number of non-player characters which are in actual game room
     */
    private int getNumberOfNonPlayerCharactersInRoom() {
        return game.getGamePlan().getActualRoom().getRoomNonPlayerCharactersNames().size();
    }
    // --------------------------------------------------------------------------------
    // endregion Help methods used in another controller's methods

    /**
     * Method shows one of possible game endings in new pop-up window
     */
    private void showGameEnding() {
        Alert gameEndingWindow = new Alert(AlertType.INFORMATION);
        gameEndingWindow.setTitle("Koniec hry");
        gameEndingWindow.getDialogPane().setPrefSize(910.0, 500.0);

        //disable header of pop-up alert and confirmation button
        gameEndingWindow.setHeaderText(null);
        gameEndingWindow.setGraphic(null);
        gameEndingWindow.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        //set content
        //label with game ending
        Label gameEnding = new Label(((GameImpl)game).getGameEnding());
        gameEnding.setContentDisplay(ContentDisplay.CENTER);
        gameEnding.setTextAlignment(TextAlignment.CENTER);

        //button for starting new game
        Button newGameButton = new Button("Začať novú hru");
        newGameButton.setPrefWidth(100);
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage popUpWindow = (Stage) newGameButton.getScene().getWindow();
                popUpWindow.close();

                Start.setUpNewGame(new Stage());
                Stage currentStage = (Stage) rootBorderPane.getScene().getWindow();
                currentStage.close();
            }
        });

        //button for ending current game
        Button endGameButton = new Button("Ukončiť hru");
        endGameButton.setPrefWidth(100);
        endGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage popUpWindow = (Stage) endGameButton.getScene().getWindow();
                popUpWindow.close();

                Stage currentStage = (Stage) rootBorderPane.getScene().getWindow();
                currentStage.close();
            }
        });

        HBox buttons = new HBox(10, newGameButton, endGameButton);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(10, gameEnding, buttons);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        gameEndingWindow.getDialogPane().setContent(content);

        gameEndingWindow.showAndWait();
    }

    /**
     * Method takes user input from gameConsole TextField and execute this text input as game command
     * @param actionEvent
     */
    public void executeTypedInGameCommand(ActionEvent actionEvent) {
        String commandToExecute = gameConsole.getText();
        if (commandToExecute.equals("napoveda")) {
            showHelpForTextVersionOfGame();
        }
        else if (commandToExecute.equals("koniec")){
            Stage currentStage = (Stage) rootBorderPane.getScene().getWindow();
            currentStage.close();
        }
        else if (commandToExecute.equals("rozhliadnut_sa")) {
            lookAroundRoom(new ActionEvent());
        }
        else {
            String commandExecutionOutput = game.parseUserInput(commandToExecute);
            updateGameInteractionOutput(commandExecutionOutput);
        }
    }
}
