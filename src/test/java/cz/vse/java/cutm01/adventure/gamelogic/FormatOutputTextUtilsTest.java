package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatOutputTextUtilsTest {
  private GamePlan gamePlan;
  private Room room;
  private Inventory inventory;

  private static Stream<Arguments> provideGroupsConsistingFromItemInstances() {
    return Stream.of(
        Arguments.of(
            new Item(ItemName.BOTTLE.toString(), null),
            new Item(ItemName.PEN.toString(), null),
            new Item(ItemName.WALLET.toString(), null),
            new Item(ItemName.ID_CARD.toString(), null),
            new Item(ItemName.MONEY.toString(), null),
            new Item(ItemName.MEDICAL_MASK.toString(), null)),
        Arguments.of(
            new Item(ItemName.ROPE.toString(), null),
            new Item(ItemName.ISIC.toString(), null),
            new Item(ItemName.SMALL_SNACK.toString(), null),
            new Item(ItemName.FIRE_EXTINGUISHER.toString(), null),
            new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null),
            new Item(ItemName.MUSIC_CD.toString(), null)),
        Arguments.of(
            new Item(ItemName.BOOK.toString(), null),
            new Item(ItemName.STOLEN_WALLET.toString(), null),
            new Item(ItemName.STOLEN_MONEY.toString(), null),
            new Item(ItemName.STOLEN_ISIC.toString(), null),
            new Item(ItemName.KEYS.toString(), null),
            new Item(ItemName.PAPER_CLIP.toString(), null)));
  }

  @BeforeEach
  void setUp() {
    GameImpl game = new GameImpl();
    gamePlan = game.getGamePlan();
    inventory = gamePlan.getPlayer().getInventory();
    gamePlan
        .getPlayer()
        .getInventory()
        .setInventoryCapacity(
            1000); // to fit all 6 items instances which will be used for test purpose

    // create room which will be used in tests (i.e. it is actual room where player is currently in)
    room = new Room("RB_201", false);
    room.setWasRoomAlreadyExamined(true);
    gamePlan.setActualRoom(room);
  }

  @ParameterizedTest
  @MethodSource("provideGroupsConsistingFromItemInstances")
  @DisplayName("output text for items which will be taken from room, max 4 item names per line")
  void getOutputTextForTakenItems(ArgumentsAccessor itemInstances) {
    List<String> itemsToTakeNames = new ArrayList<>();
    for (int i = 0; i < itemInstances.size(); ++i) {
      room.addItemToRoom((Item) itemInstances.get(i));
      itemsToTakeNames.add(((Item) itemInstances.get(i)).getName());
    }

    String expectedOutput =
        ((Item) itemInstances.get(0)).getName()
            + "("
            + ((Item) itemInstances.get(0)).getWeight()
            + "), "
            + ((Item) itemInstances.get(1)).getName()
            + "("
            + ((Item) itemInstances.get(1)).getWeight()
            + "), "
            + ((Item) itemInstances.get(2)).getName()
            + "("
            + ((Item) itemInstances.get(2)).getWeight()
            + "), "
            + ((Item) itemInstances.get(3)).getName()
            + "("
            + ((Item) itemInstances.get(3)).getWeight()
            + "), "
            + "\n"
            + // max. 4 item names per line +
            ((Item) itemInstances.get(4)).getName()
            + "("
            + ((Item) itemInstances.get(4)).getWeight()
            + "), "
            + ((Item) itemInstances.get(5)).getName()
            + "("
            + ((Item) itemInstances.get(5)).getWeight()
            + ")";
    String actualOutput =
        FormatOutputTextUtils.getOutputTextForTakenItems(gamePlan, itemsToTakeNames, 4);

    assertEquals(expectedOutput, actualOutput);
  }

  @ParameterizedTest
  @MethodSource("provideGroupsConsistingFromItemInstances")
  @DisplayName(
      "output text for items which will be dropped from inventory, max 4 item names per line")
  void getOutputTextForDroppedItems(ArgumentsAccessor itemInstances) {
    List<String> itemsToDropNames = new ArrayList<>();
    for (int i = 0; i < itemInstances.size(); ++i) {
      inventory.addItemToInventory((Item) itemInstances.get(i));
      itemsToDropNames.add(((Item) itemInstances.get(i)).getName());
    }

    String expectedOutput =
        ((Item) itemInstances.get(0)).getName()
            + "("
            + ((Item) itemInstances.get(0)).getWeight()
            + "), "
            + ((Item) itemInstances.get(1)).getName()
            + "("
            + ((Item) itemInstances.get(1)).getWeight()
            + "), "
            + ((Item) itemInstances.get(2)).getName()
            + "("
            + ((Item) itemInstances.get(2)).getWeight()
            + "), "
            + ((Item) itemInstances.get(3)).getName()
            + "("
            + ((Item) itemInstances.get(3)).getWeight()
            + "), "
            + "\n"
            + // max. 4 item names per line +
            ((Item) itemInstances.get(4)).getName()
            + "("
            + ((Item) itemInstances.get(4)).getWeight()
            + "), "
            + ((Item) itemInstances.get(5)).getName()
            + "("
            + ((Item) itemInstances.get(5)).getWeight()
            + ")";
    String actualOutput =
        FormatOutputTextUtils.getOutputTextForDroppedItems(gamePlan, itemsToDropNames, 4);

    assertEquals(expectedOutput, actualOutput);
  }

  @ParameterizedTest
  @MethodSource("provideGroupsConsistingFromItemInstances")
  @DisplayName(
      "output text for non-valid items, i.e. player can not take them or drop them, max 4 item names per line")
  void getOutputTextForNonValidItems(ArgumentsAccessor itemInstances) {
    List<String> nonValidItemsNames = new ArrayList<>();
    // items will be not added to room or inventory, therefore they can not be taken or dropped
    for (int i = 0; i < itemInstances.size(); ++i) {
      nonValidItemsNames.add(((Item) itemInstances.get(i)).getName());
    }

    String expectedOutput =
        ((Item) itemInstances.get(0)).getName()
            + ", "
            + ((Item) itemInstances.get(1)).getName()
            + ", "
            + ((Item) itemInstances.get(2)).getName()
            + ", "
            + ((Item) itemInstances.get(3)).getName()
            + ", "
            + "\n"
            + // max. 4 item names per line +
            ((Item) itemInstances.get(4)).getName()
            + ", "
            + ((Item) itemInstances.get(5)).getName();
    String actualOutput =
        FormatOutputTextUtils.getOutputTextForNonValidItems(nonValidItemsNames, 4);

    assertEquals(expectedOutput, actualOutput);
  }
}
