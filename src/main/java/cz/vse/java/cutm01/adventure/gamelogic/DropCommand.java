package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Drop command implementation, player can use this command to drop one or more items from his
 * inventory
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class DropCommand implements Command {

    private static final String NAME = CommandName.DROP.getCommandName();
    private static final String DESCRIPTION = CommandDescription.DROP.getCommandDescription();
    private final GamePlan gamePlan;

    public DropCommand(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandDescription() {
        return DESCRIPTION;
    }

    /**
     * Method is used to drop one or more items from player inventory, which are added to actual
     * room then and player can take them again later in game
     *
     * @param commandParameters one or more item names which player wants to drop
     * @return item names which were (un)successfully dropped from inventory and actual inventory
     * weight and its capacity
     */
    @Override // TODO: possibly split to several methods
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length == 0) {
            return "Naozaj ti nedokážem čítať myšlienky. Upresni, prosím ťa, ktoré predmety chceš odhodiť";
        }

        /*
         * Filtering commandParameters to find items to drop and non-valid items
         * which cannot be dropped, i.e they are not in inventory or their names
         * were misspelled
         * List of both categories of items will be later displayed to player
         * Output text is formatted and will have max. 4 items per line
         */
        Inventory inventory = gamePlan.getPlayer().getInventory();
        Set<String> itemsToDrop = new HashSet<>(); // i.e. valid ones
        List<String> nonValidItems = new ArrayList<>();

        for (String itemName : commandParameters) {
            if (inventory
                .isItemInInventory(itemName)) { // to prevent drop one SAME item multiple times
                itemsToDrop.add(itemName);
            }
            if (!inventory.isItemInInventory(itemName)) {
                nonValidItems.add(itemName);
            }
        }

        // preparing output String for player with information which items were dropped and which were
        // not
        StringBuilder outputText = new StringBuilder();
        if (itemsToDrop.size() == 0) {
            outputText.append("Zo svojho batohu si neodhodil žiadnu vec!").append(SystemInfo.LINE_SEPARATOR);
        } else {
            outputText.append("Zo svojho batohu si odhodil nasledovné predmety (váha predmetu):").append(SystemInfo.LINE_SEPARATOR);
            outputText.append(
                FormatOutputTextUtils.getOutputTextForDroppedItems(
                    gamePlan, new ArrayList<>(itemsToDrop), 4));
        }

        if (nonValidItems.size() > 0) {
            outputText.append(
                "Tieto veci sa mi nepodarilo nájsť v tvojom batohu, nepomýlil si sa náhodou?").append(SystemInfo.LINE_SEPARATOR);
            outputText
                .append(FormatOutputTextUtils.getOutputTextForNonValidItems(nonValidItems, 4));
            outputText
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Stále môžeš použiť príkaz ")
                .append(CommandName.SHOW_INVENTORY.getCommandName());
            outputText.append(" pre zobrazenie vecí, ktoré máš u seba").append(SystemInfo.LINE_SEPARATOR);
        }

        // removing items from player's inventory and adding them to actual room
        // int droppedWeight = 0; //TODO: check whether inventory weight is updated after items are
        // dropped
        Item item;
        for (String itemName : itemsToDrop) {
            item = gamePlan.getPlayer().getInventory().getItemByName(itemName);
            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemName);
            gamePlan.getActualRoom().addItemToRoom(item);
        }
        int inventoryCapacity = gamePlan.getPlayer().getInventory().getInventoryCapacity();
        outputText.append(SystemInfo.LINE_SEPARATOR).append("Aktuálna kapacita batohu: ");
        outputText.append(inventory.getInventoryWeight()).append("/").append(inventoryCapacity);

        return outputText.toString();
    }
}
