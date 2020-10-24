package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.Iterator;
import java.util.List;

/**
 * Class contains methods used to format output text which will be shown to player, these methods
 * are used TakeCommand and DropCommand classes
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public class FormatOutputTextUtils {

  // region Methods to format text output
  // --------------------------------------------------------------------------------

  /**
   * Method return names and weights of items which will be taken from actual game room<br>
   * Items with their weights are returned as one String, where each entry is separated by comma and
   * max. maxItemNamesPerLine items with their weights are displayed per line of output String
   *
   * @param gamePlan actual state of game plan with actual room to take items from
   * @param validItemsNames list with names of items which will be taken from room
   * @param maxItemNamesPerLine how many item names per line will be printed
   * @return names of items with their weights as formatted String
   */
  public static String getOutputTextForTakenItems(
      GamePlan gamePlan, List<String> validItemsNames, int maxItemNamesPerLine) {
    if (validItemsNames == null || validItemsNames.size() == 0) {
      return null;
    }

    Room actualGameRoom = gamePlan.getActualRoom();
    StringBuilder outputText = new StringBuilder();
    int itemNamesPerLine = maxItemNamesPerLine;
    Iterator<String> itemIterator = validItemsNames.iterator();

    while (itemIterator.hasNext()) {
      outputText.append(actualGameRoom.getItemByName(itemIterator.next()).getItemWithItsWeight());

      if (itemIterator.hasNext()) {
        outputText.append(", ");
      }

      itemNamesPerLine--;

      // max. number of items per line of output text reached
      // second condition ensure that empty line will not be printed if
      // there are no more item names to print
      if (itemNamesPerLine == 0 && itemIterator.hasNext()) {
        outputText.append("\n");
        itemNamesPerLine = maxItemNamesPerLine;
      }
    }

    return outputText.toString();
  }

  /**
   * Method return names and weights of items which will be drooped from player inventory<br>
   * Items with their weights are returned as one String, where each entry is separated by comma and
   * max. maxItemNamesPerLine items with their weights are displayed per line of output String
   *
   * @param gamePlan actual state of game plan containing inventory to drop items from
   * @param validItemsNames list with names of items which will be dropped from inventory
   * @param maxItemNamesPerLine how many item names per line will be printed
   * @return names of items with their weights as formatted String
   */
  public static String getOutputTextForDroppedItems(
      GamePlan gamePlan, List<String> validItemsNames, int maxItemNamesPerLine) {
    if (validItemsNames == null || validItemsNames.size() == 0) {
      return null;
    }

    Inventory inventory = gamePlan.getPlayer().getInventory();
    StringBuilder outputText = new StringBuilder();
    int itemNamesPerLine = maxItemNamesPerLine;
    Iterator<String> itemIterator = validItemsNames.iterator();

    while (itemIterator.hasNext()) {
      outputText.append(inventory.getItemWithItsWeight(itemIterator.next()));

      if (itemIterator.hasNext()) {
        outputText.append(", ");
      }

      itemNamesPerLine--;

      // max. number of items per line of output text reached
      // second condition ensure that empty line will not be printed if
      // there are no more item names to print
      if (itemNamesPerLine == 0 && itemIterator.hasNext()) {
        outputText.append("\n");
        itemNamesPerLine = maxItemNamesPerLine;
      }
    }

    return outputText.toString();
  }

  /**
   * Method return names of non-valid items which cannot be taken from room or dropped from player's
   * inventory, e.g. item's name was misspelled or item is not in player's inventory<br>
   * Items names are returned as one String, where each entry is separated by comma and max.
   * maxItemNamesPerLine items are displayed per line of String
   *
   * @param nonValidItemsNames list with names of non-valid items which cannot be dropped or taken
   * @param maxItemNamesPerLine how many item names per line will be printed
   * @return names of non-valid items as formatted String
   */
  public static String getOutputTextForNonValidItems(
      List<String> nonValidItemsNames, int maxItemNamesPerLine) {
    if (nonValidItemsNames == null || nonValidItemsNames.size() == 0) {
      return null;
    }

    StringBuilder outputText = new StringBuilder();
    int itemNamesPerLine = maxItemNamesPerLine;
    Iterator<String> itemIterator = nonValidItemsNames.iterator();

    while (itemIterator.hasNext()) {
      outputText.append(itemIterator.next());

      if (itemIterator.hasNext()) {
        outputText.append(", ");
      }

      itemNamesPerLine--;

      // max. number of items per line of output text reached
      // second condition ensure that empty line will not be printed if
      // there are no more item names to print
      if (itemNamesPerLine == 0 && itemIterator.hasNext()) {
        outputText.append("\n");
        itemNamesPerLine = maxItemNamesPerLine;
      }
    }

    return outputText.toString();
  }
  // --------------------------------------------------------------------------------
  // endregion Methods to format text output
}
