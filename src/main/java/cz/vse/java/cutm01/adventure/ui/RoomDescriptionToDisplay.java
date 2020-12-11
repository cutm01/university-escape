package cz.vse.java.cutm01.adventure.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Game room description, i.e. Strings which will be displayed
 * to player in game GUI
 *
 * @author Cúth Michal (xname: cutm01)
 * @version fall semester 2020/2021
 */
public enum RoomDescriptionToDisplay {
    RB_201("Miesto konania tvojich štátnic"),
    RB_202("Obyčajná učebňa akých sú na VŠE desiatky"),
    FIRST_FLOOR_HALL("Okolo seba vidíš samé sutiny a preklínaš neznámeho bombermana"),
    SECOND_FLOOR_HALL("Z výšky pozeráš na to, čo výbuch napáchal"),
    TOILETS("Trochu to tu zapácha, ale na to si si už za tie roky zvykol"),
    OFFICE("Na prvý pohľad to vyzerá, že tu sídli niekto dôležitý"),
    DRESSING_ROOM("Si tu asi prvýkrát vo svojom živote, nikdy predtým si ju totiž nevyužíval"),
    NEW_BUILDING("Veľa z nej po výbuchu nezostalo"),
    OLD_BUILDING("Všetky únikové cesty sú zatarasené sutinami, tadiaľto sa von nedostaneš"),
    CONNECTING_CORRIDOR("Táto chodba spája Stará a Novú budovu tvojej školy"),
    LIBRARY("Kebyže sem chodíš počas štúdia častejšie, možno by si aj skončil s červeným diplomom"),
    COURTYARD("Vyzerá to, že od úniku z areálu školy ťa delí už iba malinký krôčik"),
    LECTURE_ROOM("Spomínaš si na nekonečné hodiny, ktoré si tu počas svojho štúdia strávil"),
    STREET("Všade okolo seba vidíš záchranné zložky a svojich spolužiakov");

    private final String roomDescriptionToDisplay;
    private static final Map<String, String> ROOM_DESCRIPTIONS_TO_DISPLAY = new HashMap<>();

    static {
        for (RoomDescriptionToDisplay r : values()) {
            ROOM_DESCRIPTIONS_TO_DISPLAY.put(r.toString(), r.roomDescriptionToDisplay);
        }
    }

    RoomDescriptionToDisplay(final String roomDescriptionToDisplay) {
        this.roomDescriptionToDisplay = roomDescriptionToDisplay;
    }

    public static String getRoomDescriptionToDisplay(String roomName) {
        return ROOM_DESCRIPTIONS_TO_DISPLAY.get(roomName);
    }
}
