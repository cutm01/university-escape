package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Examine item command implementation, player can use this command to examine item from his
 * inventory or from actual room in more detail.<br> Player can find hidden items this way
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class ExamineItemCommand implements Command {

    private static final String NAME = CommandName.EXAMINE_ITEM.getCommandName();
    private static final String DESCRIPTION = CommandDescription.EXAMINE_ITEM
        .getCommandDescription();
    private final GamePlan gamePlan;

    public ExamineItemCommand(GamePlan gamePlan) {
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
     * Method takes max. 1 name of item which is currently in player's inventory or actual room,
     * examine this item and return found hidden items (e.g. item "coat" can hide items such as
     * "wallet" or "keys")<br> Found items are transferred to room's items and player can take them
     * later
     *
     * @param commandParameters name of item player wants to examine
     * @return String with description about found hidden items
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length == 0) {
            return "Nerozumiem ti! Upresni, prosím ťa, ktorý predmet z miestnosti alebo svojho batohu chceš preskúmať";
        }

        if (commandParameters.length > 1) {
            return "Situácia je vážna, no aj tak by bolo fajn zachovať si chladnú hlavu a nepreskúmavať viac predmetov naraz!";
        }

        String itemToExamineName = commandParameters[0];
        // examine item from player's inventory
        boolean isItemInInventory =
            gamePlan.getPlayer().getInventory().isItemInInventory(itemToExamineName);
        if (isItemInInventory) {
            boolean wasItemAlreadyExamined =
                gamePlan
                    .getPlayer()
                    .getInventory()
                    .getItemByName(itemToExamineName)
                    .getWasItemAlreadyExamined();
            if (!wasItemAlreadyExamined) {
                return examineItemFromInventory(itemToExamineName);
            }
            return "Tento predmet si už preskúmal v minulosti, nič nového, čo by ti pomohlo na ňom nenájdeš!";
        }

        // player has firstly look around the room to ,,unlock" interaction with items
        boolean wasRoomAlreadyExamined = gamePlan.getActualRoom().getWasRoomAlreadyExamined();
        if (!wasRoomAlreadyExamined) {
            return "Kde mám ten predmet hľadať? Asi by bolo fajn sa najprv poriadne rozhliadnuť po miestnosti";
        }

        // examine item located in room
        boolean isItemInRoom = gamePlan.getActualRoom().isItemInRoom(itemToExamineName);
        if (isItemInRoom) {
            boolean wasItemAlreadyExamined =
                gamePlan.getActualRoom().getItemByName(itemToExamineName)
                    .getWasItemAlreadyExamined();
            if (!wasItemAlreadyExamined) {
                return examineItemFromRoom(itemToExamineName);
            }
            return "Tento predmet si už preskúmal v minulosti, nič nového, čo by ti pomohlo na ňom nenájdeš!";
        }

        return "Nič také tu nevidím, nemôže byť ten predmet schovaný v rámci nejakého objektu, ktorý si ešte nepreskúmal?";
    }

    /**
     * Method examines item from player's inventory and informs him about found hidden items, if
     * there are any<br> Examined item is marked as "wasAlreadyExamined == true" to avoid future
     * calls to this method, found items are transferred to room's items and player can take them
     * later
     *
     * @param itemToExamineName name of item player wants to examine
     * @return String which informs player about result of item examination (i.e. if any hidden
     * items was found)
     */
    private String examineItemFromInventory(String itemToExamineName) {
        Item itemToExamine = gamePlan.getPlayer().getInventory().getItemByName(itemToExamineName);

        if (itemToExamine.getHiddenItems() == null
            || itemToExamine.getHiddenItems().getNumberOfHiddenItems() == 0) {
            gamePlan
                .getPlayer()
                .getInventory()
                .getItemByName(itemToExamineName)
                .setWasItemAlreadyExamined(true);
            return "V rýchlosti prezeráš predmet "
                   + itemToExamineName
                   + ", no nič zaujímavého na ňom nevidíš";
        }

        String foundItemsInfo =
            "S trasúcimi rukami si prezeráš predmet "
            + itemToExamineName
            + " a podarilo sa ti nájsť nasledovné veci (váha predmetu):\n"
            + itemToExamine.getHiddenItems().getHiddenItemsDescription()
            + "\nAle nie! Vyzeráš byť z toho všetkého v značnom strese a veci, ktoré si práve našiel, ti popadali na zem\n"
            + "a teraz sa povaľujú všade po miestnosti";
        itemToExamine.getHiddenItems()
            .moveHiddenItemsFromInventoryToRoom(gamePlan, itemToExamineName);

        gamePlan
            .getPlayer()
            .getInventory()
            .getItemByName(itemToExamineName)
            .setWasItemAlreadyExamined(true);
        return foundItemsInfo;
    }

    /**
     * Method examines item from actual room and informs player about found hidden items, if there
     * are any<br> Examined item is marked as "wasAlreadyExamined == true" to avoid future calls to
     * this method, found items are transferred to room's items and player can take them later
     *
     * @param itemToExamineName name of item player wants to examine
     * @return String which informs player about result of item examination (i.e. if any hidden
     * items was found)
     */
    private String examineItemFromRoom(String itemToExamineName) {
        Item itemToExamine = gamePlan.getActualRoom().getItemByName(itemToExamineName);

        if (itemToExamine.getHiddenItems() == null
            || itemToExamine.getHiddenItems().getNumberOfHiddenItems() == 0) {
            gamePlan.getActualRoom().getItemByName(itemToExamineName)
                .setWasItemAlreadyExamined(true);
            return "V rýchlosti prezeráš predmet "
                   + itemToExamineName
                   + ", no nič zaujímavého na ňom nevidíš";
        }

        String foundItemsInfo =
            "S trasúcimi rukami si prezeráš predmet "
            + itemToExamineName
            + " a podarilo sa ti nájsť nasledovné veci (váha predmetu):\n"
            + itemToExamine.getHiddenItems().getHiddenItemsDescription()
            + "\nAle nie! Vyzeráš byť z toho všetkého v značnom strese a veci, ktoré si práve našiel, ti popadali na zem\n"
            + "a teraz sa povaľujú všade po miestnosti";
        itemToExamine.getHiddenItems().moveHiddenItemsFromItemToRoom(gamePlan, itemToExamineName);

        gamePlan.getActualRoom().getItemByName(itemToExamineName).setWasItemAlreadyExamined(true);
        return foundItemsInfo;
    }
}
