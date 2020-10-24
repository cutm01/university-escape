package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {

  private static Stream<Arguments> provideItemInstancesWithTheirWeights() {
    return Stream.of(
        Arguments.of(
            new Item(ItemName.BOTTLE.toString(), null),
            ItemWeight.getItemWeight(ItemName.BOTTLE.toString())),
        Arguments.of(
            new Item(ItemName.PEN.toString(), null),
            ItemWeight.getItemWeight(ItemName.PEN.toString())),
        Arguments.of(
            new Item(ItemName.WALLET.toString(), null),
            ItemWeight.getItemWeight(ItemName.WALLET.toString())),
        Arguments.of(
            new Item(ItemName.ID_CARD.toString(), null),
            ItemWeight.getItemWeight(ItemName.ID_CARD.toString())),
        Arguments.of(
            new Item(ItemName.MONEY.toString(), null),
            ItemWeight.getItemWeight(ItemName.MONEY.toString())),
        Arguments.of(
            new Item(ItemName.MEDICAL_MASK.toString(), null),
            ItemWeight.getItemWeight(ItemName.MEDICAL_MASK.toString())),
        Arguments.of(
            new Item(ItemName.ROPE.toString(), null),
            ItemWeight.getItemWeight(ItemName.ROPE.toString())),
        Arguments.of(
            new Item(ItemName.ISIC.toString(), null),
            ItemWeight.getItemWeight(ItemName.ISIC.toString())),
        Arguments.of(
            new Item(ItemName.SMALL_SNACK.toString(), null),
            ItemWeight.getItemWeight(ItemName.SMALL_SNACK.toString())),
        Arguments.of(
            new Item(ItemName.FIRE_EXTINGUISHER.toString(), null),
            ItemWeight.getItemWeight(ItemName.FIRE_EXTINGUISHER.toString())),
        Arguments.of(
            new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null),
            ItemWeight.getItemWeight(ItemName.PROTECTIVE_MEDICAL_SUIT.toString())),
        Arguments.of(
            new Item(ItemName.JACKET.toString(), null),
            ItemWeight.getItemWeight(ItemName.JACKET.toString())),
        Arguments.of(
            new Item(ItemName.MUSIC_CD.toString(), null),
            ItemWeight.getItemWeight(ItemName.MUSIC_CD.toString())),
        Arguments.of(
            new Item(ItemName.BOOK.toString(), null),
            ItemWeight.getItemWeight(ItemName.BOOK.toString())),
        Arguments.of(
            new Item(ItemName.STOLEN_WALLET.toString(), null),
            ItemWeight.getItemWeight(ItemName.STOLEN_WALLET.toString())),
        Arguments.of(
            new Item(ItemName.STOLEN_MONEY.toString(), null),
            ItemWeight.getItemWeight(ItemName.STOLEN_MONEY.toString())),
        Arguments.of(
            new Item(ItemName.STOLEN_ISIC.toString(), null),
            ItemWeight.getItemWeight(ItemName.STOLEN_ISIC.toString())),
        Arguments.of(
            new Item(ItemName.KEYS.toString(), null),
            ItemWeight.getItemWeight(ItemName.KEYS.toString())),
        Arguments.of(
            new Item(ItemName.PAPER_CLIP.toString(), null),
            ItemWeight.getItemWeight(ItemName.PAPER_CLIP.toString())));
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesWithTheirWeights")
  @DisplayName("get item with its weight")
  void getItemWithItsWeight(Item item, int itemWeight) {
    String expectedOutput = item.getName() + "(" + itemWeight + ")";
    String actualOutput = item.getItemWithItsWeight();

    assertEquals(expectedOutput, actualOutput);
  }
}
