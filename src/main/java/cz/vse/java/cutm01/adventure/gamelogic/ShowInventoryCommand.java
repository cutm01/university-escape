package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

/**
 * ShowInventory command implementation, player can use this command to show all items which are
 * currently in his inventory together with inventory info, i.e. actual weight of items in inventory
 * and inventory capacity
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class ShowInventoryCommand implements Command {

    private static final String NAME = CommandName.SHOW_INVENTORY.getCommandName();
    private static final String DESCRIPTION = CommandDescription.SHOW_INVENTORY.getCommandDescription();
    private final GamePlan gamePlan;

    public ShowInventoryCommand(GamePlan gamePlan) {
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
     * Method shows iventory items together with their weights
     * @param commandParameters this command should be run without any parameter
     * @return String representing inventory content (i.e. name of items followed by their weight)
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length > 0) {
            return "Chceš ukázať obsah batohu, pochopil som to správne? Nech sa ti páči,"
                   + SystemInfo.LINE_SEPARATOR
                   + "a nabudúce to skús, prosím ťa, bez tých zbytočných rečí okolo:"
                   + SystemInfo.LINE_SEPARATOR
                   + gamePlan.getPlayer().getInventory().getInventoryContent();
        }

        return gamePlan.getPlayer().getInventory().getInventoryContent();
    }
}
