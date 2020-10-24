package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpecialActions class provides all information about performing special actions with interactable
 * objects on non-player characters<br> This class provides information about which game room will
 * be unlocked, or which items will be added to actual game room after player uses specific item
 * from his inventory while standing next to interactable object or non-player character
 */
public enum SpecialActions {
    BENCH(InteractableObjectName.getInteractableObjectName("BENCH"), null, null, null, null),
    SNACK_VENDING_MACHINE(
        InteractableObjectName.getInteractableObjectName("SNACK_VENDING_MACHINE"),
        new HashMap<>() {
            {
                put(ItemName.getItemName("MONEY"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("STOLEN_MONEY"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
            }
        },
        null, // roomToUnlockAfterItemUsage
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("MONEY"),
                    new ArrayList<>() {
                        {
                            add(new Item("SMALL_SNACK", null));
                        }
                    });
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    new ArrayList<>() {
                        {
                            add(new Item("BIG_SNACK", null));
                        }
                    });
            }
        },
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("MONEY"),
                    "Míňaš posledné svoje peniaze v snahe kúpiť si niečo drobné pod zub");
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    "Vyzerá to, že automat je ešte väčší zlodej ako ty, dal ti síce naspäť bagetu, ale akosi zabudol na výdavok");
            }
        }),
    COAT_HANGER(
        InteractableObjectName.getInteractableObjectName("COAT_HANGER"), null, null, null, null),
    BOOK_SHELF(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"), null, null, null, null),
    WINDOW(InteractableObjectName.getInteractableObjectName("WINDOW"), null, null, null, null),
    DESK(InteractableObjectName.getInteractableObjectName("DESK"), null, null, null, null),
    LOCKED_DOOR(
        InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
        new HashMap<>() {
            {
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("KEYS"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("PAPER_CLIP"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
            }
        },
        new HashMap<>() {
            {
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), RoomName.getRoomName("COURTYARD"));
                put(ItemName.getItemName("KEYS"), RoomName.getRoomName("COURTYARD"));
                put(ItemName.getItemName("PAPER_CLIP"), RoomName.getRoomName("COURTYARD"));
            }
        },
        null, // itemsToAddToRoomAfterItemUsage
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("FIRE_EXTINGUISHER"),
                    "Zúfalá situácia si vyžaduje zúfalé riešenie, zobral si do rúk hasičák a vyrazil si dvere");
                put(
                    ItemName.getItemName("KEYS"),
                    "Podarilo sa ti nájsť ten správny kľúč a odomykáš dvere");
                put(
                    ItemName.getItemName("PAPER_CLIP"),
                    "MacGyver sa môže len ticho na teba pozerať, z kancelárskej spinky si vyrobil šperhák a odomkol si dvere");
            }
        }),
    CLEANING_LADY(
        NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY"),
        new HashMap<>() {
            {
                put(ItemName.getItemName("BOTTLE"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("MONEY"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("STOLEN_MONEY"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("MUSIC_CD"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("SMALL_SNACK"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("BIG_SNACK"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("ROPE"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("PEN"), ADD_ITEMS_TO_ROOM_ACTION_TYPE);
            }
        },
        null, // roomToUnlockAfterItemUsage
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    new ArrayList<>() {
                        {
                            add(new Item("KEYS", null));
                        }
                    });
                put(
                    ItemName.getItemName("MUSIC_CD"),
                    new ArrayList<>() {
                        {
                            add(new Item("KEYS", null));
                        }
                    });
                put(
                    ItemName.getItemName("FIRE_EXTINGUISHER"),
                    new ArrayList<>() {
                        {
                            add(new Item("KEYS", null));
                        }
                    });
                put(
                    ItemName.getItemName("ROPE"),
                    new ArrayList<>() {
                        {
                            add(new Item("KEYS", null));
                        }
                    });
                put(
                    ItemName.getItemName("PEN"),
                    new ArrayList<>() {
                        {
                            add(new Item("KEYS", null));
                        }
                    });
            }
        },
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("BOTTLE"),
                    "Dávaš upratovačke napiť, vyzerá síce, že ti je vďačná, ale nič na oplátku ti nedala");
                put(
                    ItemName.getItemName("MONEY"),
                    "Podávaš upratovačke svojich posledných 30 korún, pozerá na teba ako na blázna. Snáď si nečakal, že ti za ne aj niečo dá");
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    "Upratovačka si starostlivo prepočítava peniažky a usmieva sa... vyzerá to, že ti za ne dáva niečo na oplátku");
                put(
                    ItemName.getItemName("MUSIC_CD"),
                    "Potom čo jej podávaš CD s hitmi Helenky Vondráčkovej sa upratovačka začne usmievať od ucha k uchu.. vyzerá to, že ti zaň dáva niečo na oplátku");
                put(
                    ItemName.getItemName("SMALL_SNACK"),
                    "Hotový altruista! Podávaš síce upratovačke bagetu, no nič za ňu nedostávaš naspäť");
                put(
                    ItemName.getItemName("BIG_SNACK"),
                    "Hotový altruista! Podávaš síce upratovačke bagetu, no nič za ňu nedostávaš naspäť");
                put(
                    ItemName.getItemName("FIRE_EXTINGUISHER"),
                    "Upratovačka sa ani nenazdala a už leží ovalená hasičákom na zemi, pozeráš, či nemá u seba niečo užitočné");
                put(
                    ItemName.getItemName("ROPE"),
                    "Bolo to naozaj nutné? Upratovačku si zviazal a pozeráš, či nemá u seba niečo užitočné");
                put(
                    ItemName.getItemName("PEN"),
                    "Tak to si prehnal! Vrazil si upratovačke pero do nohy a kým sa stihne z toho spamätať, tak jej berieš kľúče");
            }
        }),
    COUGHING_TEACHER(
        NonPlayerCharacterName.getNonPlayerCharacterName("COUGHING_TEACHER"), null, null, null,
        null),
    IT_ADMIN(
        NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
        new HashMap<>() {
            {
                put(ItemName.getItemName("BOTTLE"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("MONEY"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("STOLEN_MONEY"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("MUSIC_CD"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("BOOK"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("SMALL_SNACK"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("BIG_SNACK"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("ROPE"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("PEN"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
            }
        },
        new HashMap<>() {
            {
                put(ItemName.getItemName("BOOK"), RoomName.getRoomName("RB_202"));
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), RoomName.getRoomName("RB_202"));
                put(ItemName.getItemName("ROPE"), RoomName.getRoomName("RB_202"));
                put(ItemName.getItemName("PEN"), RoomName.getRoomName("RB_202"));
            }
        },
        null, // itemsToAddToRoomAfterItemUsage
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("BOTTLE"),
                    "Dávaš IT adminovi napiť, vyzerá síce, že ti je vďačný, ale nič na oplátku ti nedal");
                put(
                    ItemName.getItemName("MONEY"),
                    "Podávaš IT adminovi svojich posledných 30 korún, pozerá na teba ako na blázna. Snáď si nečakal, že ti za ne aj niečo dá");
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    "Ukazuješ IT adminovi veľký obnos peňazí, no ten na teba len pozerá ako na blázna. Nevyzerá ako, že by sa nechal uplatiť");
                put(
                    ItemName.getItemName("MUSIC_CD"),
                    "IT admin si síce zobral CD Helenky Vondráčkovej, no nič na oplátku ti nedal. Asi to nebude jej veľký fanúšik");
                put(
                    ItemName.getItemName("BOOK"),
                    ",,To je presne tá kniha, ktorú potrebujem!\" odpovedá IT admin a vyzerá to, že ti dá niečo na oplátku");
                put(
                    ItemName.getItemName("SMALL_SNACK"),
                    "Hotový altruista! Podávaš síce IT adminovi bagetu, no nič za ňu nedostávaš naspäť");
                put(
                    ItemName.getItemName("BIG_SNACK"),
                    "Hotový altruista! Podávaš síce IT adminovi bagetu, no nič za ňu nedostávaš naspäť");
                put(
                    ItemName.getItemName("FIRE_EXTINGUISHER"),
                    "IT admin sa ani nenazdal a už leží ovalená hasičákom na zemi, pristupuješ k jeho počítaču");
                put(
                    ItemName.getItemName("ROPE"),
                    "Bolo to naozaj nutné? IT admina si zviazal a pristupuješ k jeho počítaču");
                put(
                    ItemName.getItemName("PEN"),
                    "Tak to si prehnal! Vrazil si IT adminovi pero do nohy a kým sa zvíja v bolesti na zemi, ty pristupuješ k jeho počítaču");
            }
        }),
    DOOR_KEEPER(
        NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
        new HashMap<>() {
            {
                put(ItemName.getItemName("BOTTLE"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("MONEY"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("STOLEN_MONEY"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("MUSIC_CD"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("ID_CARD"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("ISIC"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("STOLEN_ISIC"), USELESS_ACTION_TYPE);
                put(ItemName.getItemName("SMALL_SNACK"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("BIG_SNACK"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("ROPE"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
                put(ItemName.getItemName("PEN"), UNLOCK_DOOR_TO_ROOM_ACTION_TYPE);
            }
        },
        new HashMap<>() {
            {
                put(ItemName.getItemName("ID_CARD"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("STOLEN_MONEY"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("SMALL_SNACK"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("BIG_SNACK"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("FIRE_EXTINGUISHER"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("ROPE"), RoomName.getRoomName("STREET"));
                put(ItemName.getItemName("PEN"), RoomName.getRoomName("STREET"));
            }
        },
        null, // itemsToAddToRoomAfterItemUsage
        new HashMap<>() {
            {
                put(
                    ItemName.getItemName("BOTTLE"),
                    "Dávaš vrátnikovi napiť, vyzerá síce, že ti je vďačný, ale von ťa aj tak nepustí");
                put(
                    ItemName.getItemName("MONEY"),
                    "Podávaš vrátnikovi svojich posledných 30 korún, pozerá na teba ako na blázna. Snáď si nečakal, že ti za ne pustí von");
                put(
                    ItemName.getItemName("STOLEN_MONEY"),
                    "Vyzerá, že reči peňazí rozumie vrátnik veľmi dobre");
                put(
                    ItemName.getItemName("MUSIC_CD"),
                    "Vrátnik si síce zobral CD Helenky Vondráčkovej, no von ťa aj tak nepustí. Asi to nebude jej veľký fanúšik\"");
                put(
                    ItemName.getItemName("ID_CARD"),
                    "Ukazuješ vrátnikovi svoj občiansky preukaz a vyzerá to, že ťa pustí von");
                put(
                    ItemName.getItemName("ISIC"),
                    ",,ISIC mi rozhodne stačiť nebude!\" odpovedá vrátnik a odmieta ťa pustiť von");
                put(
                    ItemName.getItemName("STOLEN_ISIC"),
                    ",,Ten ISIC nie je tvoj!\" odpovedá vrátnik a odmieta ťa pustiť von");
                put(
                    ItemName.getItemName("SMALL_SNACK"),
                    "Vrátnikovi sa rozžiarili oči šťastím, asi bol taký mrzutý len preto, že bol hladný. Berie si bagetu a dovolí ti ísť von");
                put(
                    ItemName.getItemName("BIG_SNACK"),
                    "Vrátnikovi sa rozžiarili oči šťastím, asi bol taký mrzutý len preto, že bol hladný. Berie si bagetu a dovolí ti ísť von");
                put(
                    ItemName.getItemName("FIRE_EXTINGUISHER"),
                    "Vrátnik sa ani nenazdal a už leží ovalená hasičákom na zemi a teba tak už nič nebráni v ceste von");
                put(
                    ItemName.getItemName("ROPE"),
                    "Bolo to naozaj nutné? Vrátnika si zviazal a tebe tak už nič nebráni v ceste von");
                put(
                    ItemName.getItemName("PEN"),
                    "Tak to si prehnal! Vrazil si vrátnikovi pero do nohy a ten sa teraz zvíja v bolesti na zemi, každopádne ti už nič nebráni v ceste von");
            }
        });

    // each item performs different special action type when used while standing next to interactable
    // object
    // these actions are specified in SpecialActions enum and later used in InteractableObject class
    static final String UNLOCK_DOOR_TO_ROOM_ACTION_TYPE = "UNLOCK_DOOR_TO_ROOM";
    static final String ADD_ITEMS_TO_ROOM_ACTION_TYPE = "ADD_ITEMS_TO_ROOM";
    static final String USELESS_ACTION_TYPE = "USELESS_ACTION";

    private final String interactableObjectName;
    private final Map<String, String> itemsWithActionTheyPerform;
    private final Map<String, String> roomToUnlockAfterItemUsage;
    private final Map<String, List<Item>> itemsToAddToRoomAfterItemUsage;
    private final Map<String, String>
        outputTextAfterItemUsage; // each item shows different output text after player uses it
    private static final Map<String, SpecialActions>
        OBJECTS_AND_NON_PLAYER_CHARACTERS_WITH_SPECIAL_ACTIONS = new HashMap<>();

    static {
        for (SpecialActions s : values()) {
            OBJECTS_AND_NON_PLAYER_CHARACTERS_WITH_SPECIAL_ACTIONS
                .put(s.getInteractableObjectName(), s);
        }
    }

    SpecialActions(
        String interactableObjectName,
        Map<String, String> itemsWithActionTheyPerform,
        Map<String, String> roomToUnlockAfterItemUsage,
        Map<String, List<Item>> itemsToAddToRoomAfterItemUsage,
        Map<String, String> outputTextAfterItemUsage) {
        this.interactableObjectName = interactableObjectName;
        this.itemsWithActionTheyPerform = itemsWithActionTheyPerform;
        this.roomToUnlockAfterItemUsage = roomToUnlockAfterItemUsage;
        this.itemsToAddToRoomAfterItemUsage = itemsToAddToRoomAfterItemUsage;
        this.outputTextAfterItemUsage = outputTextAfterItemUsage;
    }

    public static Map<String, SpecialActions> getObjectsAndNonPlayerCharactersWithSpecialActions() {
        return OBJECTS_AND_NON_PLAYER_CHARACTERS_WITH_SPECIAL_ACTIONS;
    }

    public String getInteractableObjectName() {
        return interactableObjectName;
    }

    public Map<String, String> getItemsWithActionTheyPerform() {
        return itemsWithActionTheyPerform;
    }

    public Map<String, String> getRoomToUnlockAfterItemUsage() {
        return roomToUnlockAfterItemUsage;
    }

    public Map<String, List<Item>> getItemsToAddToRoomAfterItemUsage() {
        return itemsToAddToRoomAfterItemUsage;
    }

    public Map<String, String> getOutputTextAfterItemUsage() {
        return outputTextAfterItemUsage;
    }
}
