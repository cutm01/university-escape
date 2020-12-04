package cz.vse.java.cutm01.adventure.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Game items (which player can discover during game) with their name, i.e. Strings which be displayed
 * to player in game GUI
 *
 * @author Cúth Michal (xname: cutm01)
 * @version fall semester 2020/2021
 */
public enum ItemNameToDisplay {
    BOTTLE("Fľaša s vodou"),
    PEN("Pero"),
    WALLET("Vlastná peňaženka"),
    ID_CARD("Občiansky preukaz"),
    MONEY("Malý obnos peňazí"),
    MEDICAL_MASK("Rúško"),
    ROPE("Lano"),
    ISIC("Vlastný ISIC"),
    SMALL_SNACK("Malá bageta"),
    BIG_SNACK("Veľká bageta"),
    FIRE_EXTINGUISHER("Hasiaci prístroj"),
    PROTECTIVE_MEDICAL_SUIT("Celotelový oblek"),
    JACKET("Bunda"),
    MUSIC_CD("CD Helenky Vondráčkovej"),
    BOOK("Kniha algoritmov"),
    STOLEN_WALLET("Cuzdia peňaženka"),
    STOLEN_MONEY("Veľký obnos peňazí"),
    STOLEN_ISIC("Cudzí ISIC"),
    KEYS("Kľúče"),
    PAPER_CLIP("Kancelárska spinka");

    private final String itemNameToDisplay;
    private static final Map<String, String> ITEM_NAMES_TO_DISPLAY = new HashMap<>();

    static {
        for (ItemNameToDisplay i : values()) {
            ITEM_NAMES_TO_DISPLAY.put(i.toString(), i.itemNameToDisplay);
        }
    }

    ItemNameToDisplay(final String itemNameToDisplay) {
        this.itemNameToDisplay = itemNameToDisplay;
    }

    public static String getItemNameToDisplay(String itemNameToDisplay) {
        return ITEM_NAMES_TO_DISPLAY.get(itemNameToDisplay);
    }
}
