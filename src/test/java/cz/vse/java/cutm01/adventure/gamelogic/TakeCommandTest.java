package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class TakeCommandTest {

    private TakeCommand takeCommand;
    private Inventory inventory;
    private Room room;
    private Item item;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        takeCommand = new TakeCommand(gamePlan);
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(takeCommand.getCommandName(), CommandName.TAKE.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(takeCommand.getCommandName(), CommandName.TAKE.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: take ZERO items")
        void takeZeroItems() {
            String expectedOutput =
                "Hm...nezabudol si na niečo? Napríklad uviesť aspoň 1 predmet, ktorý si chceš z miestnosti vziať";
            String actualOutput = takeCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("take item from room which WAS NOT EXAMINED yet")
        void takeItemFromNonExaminedRoom(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);
            room.setWasRoomAlreadyExamined(false);

            String expectedOutput =
                "Na chvíľu spomaľ a poriadne sa rozhliadni po miestnosti, aby si vôbec vedel, aké predmety si môžeš odniesť";
            String actualOutput = takeCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(1, room.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("take ONE VALID item")
        void takeOneValidItem(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);

            String actualOutput = takeCommand.executeCommand(item.getName());
            String expectedOutput =
                "Do batohu si vkladáš nasledovné predmety (váha predmetu):"
                + SystemInfo.LINE_SEPARATOR
                + item.getName()
                + "("
                + item.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(item.getWeight(), inventory.getInventoryWeight());
            assertEquals(item, inventory.getItemByName(item.getName()));
            assertEquals(0, room.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("take ONE NON-VALID item")
        void takeOneNonValidItem(String itemName) {
            String expectedOutput =
                "Z miestnosti si si nič nevzal!"
+ SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi v tejto miestnosti nepodarilo nájsť, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + itemName
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použit príkaz "
                + CommandName.LOOK_AROUND.getCommandName()
                + " pre zobrazenie vecí, ktoré sú aktuálne v miestnosti,"
+ SystemInfo.LINE_SEPARATOR
                + "prípadne jeden z dvojice príkazov "
                + CommandName.EXAMINE_ITEM.getCommandName()
                + ", "
                + CommandName.EXAMINE_OBJECT.getCommandName()
                + " v snahe nájsť skryté predmety"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();
            String actualOutput = takeCommand.executeCommand(itemName);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("take one item multiple times")
        void takeOneItemMultipleTimes(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            String[] itemsToTake = {item.getName(), item.getName()};
            room.addItemToRoom(item);

            String actualOutput = takeCommand.executeCommand(itemsToTake);
            String expectedOutput =
                "Do batohu si vkladáš nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + item.getName()
                + "("
                + item.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi v tejto miestnosti nepodarilo nájsť, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + item.getName()
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použit príkaz "
                + CommandName.LOOK_AROUND.getCommandName()
                + " pre zobrazenie vecí, ktoré sú aktuálne v miestnosti,"
+ SystemInfo.LINE_SEPARATOR
                + "prípadne jeden z dvojice príkazov "
                + CommandName.EXAMINE_ITEM.getCommandName()
                + ", "
                + CommandName.EXAMINE_OBJECT.getCommandName()
                + " v snahe nájsť skryté predmety"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(item.getWeight(), inventory.getInventoryWeight());
            assertEquals(item, inventory.getItemByName(item.getName()));
            assertEquals(0, room.getNumberOfItemsInRoom());
        }

        @Test
        @DisplayName("take TWO different VALID ITEMS")
        void takeTwoDifferentItems() {
            Item item1 = new Item(ItemName.BOTTLE.toString(), null);
            Item item2 = new Item(ItemName.PEN.toString(), null);
            room.addItemToRoom(item1);
            room.addItemToRoom(item2);
            String[] itemsToTake = {item1.getName(), item2.getName()};

            String actualOutput = takeCommand.executeCommand(itemsToTake);
            String expectedOutput =
                "Do batohu si vkladáš nasledovné predmety (váha predmetu):"
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
            assertEquals(item1.getWeight() + item2.getWeight(), inventory.getInventoryWeight());
            assertEquals(item1, inventory.getItemByName(item1.getName()));
            assertEquals(item2, inventory.getItemByName(item2.getName()));
            assertEquals(0, room.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space", "-123456789"})
        @DisplayName("take TWO NON-VALID ITEMS")
        void takeTwoNonValidItems(String nonValidItemName) {
            String[] nonValidItemNames = {nonValidItemName, nonValidItemName};

            String actualOutput = takeCommand.executeCommand(nonValidItemNames);
            String expectedOutput =
                "Z miestnosti si si nič nevzal!"
+ SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi v tejto miestnosti nepodarilo nájsť, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + nonValidItemName
                + ", "
                + nonValidItemName
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použit príkaz "
                + CommandName.LOOK_AROUND.getCommandName()
                + " pre zobrazenie vecí, ktoré sú aktuálne v miestnosti,"
+ SystemInfo.LINE_SEPARATOR
                + "prípadne jeden z dvojice príkazov "
                + CommandName.EXAMINE_ITEM.getCommandName()
                + ", "
                + CommandName.EXAMINE_OBJECT.getCommandName()
                + " v snahe nájsť skryté predmety"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(0, inventory.getInventoryWeight());
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("take combination of VALID AND NON-VALID ITEMS")
        void takeCombinationOfValidAndNonValidItems(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);
            String[] itemsToTake = {item.getName(), "abcdefg", "!@#$%^&**()", "null",
                                    "name with space"};

            String actualOutput = takeCommand.executeCommand(itemsToTake);
            String expectedOutput =
                "Do batohu si vkladáš nasledovné predmety (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + item.getName()
                + "("
                + item.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Tieto veci sa mi v tejto miestnosti nepodarilo nájsť, nepomýlil si sa náhodou?"
+ SystemInfo.LINE_SEPARATOR
                + itemsToTake[1]
                + ", "
                + itemsToTake[2]
                + ", "
                + itemsToTake[3]
                + ", "
                + itemsToTake[4]
                + SystemInfo.LINE_SEPARATOR
                + "Stále môžeš použit príkaz "
                + CommandName.LOOK_AROUND.getCommandName()
                + " pre zobrazenie vecí, ktoré sú aktuálne v miestnosti,"
+ SystemInfo.LINE_SEPARATOR
                + "prípadne jeden z dvojice príkazov "
                + CommandName.EXAMINE_ITEM.getCommandName()
                + ", "
                + CommandName.EXAMINE_OBJECT.getCommandName()
                + " v snahe nájsť skryté predmety"
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálna kapacita batohu: "
                + inventory.getInventoryWeight()
                + "/"
                + inventory.getInventoryCapacity();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(item.getWeight(), inventory.getInventoryWeight());
            assertEquals(item, inventory.getItemByName(item.getName()));
            assertEquals(0, room.getNumberOfItemsInRoom());
        }
    }
}
