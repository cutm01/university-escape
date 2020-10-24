package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * RoomSetUpInformation class provides all information for setting up game room, i.e. if room is
 * locked, its exits, interactable objects, items and non-player character
 */
public enum RoomSetUpInformation {
    RB_201(
        false,
        new String[]{"SECOND_FLOOR_HALL"},
        new InteractableObject[]{},
        new Item[]{
            new Item("BOTTLE", null),
            new Item("PEN", null),
            new Item(
                "WALLET",
                new HiddenItems(
                    new Item("ID_CARD", null), new Item("MONEY", null), new Item("ISIC", null)))
        },
        new NonPlayerCharacter[]{}),
    RB_202(
        true,
        new String[]{"SECOND_FLOOR_HALL"},
        new InteractableObject[]{},
        new Item[]{new Item("ROPE", null)},
        new NonPlayerCharacter[]{}),
    FIRST_FLOOR_HALL(
        false,
        new String[]{"TOILETS", "DRESSING_ROOM", "OFFICE", "NEW_BUILDING", "SECOND_FLOOR_HALL"},
        new InteractableObject[]{},
        new Item[]{},
        new NonPlayerCharacter[]{}),
    SECOND_FLOOR_HALL(
        false,
        new String[]{"RB_201", "RB_202", "FIRST_FLOOR_HALL"},
        new InteractableObject[]{
            new InteractableObject("BENCH", new HiddenItems(new Item("MEDICAL_MASK", null)))
        },
        new Item[]{},
        new NonPlayerCharacter[]{}),
    TOILETS(
        false,
        new String[]{"FIRST_FLOOR_HALL"},
        new InteractableObject[]{},
        new Item[]{new Item("FIRE_EXTINGUISHER", null)},
        new NonPlayerCharacter[]{new NonPlayerCharacter("CLEANING_LADY")}),
    OFFICE(
        false,
        new String[]{"FIRST_FLOOR_HALL"},
        new InteractableObject[]{
            new InteractableObject("DESK", new HiddenItems(new Item("PAPER_CLIP", null)))
        },
        new Item[]{},
        new NonPlayerCharacter[]{new NonPlayerCharacter("IT_ADMIN")}),
    DRESSING_ROOM(
        false,
        new String[]{"FIRST_FLOOR_HALL"},
        new InteractableObject[]{
            new InteractableObject(
                "COAT_HANGER",
                new HiddenItems(
                    new Item("JACKET", new HiddenItems(new Item("MUSIC_CD", null))),
                    new Item("PROTECTIVE_MEDICAL_SUIT", null)))
        },
        new Item[]{},
        new NonPlayerCharacter[]{}),
    NEW_BUILDING(
        false,
        new String[]{"FIRST_FLOOR_HALL", "CONNECTING_CORRIDOR", "COURTYARD"},
        new InteractableObject[]{new InteractableObject("LOCKED_DOOR", null)},
        new Item[]{},
        new NonPlayerCharacter[]{}),
    OLD_BUILDING(
        false,
        new String[]{"LIBRARY", "CONNECTING_CORRIDOR"},
        new InteractableObject[]{
            new InteractableObject("SNACK_VENDING_MACHINE", null),
            new InteractableObject("WINDOW", null)
        },
        new Item[]{},
        new NonPlayerCharacter[]{}),
    CONNECTING_CORRIDOR(
        false,
        new String[]{"NEW_BUILDING", "OLD_BUILDING"},
        new InteractableObject[]{},
        new Item[]{},
        new NonPlayerCharacter[]{new NonPlayerCharacter("COUGHING_TEACHER")}),
    LIBRARY(
        false,
        new String[]{"OLD_BUILDING"},
        new InteractableObject[]{
            new InteractableObject("BOOK_SHELF", new HiddenItems(new Item("BOOK", null)))
        },
        new Item[]{},
        new NonPlayerCharacter[]{}),
    COURTYARD(
        true,
        new String[]{"NEW_BUILDING", "LECTURE_ROOM", "STREET"},
        new InteractableObject[]{},
        new Item[]{},
        new NonPlayerCharacter[]{new NonPlayerCharacter("DOOR_KEEPER")}),
    LECTURE_ROOM(
        false,
        new String[]{"COURTYARD"},
        new InteractableObject[]{},
        new Item[]{
            new Item(
                "STOLEN_WALLET",
                new HiddenItems(new Item("STOLEN_MONEY", null), new Item("STOLEN_ISIC", null)))
        },
        new NonPlayerCharacter[]{}),
    STREET(
        true,
        new String[]{},
        new InteractableObject[]{},
        new Item[]{},
        new NonPlayerCharacter[]{});

    private final boolean isRoomLocked;
    private final String[] roomExits;
    private final InteractableObject[] interactableObjectsInRoom;
    private final Item[] itemsInRoom;
    private final NonPlayerCharacter[] nonPlayerCharactersInRoom;

    RoomSetUpInformation(
        boolean isRoomLocked,
        String[] roomExits,
        InteractableObject[] interactableObjectsInRoom,
        Item[] itemsInRoom,
        NonPlayerCharacter[] nonPlayerCharactersInRoom) {
        this.isRoomLocked = isRoomLocked;
        this.roomExits = roomExits;
        this.interactableObjectsInRoom = interactableObjectsInRoom;
        this.itemsInRoom = itemsInRoom;
        this.nonPlayerCharactersInRoom = nonPlayerCharactersInRoom;
    }

    public boolean getIsRoomLocked() {
        return isRoomLocked;
    }

    public String[] getRoomExits() {
        return roomExits;
    }

    public InteractableObject[] getInteractableObjectsInRoom() {
        return interactableObjectsInRoom;
    }

    public Item[] getItemsInRoom() {
        return itemsInRoom;
    }

    public NonPlayerCharacter[] getNonPlayerCharactersInRoom() {
        return nonPlayerCharactersInRoom;
    }
}
