package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * HiddenItems class represents list of game items which can be hidden "in" another game item or
 * interactable object<br> Player can find these hidden items by performing ExecuteItem or
 * ExecuteObject commands
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class HiddenItems {

    private final Map<String, Item> hiddenItems;

    public HiddenItems(Item... hiddenItems) {
        this.hiddenItems = new LinkedHashMap<>();
        for (Item item : hiddenItems) {
            this.hiddenItems.put(item.getName(), item);
        }
    }

    public int getNumberOfHiddenItems() {
        return hiddenItems.size();
    }

    public void removeHiddenItem(Item itemToRemove) {
        hiddenItems.remove(itemToRemove.getName());
    }

    public List<Item> getAllHiddenItems() {
        return new LinkedList<>(this.hiddenItems.values());
    }

    public String getHiddenItemsDescription() {
        if (getNumberOfHiddenItems() == 0) {
            return null;
        }

        return getHiddenItemNamesAndWeights();
    }

    /**
     * Method takes item from actual room, found all items which it hides and move these found items
     * between items which are already in room<br> (e.g. takes item "bag" from room, finds hidden
     * item "pen" and then moves this item between other items which are already in room)
     *
     * @param gamePlan         actual state of game plan to perform changes on
     * @param examinedItemName name of item from actual room which player examined
     */
    public void moveHiddenItemsFromItemToRoom(GamePlan gamePlan, String examinedItemName) {
        Item examinedItem = gamePlan.getActualRoom().getItemByName(examinedItemName);
        List<Item> itemsToMove = examinedItem.getHiddenItems().getAllHiddenItems();

        for (Item itemToTransfer : itemsToMove) {
            gamePlan.getActualRoom().addItemToRoom(itemToTransfer);
            examinedItem.getHiddenItems().removeHiddenItem(itemToTransfer);
        }
    }

    /**
     * Method takes item from player's inventory, found all items which it hides and move these
     * found items between items which are already in room<br> (e.g. takes item "wallet" from
     * player's inventory, find hidden item "money" and then moves this item between other items
     * which are already in room)
     *
     * @param gamePlan         actual state of game plan to perform changes on
     * @param examinedItemName name of item from inventory which player examined
     */
    public void moveHiddenItemsFromInventoryToRoom(GamePlan gamePlan, String examinedItemName) {
        Item examinedItem = gamePlan.getPlayer().getInventory().getItemByName(examinedItemName);
        List<Item> itemsToMove = examinedItem.getHiddenItems().getAllHiddenItems();

        for (Item itemToTransfer : itemsToMove) {
            gamePlan.getActualRoom().addItemToRoom(itemToTransfer);
            examinedItem.getHiddenItems().removeHiddenItem(itemToTransfer);
        }
    }

    /**
     * Method takes interactable object from room, found all items which it hides and move these
     * found items between items which are already in room<br> (e.g. takes object "desk" from actual
     * room, find hidden item "screwdriver" and then moves this item between other items which are
     * already in room)
     *
     * @param gamePlan           actual state of game plan to perform changes on
     * @param examinedObjectName name of interactable object from actual room which player examined
     */
    public void moveHiddenItemsFromObjectToRoom(GamePlan gamePlan, String examinedObjectName) {
        InteractableObject examinedObject =
            gamePlan.getActualRoom().getInteractableObjectByName(examinedObjectName);
        List<Item> itemsToMove = examinedObject.getHiddenItems().getAllHiddenItems();

        for (Item itemToTransfer : itemsToMove) {
            gamePlan.getActualRoom().addItemToRoom(itemToTransfer);
            examinedObject.getHiddenItems().removeHiddenItem(itemToTransfer);
        }
    }

    /**
     * Method return names of all items inside the player's inventory with weight for each one of
     * them
     * <br>
     * Items with their weights are returned as one String, where each entry is separated by comma
     * and max. 4 items with their weight are displayed per line of String
     *
     * @return names of items with their weights as formatted String
     */
    private String getHiddenItemNamesAndWeights() {
        StringBuilder text = new StringBuilder();
        Iterator<String> itemIterator = hiddenItems.keySet().iterator();

        int itemsPerLine = 4;
        Item item;
        while (itemIterator.hasNext()) {
            item = hiddenItems.get(itemIterator.next());
            text.append(item.getItemWithItsWeight());

            if (itemIterator.hasNext()) {
                text.append(", ");
            }

            // max. number of words per line of text reached
            if (itemsPerLine == 0) {
                text.append("\n");
                itemsPerLine = 4;
            }
            itemsPerLine--;
        }

        return text.toString();
    }
}
