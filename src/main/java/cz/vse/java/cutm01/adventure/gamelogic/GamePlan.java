package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.ui.InteractableObjectNameToDisplay;
import cz.vse.java.cutm01.adventure.ui.NonPlayerCharacterNameToDisplay;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Class GamePlan - třída představující mapu a stav adventury.
 *
 * <p>Tato třída inicializuje prvky ze kterých se hra skládá: vytváří všechny prostory, propojuje
 * je vzájemně pomocí východů a pamatuje si aktuální prostor, ve kterém se hráč právě nachází.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
public class GamePlan {

    private Room actualRoom;
    // InteractableObject which the player is standing nearby
    private InteractableObject actualInteractableObject;
    // NPC who the player is standing next to
    private NonPlayerCharacter actualNonPlayerCharacter;
    private final Player player;
    // game ends after player reaches room Street
    private boolean hasPlayerReachedFinalRoom;
    private StringProperty actualRoomName = new SimpleStringProperty();
    private StringProperty playerActuallyStandsBy = new SimpleStringProperty();
    private BooleanProperty playerFinishedGame = new SimpleBooleanProperty();
    // region Constructor
    // --------------------------------------------------------------------------------

    /**
     * Konstruktor který vytváří jednotlivé prostory a propojuje je pomocí východů. Jako výchozí
     * aktuální prostor nastaví halu.
     */
    public GamePlan() {
        player = new Player();
        hasPlayerReachedFinalRoom = false;
        playerActuallyStandsBy.setValue(getNameOfGameObjectPlayerStandsBy());
        setUpGamePlan();
    }
    // --------------------------------------------------------------------------------
    // endregion Constructor

    // region JavaFX beans getters and setters
    // --------------------------------------------------------------------------------
    /**
     * Getter for the property's value
     * @return value of actualRoomName StringProperty
     */
    public final String getActualRoomName(){return actualRoomName.get();}

    /**
     * Setter for the property's value
     */
    public final void setActualRoomName(String value){actualRoomName.set(value);}

    /**
     * Getter for the property itself
     * @return StringProperty actualRoomName
     */
    public StringProperty actualRoomNameProperty() {return actualRoomName;}

    /**
     * Getter for the property's value
     * @return value of playerActuallyStandsBy StringProperty
     */
    public final String getPlayerActuallyStandsBy(){return playerActuallyStandsBy.get();}

    /**
     * Setter for the property's value
     */
    public final void setPlayerActuallyStandsBy(String value){playerActuallyStandsBy.set(value);}

    /**
     * Getter for the property itself
     * @return StringProperty playerActuallyStandsBy
     */
    public StringProperty playerActuallyStandsBy() {return playerActuallyStandsBy;}

    /**
     * Getter for the property's value
     * @return value of playerFinishedGame BooleanProperty
     */
    public final boolean getPlayerFinishedGame(){return playerFinishedGame.get();}

    /**
     * Setter for the property's value
     */
    public final void setPlayerFinishedGame(boolean value){playerFinishedGame.set(value);}

    /**
     * Getter for the property itself
     * @return BooleanProperty playerFinishedGame
     */
    public BooleanProperty playerFinishedGame() {return playerFinishedGame;}
    // --------------------------------------------------------------------------------
    // endregion JavaFX beans getters and setters

    // region Getters and Setters
    // --------------------------------------------------------------------------------
    /**
     * Metoda vrací odkaz na aktuální prostor, ve ktetém se hráč právě nachází.
     *
     * @return aktuální prostor
     */
    public Room getActualRoom() {
        return actualRoom;
    }

    public boolean hasPlayerReachedFinalRoom() {
        return hasPlayerReachedFinalRoom;
    }

    public void setHasPlayerReachedFinalRoom(boolean hasPlayerReachedFinalRoom) {
        this.hasPlayerReachedFinalRoom = hasPlayerReachedFinalRoom;
        playerFinishedGame.setValue(hasPlayerReachedFinalRoom);
    }

    /**
     * Metoda nastaví aktuální room, používá se nejčastěji při přechodu mezi prostory
     *
     * @param room nový aktuální room
     */
    public void setActualRoom(Room room) {
        actualRoom = room;
        actualRoomName.setValue(room.getName());
        playerActuallyStandsBy.setValue(getNameOfGameObjectPlayerStandsBy());
    }

    /**
     * Method returns the actual interactable object which player is currently standing nearby<br>
     * This object is inside the actual room (i.e actualRoom = Hall, actualInteractableObject =
     * Vending Machine)
     *
     * @return interactable object which player is currently standing nearby
     */
    public InteractableObject getActualInteractableObject() {
        return actualInteractableObject;
    }

