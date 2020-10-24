package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * OverlookItem command implementation, player can use this command to quickly look on item from
 * actual game room or his inventory and get helpful description
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class OverlookItemCommand implements Command {
  private static final String NAME = CommandName.OVERLOOK_ITEM.getCommandName();
  private static final String DESCRIPTION =
      CommandDescription.OVERLOOK_ITEM.getCommandDescription();
  private final GamePlan gamePlan;

  public OverlookItemCommand(GamePlan gamePlan) {
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
   * Method overlook item in room or player's inventory and return its description, which gives
   * player hints how he can use item
   *
   * @param commandParameters name of item player wants to examine
   * @return String with item description containing useful hints
   */
  @Override
  public String executeCommand(String... commandParameters) {
    if (commandParameters.length == 0) {
      return "Špecifikuj, prosím, jeden predmet z miestnosti alebo svojho batohu, ktorý si chceš prehliadnuť";
    }

    if (commandParameters.length > 1) {
      return "Spomaľ trochu, naraz viem pracovať iba s jedným predmetom z miestnosti alebo tvojho batohu";
    }

    String itemToOverlook = commandParameters[0];
    boolean isItemInInventory =
        gamePlan.getPlayer().getInventory().isItemInInventory(itemToOverlook);
    if (isItemInInventory) {
      return gamePlan.getPlayer().getInventory().getItemByName(itemToOverlook).getDescription();
    }

    boolean wasRoomAlreadyExamined = gamePlan.getActualRoom().getWasRoomAlreadyExamined();
    if (!wasRoomAlreadyExamined) {
      return "Kde mám ten predmet hľadať? Asi by bolo fajn sa najprv poriadne rozhliadnuť po miestnosti";
    }

    boolean isItemInRoom = gamePlan.getActualRoom().isItemInRoom(itemToOverlook);
    if (isItemInRoom) {
      return gamePlan.getActualRoom().getItemByName(itemToOverlook).getDescription();
    }

    return "Žiadny taký predmet v miestnosti alebo tvojom batohu nevidím, nepomýlil si sa náhodou?";
  }
}
