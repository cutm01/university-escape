package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import cz.vse.java.cutm01.adventure.ui.ItemNameToDisplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Inventory class represent player's inventory (bag) and provides methods used to manipulate with
 * it during the game (e.q. add/remove item, getItemByName,etc.)
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public class Inventory {

    private final Map<String, Item> items;
    private int inventoryCapacity;
    private int inventoryWeight;
    private ObservableList<String> itemsInPlayerInventoryObservableList = FXCollections.observableArrayList();

    // region Constructor
    // --------------------------------------------------------------------------------
    public Inventory() {
        this.items = new LinkedHashMap<>();
        this.inventoryCapacity = 35;
        this.inventoryWeight = 0;
    }
    // --------------------------------------------------------------------------------
    // endregion Constructor

    // region Getters and Setters
    // --------------------------------------------------------------------------------
    /**
     * Method returns ObservableList which is used in game's GUI to handle changes in main scene
     * @return ObservableList containing all items which are currently in player's inventory
     */
    public ObservableList<String> getItemsInPlayerInventoryObservableList() {
        return itemsInPlayerInventoryObservableList;
    }

    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    public void setInventoryCapacity(int inventoryCapacity) {
        this.inventoryCapacity = inventoryCapacity;
    }

    public int getInventoryWeight() {
        return inventoryWeight;
    }

    public void setInventoryWeight(int inventoryWeight) {
        this.inventoryWeight = inventoryWeight;
    }
    // --------------------------------------------------------------------------------
    // endregion Getters and Setters

    // region Methods related to items
    // --------------------------------------------------------------------------------
    public Set<String> getInventoryItemsNames() {
        return items.keySet();
    }

    public boolean isItemInInventory(String itemName) {
        return items.containsKey(itemName);
    }

    /**
     * Method returns Item instance from player's inventory by its name
     * @param itemName name of item whose instance we want to obtain from player's inventory
     * @return Item instance with name which was used to call this method
     */
    public Item getItemByName(String itemName) {
        if (!isItemInInventory(itemName) || itemName == null) {
            return null;
        }

        return items.get(itemName);
    }

    /**
     * Method adds item to player's inventory and update inventory actual weight
     * @param item Item instance we want to add to player's inventory
     */
    public void addItemToInventory(Item item) {
        if (item != null) {
            items.put(item.getName(), item);
            inventoryWeight += item.getWeight();

            String itemNameToDisplay = ItemNameToDisplay.getItemNameToDisplay(ItemName.getEnumValueForItemName(item.getName()));
            itemsInPlayerInventoryObservableList.add(itemNameToDisplay);
        }
    }

    /**
     * Method removes item from player's inventory and update inventory actual weight
     * @param itemName name of item we want to remove from player's inventory
     */
    public void removeItemFromInventory(String itemName) {
        if (itemName != null && isItemInInventory(itemName)) {
            inventoryWeight -= items.get(itemName).getWeight();
            items.remove(itemName);

            String itemNameToDisplay = ItemNameToDisplay.getItemNameToDisplay(ItemName.getEnumValueForItemName(itemName));
            itemsInPlayerInventoryObservableList.remove(itemNameToDisplay);
        }
    }

    /**
     * Method return item name with its weight as one String in following format:<br>
     * itemName(itemWeight)
     *
     * @param itemName name of item which we want obtain its name and weight from
     * @return item name and its weight
     */
    public String getItemWithItsWeight(String itemName) {
        if (!isItemInInventory(itemName) || itemName == null) {
            return null;
        }

        return items.get(itemName).getItemWithItsWeight();
    }

    /**
     * Methods return information about player's inventory, i.e list of items with their weights,
     * actual weight of inventory and maximal capacity of inventory
     *
     * @return String representing detailed information about player's inventory
     */
    public String getInventoryContent() {
        if (items.size() == 0) {
            return "V batohu sa aktuálne nič nenachádza";
        }

        return "V batohu máš nasledovné predmety (váha predmetu):"
               + SystemInfo.LINE_SEPARATOR
               + getItemNamesAndWeights()
               + SystemInfo.LINE_SEPARATOR
               + "Aktuálna váha batohu je "
               + inventoryWeight
               + ", maximálna kapacita je "
               + inventoryCapacity;
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
    private String getItemNamesAndWeights() {
        StringBuilder text = new StringBuilder();
        Iterator<String> itemIterator = items.keySet().iterator();

        int itemsPerLine = 4;
        Item item;
        while (itemIterator.hasNext()) {
            item = items.get(itemIterator.next());
            text.append(item.getItemWithItsWeight());

            if (itemIterator.hasNext()) {
                text.append(", ");
            }

            // max. number of words per line of text reached
            if (itemsPerLine == 0) {
                text.append(SystemInfo.LINE_SEPARATOR);
                itemsPerLine = 4;
            }
            itemsPerLine--;
        }

        return text.toString();
    }
    // --------------------------------------------------------------------------------
    // endregion Methods related to items
}