    /**
     * Method sets new actual interactable object if player moves toward it (by using
     * ApproachCommand)
     * <br>
     * This interactable object is inside the actual room (e.g. player moves from window toward
     * bench)
     *
     * @param interactableObject new actual interactable object to set
     */
    public void setActualInteractableObject(InteractableObject interactableObject) {
        actualInteractableObject = interactableObject;
        playerActuallyStandsBy.setValue(getNameOfGameObjectPlayerStandsBy());
    }

    /**
     * Method returns the actual non player character who player is currently standing next to<br>
     * This NPC is inside the actual room (i.e actualRoom = Office, actualNonPlayerCharacter = IT
     * admin)
     *
     * @return interactable object which player is currently standing nearby
     */
    public NonPlayerCharacter getActualNonPlayerCharacter() {
        return actualNonPlayerCharacter;
    }

    /**
     * Method sets new actual non player character if player moves toward it (by using
     * ApproachCommand)<br> This NPC is inside the actual room (i.e actualRoom = Office,
     * actualNonPlayerCharacter = IT admin)
     *
     * @param nonPlayerCharacter new actual non-player character to set
     */
    public void setActualNonPlayerCharacter(NonPlayerCharacter nonPlayerCharacter) {
        this.actualNonPlayerCharacter = nonPlayerCharacter;
    }

    public Player getPlayer() {
        return player;
    }
    // --------------------------------------------------------------------------------
    // endregion Getters and Setters

    // region Methods used to set up game plan
    // --------------------------------------------------------------------------------

    /**
     * Method is used to set up initial state of game plan<br> It will create all rooms, add exits
     * (i.e. neighboring rooms) for each room and fill it with interactable object, items and
     * non-player characters
     */
    private void setUpGamePlan() {
        Map<String, Room> gameRooms = createGameRooms();
        addExitsFromEachRoom(gameRooms);
        addInteractableObjectToEachRoom(gameRooms);
        addItemsToEachRoom(gameRooms);
        addNonPlayerCharacterToEachRoom(gameRooms);

        actualRoom = gameRooms.get("RB_201"); // game begin in this room
        setActualRoomName("RB_201");
    }

    /**
     * Method creates one Room instance for each constant defined in RoomName enum
     *
     * @return Map containing all game rooms, value from RoomName enum is used as key
     */
    private Map<String, Room> createGameRooms() {
        // create room instance for every entry in RoomName enum
        EnumSet<RoomSetUpInformation> roomNames = EnumSet.allOf(RoomSetUpInformation.class);
        Map<String, Room> gameRooms = new HashMap<>();
        for (RoomSetUpInformation roomName : roomNames) {
            gameRooms.put(roomName.toString(),
                new Room(roomName.toString(), roomName.isRoomLocked()));
        }

        return gameRooms;
    }

    /**
     * Method adds exits (i.e. neighboring rooms) for each game room as it is specified in
     * RoomSetUpInformation enum
     *
     * @param gameRooms Map containing all game rooms, value from RoomName enum is used as key
     */
    private void addExitsFromEachRoom(Map<String, Room> gameRooms) {
        EnumSet<RoomSetUpInformation> roomNames = EnumSet.allOf(RoomSetUpInformation.class);
        String[] neighboringRoomNames;
        for (RoomSetUpInformation roomName : roomNames) {
            neighboringRoomNames = roomName.getRoomExits();
            // get one Room instance at time from Map gameRoom above and all possible exits for this room
            for (String name : neighboringRoomNames) {
                gameRooms.get(roomName.toString()).addExitFromRoom(gameRooms.get(name));
            }
        }
    }

    /**
     * Method adds interactable objects to game rooms as it is specified in RoomSetUpInformation
     * enum and set special action for them as they are specified in SpecialActions enum
     *
     * @param gameRooms Map containing all game rooms, value from RoomName enum is used as key
     */
    private void addInteractableObjectToEachRoom(Map<String, Room> gameRooms) {
        Map<String, SpecialActions> allSpecialAction =
            SpecialActions.getObjectsAndNonPlayerCharactersWithSpecialActions();
        EnumSet<RoomSetUpInformation> roomNames = EnumSet.allOf(RoomSetUpInformation.class);
        InteractableObject[] interactableObjectsToAdd;
        for (RoomSetUpInformation roomName : roomNames) {
            interactableObjectsToAdd = roomName.getInteractableObjectsInRoom();
            // get one Room instance at time from Map gameRoom above and all interactable objects for this
            // room
            for (InteractableObject object : interactableObjectsToAdd) {
                gameRooms.get(roomName.toString()).addInteractableObjectToRoom(object);
                // set special actions for added interactable object
                SpecialActions specialActionsForObject = allSpecialAction.get(object.getName());
                gameRooms
                    .get(roomName.toString())
                    .getInteractableObjectByName(object.getName())
                    .setSpecialActions(
                        specialActionsForObject.getItemsWithActionTheyPerform(),
                        specialActionsForObject.getRoomToUnlockAfterItemUsage(),
                        specialActionsForObject.getItemsToAddToRoomAfterItemUsage(),
                        specialActionsForObject.getOutputTextAfterItemUsage());
            }
        }
    }

