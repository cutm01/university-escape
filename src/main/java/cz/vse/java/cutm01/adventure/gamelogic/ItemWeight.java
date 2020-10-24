package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Game items (which player can discover during game) with their weights, i.e. int value
 * representing how much space they will take in player's inventory
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum ItemWeight {
    // light items
    PAPER_CLIP(1),
    PEN(2),
    ID_CARD(2),
    ISIC(2),
    STOLEN_ISIC(2),
    MONEY(3),
    MEDICAL_MASK(3),
    STOLEN_MONEY(4),

    // semi-heavy items
    WALLET(6),
    STOLEN_WALLET(6),
    MUSIC_CD(6),
    SMALL_SNACK(6),
    BOTTLE(7),
    BIG_SNACK(8),
    KEYS(8),

    // heavy items
    BOOK(11),
    ROPE(12),
    PROTECTIVE_MEDICAL_SUIT(14),
    JACKET(18),
    FIRE_EXTINGUISHER(25);

    private final int itemWeight;
    private static final Map<String, Integer> ITEM_WEIGHTS = new HashMap<>();

    static {
        for (ItemWeight i : values()) {
            ITEM_WEIGHTS.put(i.toString(), i.itemWeight);
        }
    }

    ItemWeight(final int itemWeight) {
        this.itemWeight = itemWeight;
    }

    public static int getItemWeight(String itemName) {
        return ITEM_WEIGHTS.get(itemName);
    }
}
