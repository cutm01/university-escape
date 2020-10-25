package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

class DropCommandTest {

    private DropCommand dropCommand;
    private Inventory inventory;
    private Room room;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        dropCommand = new DropCommand(gamePlan);
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

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(dropCommand.getCommandName(), CommandName.DROP.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(dropCommand.getCommandName(), CommandName.DROP.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: drop ZERO items")
        void dropZeroItems() {
            String expectedOutput =
                "Naozaj ti nedokážem čítať myšlienky. Upresni, prosím ťa, ktoré predmety chceš odhodiť";
            String actualOutput =
                dropCommand.executeCommand(); // executeCommand method takes item name as parameter

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest(name = "dropped item: \"{0}\"")
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.DropCommandTest#provideItemInstancesWithTheirWeights")
        @DisplayName("drop one item which is already in inventory")
        void dropOneItemWhichIsAlreadyInInventory(Item droppedItem, int droppedItemWeight) {
            inventory.addItemToInventory(droppedItem);

            String actualOutput =
                dropCommand.executeCommand(
                    droppedItem.getName()); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si odhodil nasledovné predmety (váha predmetu):"
                + SystemInfo.LINE_SEPARATOR
                + droppedItem.getName()
                + "("
                + droppedItemWeight
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                droppedItem,
                room.getItemByName(droppedItem.getName())); // was dropped item added to room?
            assertEquals(0, inventory.getInventoryWeight());
        }

        @ParameterizedTest(name = "dropped item: \"{0}\"")
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.DropCommandTest#provideItemInstancesWithTheirWeights")
        @DisplayName("try to drop one SAME item multiple times")
        void dropSameItemMultipleTimes(Item droppedItem, int droppedItemWeight) {
            inventory.addItemToInventory(droppedItem);
            String[] itemsToDrop = {droppedItem.getName(), droppedItem.getName()};

            String actualOutput =
                dropCommand.executeCommand(
                    itemsToDrop); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si odhodil nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + droppedItem.getName()
                + "("
                + droppedItemWeight
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                droppedItem,
                room.getItemByName(droppedItem.getName())); // was dropped item added to room?
            assertEquals(1, room.getNumberOfItemsInRoom());
            assertEquals(0, inventory.getInventoryWeight());
        }

        @Test
        @DisplayName("drop two different items")
        void dropTwoDifferentItems() {
            Item item1 = new Item(ItemName.BOTTLE.toString(), null);
            Item item2 = new Item(ItemName.PEN.toString(), null);
            inventory.addItemToInventory(item1);
            inventory.addItemToInventory(item2);
            String[] itemsToDrop = {item1.getName(), item2.getName()};

            String actualOutput =
                dropCommand.executeCommand(
                    itemsToDrop); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si odhodil nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + item1.getName()
                + "("
                + item1.getWeight()
                + "), "
                + item2.getName()
                + "("
                + item2.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            // was dropped items added to room?
            assertEquals(item1, room.getItemByName(item1.getName()));
            assertEquals(item2, room.getItemByName(item2.getName()));
            assertEquals(2, room.getNumberOfItemsInRoom());

            assertEquals(0, inventory.getInventoryWeight());
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("try to drop ONE item which is not in inventory")
        void dropOneInvalidItem(String invalidItemName) {
            String actualOutput =
                dropCommand.executeCommand(
                    invalidItemName); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si neodhodil žiadnu vec!"
+ SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi nepodarilo nájsť v tvojom batohu, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + invalidItemName
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použiť príkaz "
                + CommandName.SHOW_INVENTORY.getCommandName()
                + " pre zobrazenie vecí, ktoré máš u seba"
+ SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(0, inventory.getInventoryWeight());
            assertEquals(0, room.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space", "-123456789"})
        @DisplayName("try drop TWO items which are not in inventory")
        void dropTwoInvalidItems(String invalidItemName) {
            String[] invalidItemNames = {invalidItemName, invalidItemName};

            String actualOutput =
                dropCommand.executeCommand(
                    invalidItemNames); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si neodhodil žiadnu vec!"
+ SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi nepodarilo nájsť v tvojom batohu, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + invalidItemName
                + ", "
                + invalidItemName
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použiť príkaz "
                + CommandName.SHOW_INVENTORY.getCommandName()
                + " pre zobrazenie vecí, ktoré máš u seba"
+ SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(0, inventory.getInventoryWeight());
            assertEquals(0, room.getNumberOfItemsInRoom());
        }

        @ParameterizedTest(name = "valid dropped item: \"{0}\"")
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.DropCommandTest#provideItemInstancesWithTheirWeights")
        @DisplayName("drop combination of VALID and INVALID items")
        void dropCombinationOfValidAndInvalidItems(Item validItemName, int validItemWeight) {
            inventory.addItemToInventory(validItemName);
            String[] itemsToDrop = {
                validItemName.getName(), "abcdefg", "!@#$%^&**()", "null", "name with space"
            };

            String actualOutput =
                dropCommand.executeCommand(
                    itemsToDrop); // executeCommand method takes item name as parameter
            String expectedOutput =
                "Zo svojho batohu si odhodil nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + validItemName.getName()
                + "("
                + validItemWeight
                + ")"
                + "Tieto veci sa mi nepodarilo nájsť v tvojom batohu, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + itemsToDrop[1]
                + ", "
                + itemsToDrop[2]
                + ", "
                + itemsToDrop[3]
                + ", "
                + itemsToDrop[4]
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použiť príkaz "
                + CommandName.SHOW_INVENTORY.getCommandName()
                + " pre zobrazenie vecí, ktoré máš u seba"
+ SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                validItemName,
                room.getItemByName(validItemName.getName())); // was dropped item added to room?
            assertEquals(1, room.getNumberOfItemsInRoom());
            assertEquals(0, inventory.getInventoryWeight());
        }
    }
}
