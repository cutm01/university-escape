package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InteractableObject class represents object such as desk which player can interact with<br> They
 * can contain hidden items or perform special action after player uses specific item while standing
 * next to them
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class InteractableObject {

    private String name;
    private HiddenItems hiddenItems;
    private boolean wasInteractableObjectAlreadyExamined;
    // each item performs different actions (e.g. unlock game room, give player extra items, etc.)
    private Map<String, String> itemsWithActionTheyPerform;
    private Map<String, String> roomToUnlockAfterItemUsage;
    private Map<String, List<Item>> itemsToAddToRoomAfterItemUsage;
    // each item shows different output text after player uses it
    private Map<String, String> outputTextAfterItemUsage;

    InteractableObject() {
    }

    public InteractableObject(String nameDefinedInEnum, HiddenItems hiddenItems) {
        this.name = InteractableObjectName.getInteractableObjectName(nameDefinedInEnum);

        this.hiddenItems = hiddenItems == null ? new HiddenItems() : hiddenItems;
        this.wasInteractableObjectAlreadyExamined = false;
        this.itemsWithActionTheyPerform = new HashMap<>();
        this.roomToUnlockAfterItemUsage = new HashMap<>();
        this.itemsToAddToRoomAfterItemUsage = new HashMap<>();
        this.outputTextAfterItemUsage = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public boolean wasInteractableObjectAlreadyExamined() {
        return wasInteractableObjectAlreadyExamined;
    }

    public void setWasInteractableObjectAlreadyExamined(
        boolean wasInteractableObjectAlreadyExamined) {
        this.wasInteractableObjectAlreadyExamined = wasInteractableObjectAlreadyExamined;
    }

    public HiddenItems getHiddenItems() {
        return hiddenItems;
    }

    public void setHiddenItems(HiddenItems hiddenItems) {
        this.hiddenItems = hiddenItems;
    }

    /**
     * Methods sets all necessary data which are needed to perform special action with interactable
     * object
     *
     * @param itemsWithActionTheyPerform     item name with action type which it performs (e.q. keys
     *                                       -> UNLOCK_DOOR_TO_ROOM)
     * @param roomToUnlockAfterItemUsage     item name with name of room which will be opened after
     *                                       item usage
     * @param itemsToAddToRoomAfterItemUsage item name with list of items which will be added to
     *                                       room after player uses this item
     * @param outputTextAfterItemUsage       item name with output text, this text will inform
     *                                       player about performed action (e.q. "You just unlocked
     *                                       door to...")
     */
    public void setSpecialActions(
        Map<String, String> itemsWithActionTheyPerform,
        Map<String, String> roomToUnlockAfterItemUsage,
        Map<String, List<Item>> itemsToAddToRoomAfterItemUsage,
        Map<String, String> outputTextAfterItemUsage) {
        this.itemsWithActionTheyPerform = itemsWithActionTheyPerform;
        this.roomToUnlockAfterItemUsage = roomToUnlockAfterItemUsage;
        this.itemsToAddToRoomAfterItemUsage = itemsToAddToRoomAfterItemUsage;
        this.outputTextAfterItemUsage = outputTextAfterItemUsage;
    }

    /**
     * Method checks if player's item can be used to perform special action with actual
     * InteractableObject the player stands nearby (e.g. unlock door to another room, give extra
     * items to player inventory)
     *
     * @param itemName name of item which player wants to use
     * @param gamePlan actual state of game plan to perform changes on
     * @return String which informs player about what happened after he has used given item
     */
    public String performSpecialAction(String itemName, GamePlan gamePlan) {
        String outputText;
        String actionType;
        if (itemsWithActionTheyPerform == null
            || itemsWithActionTheyPerform.get(itemName) == null) {
            actionType = "";
        } else {
            actionType = itemsWithActionTheyPerform.get(itemName);
        }
        switch (actionType) {
            case SpecialActions.UNLOCK_DOOR_TO_ROOM_ACTION_TYPE:
                outputText = unlockDoorToRoom(itemName, gamePlan);
                break;
            case SpecialActions.ADD_ITEMS_TO_ROOM_ACTION_TYPE:
                outputText = addItemsToRoom(itemName, gamePlan);
                break;
            // player will loose item from his inventory, but performed action will not really help him
            // with game progress
            case SpecialActions.USELESS_ACTION_TYPE:
                outputText = performUselessAction(itemName, gamePlan);
                break;
            default:
                outputText = "Prepáč, nenapadá mi žiadny spôsob akým by šlo tento predmet využiť";
        }

        return outputText;
    }

    /**
     * Method unlock door to another room and removes item which was used to unlock door from player
     * inventory
     *
     * @param itemName name of item which player used to unlock door
     * @param gamePlan actual state of game plan to perform changes on
     * @return String which informs player which room was unlocked
     */
    private String unlockDoorToRoom(String itemName, GamePlan gamePlan) {
        StringBuilder outputText = new StringBuilder();
        String roomToUnlock = roomToUnlockAfterItemUsage.get(itemName);
        if (roomToUnlock != null) {
            if (!roomToUnlock.equals(RoomName.getRoomName("RB_202"))) {
                gamePlan.getActualRoom().getNeighboringRoomByName(roomToUnlock)
                    .setIsRoomLocked(false);
            }
            // special case when giving book to IT admin unlocks room RB_202, which is not neighboring
            // room for Office, where IT admin sits
            // so it's little bit hard coded over here :)
            else {
                gamePlan
                    .getActualRoom()
                    .getNeighboringRoomByName(RoomName.getRoomName("FIRST_FLOOR_HALL"))
                    .getNeighboringRoomByName(RoomName.getRoomName("SECOND_FLOOR_HALL"))
                    .getNeighboringRoomByName(RoomName.getRoomName("RB_202"))
                    .setIsRoomLocked(false);
            }

            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemName);

            outputText.append(outputTextAfterItemUsage.get(itemName));
            outputText
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Prichádzaš síce o predmet ").append(itemName).append(", ");
            outputText
                .append("avšak teraz ti už nič nebráni prejsť do nasledovnej miestnosti: ")
                .append(roomToUnlock);

            return outputText.toString();
        }

        return "Hm, vyzerá to, že sa niečo pokazilo. Skús to, prosím, ešte raz";
    }

    /**
     * Method removes item which was used with InteractableObject from player inventory and it in
     * return, it adds some new items to room which player can take and use later
     *
     * @param itemName name of item which player used to interact with InteractableObject he stands
     *                 nearby
     * @param gamePlan actual state of game plan to perform changes on
     * @return String which informs which player about performed action (i.e. which items were added
     * to room)
     */
    private String addItemsToRoom(String itemName, GamePlan gamePlan) {
        StringBuilder outputText = new StringBuilder();
        List<Item> itemsToAdd = itemsToAddToRoomAfterItemUsage.get(itemName);
        if (itemsToAdd != null) {
            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemName);

            StringBuilder itemNames = new StringBuilder();
            for (Item item : itemsToAdd) {
                itemNames.append(item.getName()).append(" ");
                gamePlan.getActualRoom().addItemToRoom(item);
            }

            outputText.append(outputTextAfterItemUsage.get(itemName));
            outputText
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Prichádzaš síce o predmet ").append(itemName).append(", ");
            outputText
                .append("no do miestnosti pribudli nasledovné veci:")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(itemNames.toString())
                .append(SystemInfo.LINE_SEPARATOR);

            return outputText.toString();
        }

        return "Hm, vyzerá to, že sa niečo pokazilo. Skús to, prosím, ešte raz";
    }

    /**
     * Method removes item which was used with InteractableObject from player inventory, but nothing
     * useful (i.e. door unlocking or adding extra items to inventory) will happen
     *
     * @param itemName name of item which player used to interact with InteractableObject he stands
     *                 nearby
     * @param gamePlan actual state of game plan to perform changes on
     * @return String which informs which player about performed action
     */
    private String performUselessAction(String itemName, GamePlan gamePlan) {
        String outputText = outputTextAfterItemUsage.get(itemName);
        if (outputText != null) {
            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemName);
            outputText += SystemInfo.LINE_SEPARATOR
                          + "Predmet " + itemName + " bol nenávratne odobraný z tvojho batohu"
                          + SystemInfo.LINE_SEPARATOR;

            return outputText;
        }

        return "Hm, vyzerá to, že sa niečo pokazilo. Skús to, prosím, ešte raz";
    }
}
