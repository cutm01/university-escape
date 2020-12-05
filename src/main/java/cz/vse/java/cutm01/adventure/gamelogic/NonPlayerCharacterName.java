package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Non-player character (which player can discover during game) with their name, i.e. Strings which
 * player has to type in order to talk with them or give them any item
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum NonPlayerCharacterName {
    CLEANING_LADY("upratovacka"),
    COUGHING_TEACHER("podozrivo_kaslajuci_profesor"),
    IT_ADMIN("IT_admin"),
    DOOR_KEEPER("vratnik");

    private final String nonPlayerCharacterName;
    private static final Map<String, String> NPC_NAMES = new HashMap<>();
    private static final Map<String, String> ENUM_VALUES_FOR_NON_PLAYER_CHARACTER_NAMES = new HashMap<>();


    static {
        for (NonPlayerCharacterName npc : values()) {
            NPC_NAMES.put(npc.toString(), npc.nonPlayerCharacterName);
            ENUM_VALUES_FOR_NON_PLAYER_CHARACTER_NAMES.put(npc.nonPlayerCharacterName, npc.toString());
        }
    }

    NonPlayerCharacterName(final String nonPlayerCharacterName) {
        this.nonPlayerCharacterName = nonPlayerCharacterName;
    }

    public static String getNonPlayerCharacterName(String nonPlayerCharacterName) {
        return NPC_NAMES.get(nonPlayerCharacterName);
    }

    public static String getEnumValueForNonPlayerCharacterName(String nonPlayerCharacterName) {
        return ENUM_VALUES_FOR_NON_PLAYER_CHARACTER_NAMES.get(nonPlayerCharacterName);
    }
}
