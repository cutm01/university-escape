package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Game room with their name, i.e. Strings which player has to type in order to enter them
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum RoomName {
    RB_201("RB_201"),
    RB_202("RB_202"),
    FIRST_FLOOR_HALL("chodba_na_I._poschodi"),
    SECOND_FLOOR_HALL("chodba_na_II._poschodi"),
    TOILETS("toalety"),
    OFFICE("kancelaria"),
    DRESSING_ROOM("satna"),
    NEW_BUILDING("Nova_budova"),
    OLD_BUILDING("Stara_budova"),
    CONNECTING_CORRIDOR("spojovacia_chodba"),
    LIBRARY("kniznica"),
    COURTYARD("dvor"),
    LECTURE_ROOM("Vencovskeho_aula"),
    STREET("ulica");

    private final String roomName;
    private static final Map<String, String> ROOM_NAMES = new HashMap<>();

    static {
        for (RoomName r : values()) {
            ROOM_NAMES.put(r.toString(), r.roomName);
        }
    }

    RoomName(final String roomName) {
        this.roomName = roomName;
    }

    public static String getRoomName(String roomName) {
        return ROOM_NAMES.get(roomName);
    }
}
