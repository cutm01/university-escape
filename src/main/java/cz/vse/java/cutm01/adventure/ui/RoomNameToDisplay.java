package cz.vse.java.cutm01.adventure.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Game rooms with their name, i.e. Strings which will be displayed
 * to player in game GUI after he uses button to show whole map
 * in pop-up Alert dialog window
 *
 * @author Cúth Michal (xname: cutm01)
 * @version fall semester 2020/2021
 */
public enum RoomNameToDisplay {
    RB_201("RB 201"),
    RB_202("RB 202"),
    FIRST_FLOOR_HALL("Chodba na I. poschodí"),
    SECOND_FLOOR_HALL("Chodba na II. poschodí"),
    TOILETS("Toalety"),
    OFFICE("Kancelária"),
    DRESSING_ROOM("Šatňa"),
    NEW_BUILDING("Nová budova"),
    OLD_BUILDING("Stará budova"),
    CONNECTING_CORRIDOR("Spojovacia chodba"),
    LIBRARY("Knižnica"),
    COURTYARD("Dvor"),
    LECTURE_ROOM("Vencovského aula"),
    STREET("Ulica");

    private final String roomNameToDisplay;
    private static final Map<String, String> ROOM_NAMES_TO_DISPLAY = new HashMap<>();

    static {
        for (RoomNameToDisplay r : values()) {
            ROOM_NAMES_TO_DISPLAY.put(r.toString(), r.roomNameToDisplay);
        }
    }

    RoomNameToDisplay(final String roomNameToDislay) {
        this.roomNameToDisplay = roomNameToDislay;
    }

    public static String getRoomNameToDisplay(String roomName) {
        return ROOM_NAMES_TO_DISPLAY.get(roomName);
    }
}
