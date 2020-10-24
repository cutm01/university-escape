package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Take command implementation, player can use this command to take items from actual game room and
 * put them into his inventory
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class TakeCommand implements Command {
  private static final String NAME = CommandName.TAKE.getCommandName();
  private static final String DESCRIPTION = CommandDescription.TAKE.getCommandDescription();
  private final GamePlan gamePlan;

  public TakeCommand(GamePlan gamePlan) {
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
   * Method is used to take one or more items from actual game room, which are added to player's
   * inventory
   *
   * @param commandParameters one or more item names which player wants to drop
   * @return item names which were (un)successfully taken from room and actual inventory weight and
   *     its capacity
   */
  @Override // TODO: split it into several methods
  public String executeCommand(String... commandParameters) {
    if (commandParameters.length == 0) {
      return "Hm...nezabudol si na niečo? Napríklad uviesť aspoň 1 predmet, ktorý si chceš z miestnosti vziať";
    }

    /// player has firstly look around the room to find all items he can take
    boolean wasRoomAlreadyExamined = gamePlan.getActualRoom().getWasRoomAlreadyExamined();
    if (!wasRoomAlreadyExamined) {
      return "Na chvíľu spomaľ a poriadne sa rozhliadni po miestnosti, aby si vôbec vedel, aké predmety si môžeš odniesť";
    }

    /*
     * Filtering commandParameters to find items which player can take and
     * which cannot be taken, i.e they are not in room or their names
     * were misspelled
     * List of both categories of items will be later displayed to player
     * Output text is formatted and will have max. 4 items per line
     */
    Room room = gamePlan.getActualRoom();
    List<String> itemsToTake = new ArrayList<>(); // i.e. valid ones
    List<String> nonValidItems = new ArrayList<>();
    int itemsToTakeWeight = 0;

    for (String itemName : commandParameters) {
      if (room.isItemInRoom(itemName)
          && !itemsToTake.contains(itemName)) { // to prevent take one SAME item multiple times
        itemsToTake.add(itemName);
        itemsToTakeWeight += room.getItemByName(itemName).getWeight();
      } else {
        nonValidItems.add(itemName);
      }
    }

    // check if player's inventory can hold all items he wants to take
    int inventoryWeight = gamePlan.getPlayer().getInventory().getInventoryWeight();
    int inventoryCapacity = gamePlan.getPlayer().getInventory().getInventoryCapacity();
    if (inventoryCapacity < (inventoryWeight + itemsToTakeWeight)) {
      return "Toľko vecí do batohu naozaj nedáš! Skús ich zobrať menej alebo najprv nejaké z batohu odhodiť";
    }

    // preparing output String for player with information which items were taken and which were not
    StringBuilder outputText = new StringBuilder();
    if (itemsToTake.size() == 0) {
      outputText.append("Z miestnosti si si nič nevzal!\n");
    } else {
      outputText.append("Do batohu si vkladáš nasledovné predmety (váha predmetu):\n");
      outputText.append(FormatOutputTextUtils.getOutputTextForTakenItems(gamePlan, itemsToTake, 4));
    }

    if (nonValidItems.size() > 0) {
      outputText.append(
          "\nTieto veci sa mi v tejto miestnosti nepodarilo nájsť, nepomýlil si sa náhodou?\n");
      outputText.append(FormatOutputTextUtils.getOutputTextForNonValidItems(nonValidItems, 4));
      outputText
          .append("\nStále môžeš použit príkaz ")
          .append(CommandName.LOOK_AROUND.getCommandName())
          .append(" pre zobrazenie vecí, ktoré sú aktuálne v miestnosti,\n");
      outputText
          .append("prípadne jeden z dvojice príkazov ")
          .append(CommandName.EXAMINE_ITEM.getCommandName())
          .append(", ")
          .append(CommandName.EXAMINE_OBJECT.getCommandName())
          .append(" v snahe nájsť skryté predmety");
    }

    // removing items from room and adding them to player's inventory
    Item item;
    for (String itemName : itemsToTake) {
      item = gamePlan.getActualRoom().getItemByName(itemName);
      gamePlan.getPlayer().getInventory().addItemToInventory(item);
      gamePlan.getActualRoom().removeItemFromRoom(item);
    }

    // change actual inventory weight and inform player about it
    int newInventoryWeight = inventoryWeight + itemsToTakeWeight;
    gamePlan.getPlayer().getInventory().setInventoryWeight(newInventoryWeight);

    outputText.append("\nAktuálna kapacita batohu: ");
    outputText.append(newInventoryWeight).append("/").append(inventoryCapacity);

    return outputText.toString();
  }
}
