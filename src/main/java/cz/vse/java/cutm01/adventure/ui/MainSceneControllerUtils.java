package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.InteractableObjectName;
import cz.vse.java.cutm01.adventure.gamelogic.ItemName;
import cz.vse.java.cutm01.adventure.gamelogic.NonPlayerCharacterName;
import cz.vse.java.cutm01.adventure.gamelogic.RoomName;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * MainSceneControllerUtils class contains utility methods used in MainSceneController class
 * such as methods for loading game images
 *
 * @author Michal Cúth
 * @version 1.0.0
 */
public class MainSceneControllerUtils {
    /**
     * Method to load images for all game items
     * @return Map where key is name of game item and value is corresponding image
     */
    public Map<String, Image> loadGameItemsImages() {
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
     * Method to load images for all game interactable objects
     * @return Map where key is name of game interactable object and value is corresponding image
     */
    public Map<String, Image> loadGameInteractableObjectsImages() {
        Map<String, Image> loadedImages = new HashMap<>();

        Object[] interactableObjectNames = InteractableObjectName.values();
        for(Object o : interactableObjectNames) {
            String interactableObjectName = o.toString().toLowerCase();

            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(interactableObjectName + ".png");
            Image itemImage = new Image(imageStream);

            loadedImages.put(InteractableObjectNameToDisplay.getInteractableObjectNameToDisplay(o.toString()), itemImage);
        }

        return loadedImages;
    }

    /**
     * Method to load images for all game non player characters
     * @return Map where key is name of game non player character object and value is corresponding image
     */
    public Map<String, Image> loadGameNonPlayerCharactersImages() {
        Map<String, Image> loadedImages = new HashMap<>();

        Object[] nonPlayerCharacterNames = NonPlayerCharacterName.values();
        for(Object npc : nonPlayerCharacterNames) {
            String nonPlayerCharacterName = npc.toString().toLowerCase();

            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(nonPlayerCharacterName + ".png");
            Image itemImage = new Image(imageStream);

            loadedImages.put(NonPlayerCharacterNameToDisplay.getNonPlayerCharacterNameToDisplay(npc.toString()), itemImage);
        }

        return loadedImages;
    }

    /**
     * Method to load images for all minimaps of game rooms and one big game map with all rooms in it
     */
    public Map<String, Image> loadGameRoomMapsImages() {
        Map<String, Image> loadedImages = new HashMap<>();
        Object[] gameRoomNames = RoomName.values();
        for(Object r : gameRoomNames) {
            String gameRoomName = r.toString().toLowerCase();

            //there is no minimap for game room with name street as game ends after reaching this room
            if (!gameRoomName.equals("street")) {
                InputStream imageStream = getClass().getClassLoader().getResourceAsStream(gameRoomName + "_map.png");
                Image gameRoomMapImage = new Image(imageStream);
                loadedImages.put(gameRoomName, gameRoomMapImage);
            }
        }

        //insert image for whole game map
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream("game_map.png");
        Image gameRoomMapImage = new Image(imageStream);
        loadedImages.put("game_map", gameRoomMapImage);

        return loadedImages;
    }

    /**
     * Method to load images for all game rooms
     */
    public Map<String, Image> loadGameRoomsImages() {
        Map<String, Image> loadedImages = new HashMap<>();

        Object[] roomNames = RoomName.values();
        for(Object room : roomNames) {
            String roomName = room.toString().toLowerCase();

            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(roomName + ".png");
            Image itemImage = new Image(imageStream);

            loadedImages.put(RoomNameToDisplay.getRoomNameToDisplay(room.toString()), itemImage);
        }

        return loadedImages;
    }
}
