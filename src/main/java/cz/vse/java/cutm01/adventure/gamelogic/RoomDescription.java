package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Game rooms with their descriptions, which are shown to player after he enters this room (if it is
 * not locked)
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum RoomDescription {
    RB_201("miesta konania tvojich štátnic"),
    RB_202("obyčajnej učebne akých sú na VŠE desiatky"),
    FIRST_FLOOR_HALL("okolo seba vidíš samé sutiny a preklínaš neznámeho bombermana"),
    SECOND_FLOOR_HALL("z výšky pozeráš na to, čo výbuch napáchal"),
    TOILETS("trochu to tu zapácha, ale na to si si už za tie roky zvykol"),
    OFFICE("na prvý pohľad to vyzerá, že tu sídli niekto dôležitý"),
    DRESSING_ROOM("asi prvýkrát vo svojom živote, nikdy predtým si ju totiž nevyužíval"),
    NEW_BUILDING("teda resp. v tom čo z nej po výbuchu zostalo"),
    OLD_BUILDING("všetky únikové cesty sú zatarasené sutinami, tadiaľto sa von nedostaneš"),
    CONNECTING_CORRIDOR("ktorá spája Stará a Novú budovu tvojej školy"),
    LIBRARY("kebyže sem chodíš počas štúdia častejšie, možno by si aj skončil s červeným diplomom"),
    COURTYARD("vyzerá to, že od úniku z areálu školy ťa delí už iba malinký krôčik"),
    LECTURE_ROOM("spomínaš si na nekonečné hodiny, ktoré si tu počas svojho štúdia strávil"),
    STREET("všade okolo seba vidíš záchranné zložky a svojich spolužiakov");

    private final String roomDescription;
    private static final Map<String, String> ROOM_DESCRIPTIONS = new HashMap<>();

    static {
        for (RoomDescription r : values()) {
            ROOM_DESCRIPTIONS.put(r.toString(), r.roomDescription);
        }
    }

    RoomDescription(final String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public static String getRoomDescription(String roomName) {
        return ROOM_DESCRIPTIONS.get(roomName);
    }
}
