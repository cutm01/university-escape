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

    static {
        for (NonPlayerCharacterName npc : values()) {
            NPC_NAMES.put(npc.toString(), npc.nonPlayerCharacterName);
        }
    }

    NonPlayerCharacterName(final String nonPlayerCharacterName) {
        this.nonPlayerCharacterName = nonPlayerCharacterName;
    }

    public static String getNonPlayerCharacterName(String characterName) {
        return NPC_NAMES.get(characterName);
    }
}
