package cz.vse.java.cutm01.adventure.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Game interactable objects (which player can discover during game) with their name, i.e. Strings which will be displayed
 * to player in game GUI
 *
 * @author Cúth Michal (xname: cutm01)
 * @version fall semester 2020/2021
 */
public enum InteractableObjectNameToDisplay {
    BENCH("Lavička"),
    SNACK_VENDING_MACHINE("Automat s jedlom"),
    COAT_HANGER("Vešiak"),
    BOOK_SHELF("Polička s knihami"),
    WINDOW("Okno"),
    DESK("Stôl"),
    LOCKED_DOOR("Zamknuté dvere");

    private final String interactableObjectNameToDisplay;
    private static final Map<String, String> INTERACTABLE_OBJECT_NAMES_TO_DISPLAY = new HashMap<>();
    private static final Map<String, String> ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES = new HashMap<>();

    static {
        for (InteractableObjectNameToDisplay o : values()) {
            INTERACTABLE_OBJECT_NAMES_TO_DISPLAY.put(o.toString(), o.interactableObjectNameToDisplay);
            ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES.put(o.interactableObjectNameToDisplay, o.toString());
        }
    }

    InteractableObjectNameToDisplay(final String interactableObjectNameToDisplay) {
        this.interactableObjectNameToDisplay = interactableObjectNameToDisplay;
    }

    public static String getInteractableObjectNameToDisplay(String interactableObjectNameToDisplay) {
        return INTERACTABLE_OBJECT_NAMES_TO_DISPLAY.get(interactableObjectNameToDisplay);
    }

    public static String getEnumValueForInteractableObjectName(String itemName) {
        return ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES.get(itemName);
    }
}

