package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LookAround command implementation, player can use this command to examine actual game room in
 * order to find items, interactable objects or non-player characters he can interact with
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class LookAroundCommand implements Command {

    private static final String NAME = CommandName.LOOK_AROUND.getCommandName();
    private static final String DESCRIPTION = CommandDescription.LOOK_AROUND.getCommandDescription();
    private final GamePlan gamePlan;
    private final Map<String, String> textToDisplay;
    private static final String ITEMS_IN_ROOM_TEXT = "V miestnosti si si všimol nasledovné predmety: ";
    private static final String OBJECTS_IN_ROOM_TEXT = "Dobre sa rozhliadneš a uvidíš nasledovné objekty: ";
    private static final String NPC_IN_ROOM_TEXT = "Nie si tu sám, okrem teba je tu ešte ";
    private static final String EXITS = "Východy: ";

    public LookAroundCommand(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
        textToDisplay = new LinkedHashMap<>();
        textToDisplay.put(ITEMS_IN_ROOM_TEXT, null);
        textToDisplay.put(OBJECTS_IN_ROOM_TEXT, null);
        textToDisplay.put(NPC_IN_ROOM_TEXT, null);
        textToDisplay.put(EXITS, null);
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandDescription() {
        return DESCRIPTION;
    }

    // TODO: zobrazovat veci aj s hmotnostami

    /**
     * Method returns names of all interactable objects in actual room
     *
     * @param commandParameters command is used without any parameters
     * @return name of all objects which player can interact with
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length > 0) {
            return "Zbytočne to komplikuješ! Skús zadať ten príkaz ešte raz bez akýchkoľvek parametrov";
        }

        // get names of items, object, NPCs and exits for game actual room
        Room actualRoom = gamePlan.getActualRoom();
        String itemNames =
            (actualRoom.getNumberOfItemsInRoom() > 0) ? actualRoom.getItemNamesAsString() : null;
        String objectNames =
            (actualRoom.getNumberOfInteractableObjectsInRoom() > 0)
            ? actualRoom.getInteractableObjectNamesAsString()
            : null;
        String characterNames =
            (actualRoom.getNumberOfNonPlayerCharactersInRoom() > 0)
            ? actualRoom.getNonPlayerCharacterNamesAsString()
            : null;
        String exits =
            (actualRoom.getNeighboringRooms().size() > 0) ? actualRoom.getNeighboringRoomNames()
                                                          : null;
        textToDisplay.replace(ITEMS_IN_ROOM_TEXT, itemNames);
        textToDisplay.replace(OBJECTS_IN_ROOM_TEXT, objectNames);
        textToDisplay.replace(NPC_IN_ROOM_TEXT, characterNames);
        textToDisplay.replace(EXITS, exits);

        // get text which will be displayed to player
        StringBuilder text = new StringBuilder();
        Map<String, String> actualRoomContent = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : textToDisplay.entrySet()) {
            // add only that entry where key is mapped to non null value, which means room contains either
            // items, NPCs, objects or exits
            if (entry.getValue() != null) {
                actualRoomContent.put(entry.getKey(), entry.getValue());
            }
        }

        Iterator<Map.Entry<String, String>> entryIterator = actualRoomContent.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (entryIterator.hasNext()) {
            entry = entryIterator.next();
            if (entry.getValue() == null) {
                continue;
            }

            text.append(entry.getKey()).append(entry.getValue());
            if (entryIterator.hasNext()) {
                text.append(SystemInfo.LINE_SEPARATOR);
            }
        }

        if (text.length() == 0) {
            gamePlan.getActualRoom().setWasRoomAlreadyExamined(true);
            return "Rozhliadol si sa po miestnosti, no neuvidel si nič, čo by ti pomohlo v tvojej snahe dostať sa von živý";
        }
        gamePlan.getActualRoom().setWasRoomAlreadyExamined(true);

        return text.toString();
    }
}
