package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

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

    @Nested
    class GetItemByName {

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("getItemByName with VALID item instances which ARE in inventory")
        void getItemByNameWithItemInstancesWhichAreInInventory(Item item) {
            inventory.addItemToInventory(item);

            assertEquals(item, inventory.getItemByName(item.getName()));
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("getItemByName with VALID item instances which ARE NOT in inventory")
        void getItemByNameWithItemInstancesWhichAreNotInInventory(Item item) {
            assertNull(inventory.getItemByName(item.getName()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("getItemByName with NON-VALID item instances")
        void getItemByNameWithNonValidItemInstances(String parameter) {
            assertNull(inventory.getItemByName(parameter));
        }
    }

    @ParameterizedTest
    @MethodSource("provideItemInstancesWithTheirWeights")
    @DisplayName("add item to inventory")
    void addItemToInventory(Item item) {
        inventory.addItemToInventory(item);

        assertEquals(item, inventory.getItemByName(item.getName()));
        assertEquals(item.getWeight(), inventory.getInventoryWeight());
    }

    @Nested
    class RemoveItemFromInventory {

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("remove item which IS in inventory")
        void removeItemFromInventoryWithItemWhichIsInInventory(Item item) {
            inventory.addItemToInventory(item);
            inventory.removeItemFromInventory(item.getName());

            assertEquals(0, inventory.getInventoryWeight());
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("remove item which IS NOT in inventory")
        void removeItemFromInventoryWithItemWhichIsNotInInventory(Item item) {
            inventory.removeItemFromInventory(item.getName());

            assertEquals(0, inventory.getInventoryWeight());
        }
    }

    @Nested
    class GetItemWithItsWeight {

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("get item with its weight for item which IS in inventory")
        void getItemWithItsWeightForItemWhichIsInInventory(Item item, int itemWeight) {
            inventory.addItemToInventory(item);

            String expectedOutput = item.getName() + "(" + itemWeight + ")";
            String actualOutput = inventory.getItemWithItsWeight(item.getName());

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("get item with its weight for item which IS NOT in inventory")
        void getItemWithItsWeightForItemWhichIsNotInInventory(Item item) {
            assertNull(inventory.getItemByName(item.getName()));
        }
    }

    @Nested
    class GetInventoryContent {

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.InventoryTest#provideItemInstancesWithTheirWeights")
        @DisplayName("get inventory content for NON-EMPTY inventory")
        void getInventoryContent(Item item, int itemWeight) {
            inventory.addItemToInventory(item);

            String expectedOutput =
                "V batohu máš nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + item.getName()
                + "("
                + itemWeight
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna váha batohu je "
                + inventory.getInventoryWeight()
                + ", "
                + "maximálna kapacita je "
                + inventory.getInventoryCapacity();
            String actualOutput = inventory.getInventoryContent();

            assertEquals(expectedOutput, actualOutput);
        }

        @Test
        @DisplayName("get inventory content for EMPTY inventory")
        void getInventoryContent() {
            String expectedOutput = "V batohu sa aktuálne nič nenachádza";
            String actualOutput = inventory.getInventoryContent();

            assertEquals(expectedOutput, actualOutput);
        }
    }
}