    /**
     * Method adds items to game rooms as it is specified in RoomSetUpInformation enum
     *
     * @param gameRooms Map containing all game rooms, value from RoomName enum is used as key
     */
    private void addItemsToEachRoom(Map<String, Room> gameRooms) {
        EnumSet<RoomSetUpInformation> roomNames = EnumSet.allOf(RoomSetUpInformation.class);
        Item[] itemsToAdd;
        for (RoomSetUpInformation roomName : roomNames) {
            itemsToAdd = roomName.getItemsInRoom();
            // get one Room instance at time from Map gameRoom above and all items for this room
            for (Item item : itemsToAdd) {
                gameRooms.get(roomName.toString()).addItemToRoom(item);
            }
        }
    }

    /**
     * Method adds non-player characters to game rooms as it is specified in RoomSetUpInformation
     * enum and set special action for them as they are specified in SpecialActions enum
     *
     * @param gameRooms Map containing all game rooms, value from RoomName enum is used as key
     */
    private void addNonPlayerCharacterToEachRoom(Map<String, Room> gameRooms) {
        Map<String, SpecialActions> allSpecialAction =
            SpecialActions.getObjectsAndNonPlayerCharactersWithSpecialActions();
        EnumSet<RoomSetUpInformation> roomNames = EnumSet.allOf(RoomSetUpInformation.class);
        NonPlayerCharacter[] nonPlayerCharacters;
        for (RoomSetUpInformation roomName : roomNames) {
            nonPlayerCharacters = roomName.getNonPlayerCharactersInRoom();
            // get one Room instance at time from Map gameRoom above and all non-player characters for
            // this room
            for (NonPlayerCharacter npc : nonPlayerCharacters) {
                gameRooms.get(roomName.toString()).addNonPlayerCharacterToRoom(npc);
                // set special actions for added non-player character
                SpecialActions specialActionsForNPC = allSpecialAction.get(npc.getName());
                gameRooms
                    .get(roomName.toString())
                    .getNonPlayerCharacterByName(npc.getName())
                    .setSpecialActions(
                        specialActionsForNPC.getItemsWithActionTheyPerform(),
                        specialActionsForNPC.getRoomToUnlockAfterItemUsage(),
                        specialActionsForNPC.getItemsToAddToRoomAfterItemUsage(),
                        specialActionsForNPC.getOutputTextAfterItemUsage());
            }
        }
    }
    // endregion Methods used to set up game plan
    // --------------------------------------------------------------------------------

    /**
     * Method returns name of NonPlayerCharacter or InteractableObject which player
     * currently stands by
     * @return name of NonPlayerCharacter or InteractableObject player currently stands by and which will be shown in GUI,
     * String "---" if player does not currently stands by any of it
     */
    private String getNameOfGameObjectPlayerStandsBy() {
        if (actualInteractableObject != null) {
            // following line get interactable object name which will be displayed in GUI from interactable object name in format used for game command execution, e.g.:
            // (interactable object name for game command execution) "lavicka" --(Enum value)--> "BENCH" --(item name to display in GUI)--> "Lavička"
            return InteractableObjectNameToDisplay.getInteractableObjectNameToDisplay(InteractableObjectName.getEnumValueForInteractableObjectName(actualInteractableObject.getName()));
        }

        if (actualNonPlayerCharacter != null) {
            // following line get non-player character name which will be displayed in GUI from non-player character object name in format used for game command execution, e.g.:
            // (non-player character name for game command execution) "upratovacka" --(Enum value)--> "CLEANING_LADY" --(item name to display in GUI)--> "Upratovačka"
            return NonPlayerCharacterNameToDisplay.getNonPlayerCharacterNameToDisplay(NonPlayerCharacterName.getEnumValueForNonPlayerCharacterName(actualNonPlayerCharacter.getName()));
        }

        return "---";
    }
}
