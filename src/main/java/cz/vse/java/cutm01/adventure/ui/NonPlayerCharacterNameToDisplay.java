package cz.vse.java.cutm01.adventure.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Game non-player characters (which player can discover during game) with their name, i.e. Strings which will be displayed
 * to player in game GUI
 *
 * @author Cúth Michal (xname: cutm01)
 * @version 1.0.0
 */
public enum NonPlayerCharacterNameToDisplay {
    CLEANING_LADY("Upratovačka"),
    COUGHING_TEACHER("Kašlajúci profesor"),
    IT_ADMIN("IT admin"),
    DOOR_KEEPER("Vrátnik");

    private final String nonPlayerCharacterNameToDisplay;
    private static final Map<String, String> NPC_NAMES_TO_DISPLAY = new HashMap<>();
    private static final Map<String, String> ENUM_VALUES_FOR_NPC_NAMES = new HashMap<>();

    static {
        for (NonPlayerCharacterNameToDisplay npc : values()) {
            NPC_NAMES_TO_DISPLAY.put(npc.toString(), npc.nonPlayerCharacterNameToDisplay);
            ENUM_VALUES_FOR_NPC_NAMES.put(npc.nonPlayerCharacterNameToDisplay, npc.toString());
        }
    }

    NonPlayerCharacterNameToDisplay(final String nonPlayerCharacterNameToDisplay) {
        this.nonPlayerCharacterNameToDisplay = nonPlayerCharacterNameToDisplay;
    }

    public static String getNonPlayerCharacterNameToDisplay(String nonPlayerCharacterNameToDisplay) {
        return NPC_NAMES_TO_DISPLAY.get(nonPlayerCharacterNameToDisplay);
    }

    public static String getEnumValueForNonPlayerCharacterName(String itemName) {
        return ENUM_VALUES_FOR_NPC_NAMES.get(itemName);
    }
}