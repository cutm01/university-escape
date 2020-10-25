package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Item class represents game items which player can discover<br> Player can take them and use them
 * to perform special action later in game, they can contain hidden items which player can find by
 * performing ExecuteItem command
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class Item {

    private final String name;
    private final String description;
    // following items will be revealed when player uses ExamineCommand
    private HiddenItems hiddenItems;
    private boolean wasItemAlreadyExamined;
    private final int weight;

    // region Constructor
    // --------------------------------------------------------------------------------
    public Item(String nameDefinedInEnum, HiddenItems hiddenItems) {
        this.name = ItemName.getItemName(nameDefinedInEnum);
        this.description = ItemDescription.getItemDescription(nameDefinedInEnum);
        this.hiddenItems = hiddenItems;
        this.wasItemAlreadyExamined = false;
        this.weight = ItemWeight.getItemWeight(nameDefinedInEnum);
        this.hiddenItems = hiddenItems == null ? new HiddenItems() : hiddenItems;
    }
    // --------------------------------------------------------------------------------
    // region Constructor

    // region Getters and Setters
    // --------------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HiddenItems getHiddenItems() {
        return hiddenItems;
    }

    public boolean wasItemAlreadyExamined() {
        return wasItemAlreadyExamined;
    }

    public void setWasItemAlreadyExamined(boolean wasItemAlreadyExamined) {
        this.wasItemAlreadyExamined = wasItemAlreadyExamined;
    }

    public int getWeight() {
        return weight;
    }
    // --------------------------------------------------------------------------------
    // endregion Getters and Setters

    /**
     * Method return item name with its weight as one String in following format:<br>
     * itemName(itemWeight)
     *
     * @return item name and its weight as formatted String
     */
    public String getItemWithItsWeight() {
        return getName() + "(" + getWeight() + ")";
    }
}
