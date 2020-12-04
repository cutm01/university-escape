package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Interactable object (which player can discover during game) with their name, i.e. Strings which
 * player has to type in order to approach them
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum InteractableObjectName {
    BENCH("lavicka"),
    SNACK_VENDING_MACHINE("automat_s_jedlom"),
    COAT_HANGER("vesiak"),
    BOOK_SHELF("policka_s_knihami"),
    WINDOW("okno"),
    DESK("stol"),
    LOCKED_DOOR("zamknute_dvere");

    private final String interactableObjectName;
    private static final Map<String, String> INTERACTABLE_OBJECTS_NAMES = new HashMap<>();
    private static final Map<String, String> ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES = new HashMap<>();

    static {
        for (InteractableObjectName o : values()) {
            INTERACTABLE_OBJECTS_NAMES.put(o.toString(), o.interactableObjectName);
            ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES.put(o.interactableObjectName, o.toString());
        }
    }

    InteractableObjectName(final String interactableObjectName) {
        this.interactableObjectName = interactableObjectName;
    }

    public static String getInteractableObjectName(String objectName) {
        return INTERACTABLE_OBJECTS_NAMES.get(objectName);
    }

    public static String getEnumValueForInteractableObjectName(String interactableObjectName) {
        return ENUM_VALUES_FOR_INTERACTABLE_OBJECT_NAMES.get(interactableObjectName);
    }
}
