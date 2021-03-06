package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExamineItemCommandTest {

    private ExamineItemCommand examineItemCommand;
    private Room room;
    private Inventory inventory;
    private Item item;
    private HiddenItems hiddenItems;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();
        item = new Item("JACKET", null);

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        examineItemCommand = new ExamineItemCommand(gamePlan);
    }

    private static Stream<Arguments> provideItemInstancesToCreateHiddenItemsInstances() {
        return Stream.of(
            Arguments.of(
                new Item(ItemName.BOTTLE.toString(), null),
                new Item(ItemName.PEN.toString(), null),
                new Item(ItemName.WALLET.toString(), null)),
            Arguments.of(
                new Item(ItemName.ID_CARD.toString(), null),
                new Item(ItemName.MONEY.toString(), null),
                new Item(ItemName.MEDICAL_MASK.toString(), null)),
            Arguments.of(
                new Item(ItemName.ROPE.toString(), null),
                new Item(ItemName.ISIC.toString(), null),
                new Item(ItemName.SMALL_SNACK.toString(), null)),
            Arguments.of(
                new Item(ItemName.FIRE_EXTINGUISHER.toString(), null),
                new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null),
                new Item(ItemName.MUSIC_CD.toString(), null)),
            Arguments.of(
                new Item(ItemName.BOOK.toString(), null),
                new Item(ItemName.STOLEN_WALLET.toString(), null),
                new Item(ItemName.STOLEN_MONEY.toString(), null)),
            Arguments.of(
                new Item(ItemName.STOLEN_ISIC.toString(), null),
                new Item(ItemName.KEYS.toString(), null),
                new Item(ItemName.PAPER_CLIP.toString(), null)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(examineItemCommand.getCommandName(),
            CommandName.EXAMINE_ITEM.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(examineItemCommand.getCommandName(),
            CommandName.EXAMINE_ITEM.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void examineItemWithZeroParameters() {
            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Nerozumiem ti! Upresni, pros??m ??a, ktor?? predmet z miestnosti alebo svojho batohu chce?? presk??ma??";
            String actualOutput = examineItemCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: two parameters")
        void examineItemWithTwoParameters(String parameter) {
            String[] parameters = {parameter, parameter};

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Situ??cia je v????na, no aj tak by bolo fajn zachova?? si chladn?? hlavu a nepresk??mava?? viac predmetov naraz!";
            String actualOutput = examineItemCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest(name = "examined item: {0}")
        @EnumSource(ItemName.class)
        @DisplayName("examine item from inventory with NO hidden items in it")
        void examineInventoryItemWithNoHiddenItems(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            inventory.addItemToInventory(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "V r??chlosti prezer???? predmet " + item.getName()
                + ", no ni?? zauj??mav??ho na ??om nevid????";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
            assertEquals(
                item,
                inventory.getItemByName(
                    item.getName())); // item should remain in inventory after examination
            assertTrue(
                item.wasItemAlreadyExamined()); // should be set to true, even if items does not have
            // any hidden items in it
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.ExamineItemCommandTest#provideItemInstancesToCreateHiddenItemsInstances")
        @DisplayName("examine item from inventory with hidden items in it")
        void examineInventoryItemWithHiddenItems(Item hiddenItem1, Item hiddenItem2,
            Item hiddenItem3) {
            Item[] itemsToHide = {hiddenItem1, hiddenItem2, hiddenItem3};
            hiddenItems = new HiddenItems(itemsToHide);
            item = new Item("JACKET", hiddenItems);
            inventory.addItemToInventory(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "S tras??cimi rukami si prezer???? predmet "
                + item.getName()
                + " a podarilo sa ti n??js?? nasledovn?? veci (v??ha predmetu):"
                + SystemInfo.LINE_SEPARATOR
                + hiddenItem1.getName()
                + "("
                + hiddenItem1.getWeight()
                + "), "
                + hiddenItem2.getName()
                + "("
                + hiddenItem2.getWeight()
                + "), "
                + hiddenItem3.getName()
                + "("
                + hiddenItem3.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Ale nie! Vyzer???? by?? z toho v??etk??ho v zna??nom strese a veci, ktor?? si pr??ve na??iel, ti popadali na zem"
                + SystemInfo.LINE_SEPARATOR
                + "a teraz sa pova??uj?? v??ade po miestnosti";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            // hidden items should be added to room after examination
            assertEquals(expectedNumberOfItemsInRoom, room.getNumberOfItemsInRoom());
            assertEquals(hiddenItem1, room.getItemByName(hiddenItem1.getName()));
            assertEquals(hiddenItem2, room.getItemByName(hiddenItem2.getName()));
            assertEquals(hiddenItem3, room.getItemByName(hiddenItem3.getName()));
            assertEquals(0, item.getHiddenItems().getNumberOfHiddenItems());
            // item should be set to "wasAlreadyExamined = true" and remain in inventory after examination
            assertEquals(item, inventory.getItemByName(item.getName()));
            assertTrue(item.wasItemAlreadyExamined());
        }

        @ParameterizedTest(name = "examined item: {0}")
        @EnumSource(ItemName.class)
        @DisplayName("examine item from inventory which was ALREADY EXAMINED before")
        void examineInventoryItemWhichWasAlreadyExaminedBefore(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            item.setWasItemAlreadyExamined(true);
            inventory.addItemToInventory(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Tento predmet si u?? presk??mal v minulosti, ni?? nov??ho, ??o by ti pomohlo na ??om nen??jde??!";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
            assertEquals(
                item,
                inventory.getItemByName(
                    item.getName())); // item should remain in inventory after examination
        }

        @ParameterizedTest(name = "examined item: {0}")
        @EnumSource(ItemName.class)
        @DisplayName("examine item from room with NO hidden items in it")
        void examineRoomItemWithNoHiddenItems(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "V r??chlosti prezer???? predmet " + item.getName()
                + ", no ni?? zauj??mav??ho na ??om nevid????";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
            assertEquals(
                item,
                room.getItemByName(item.getName())); // item should remain in room after examination
            assertTrue(
                item.wasItemAlreadyExamined()); // should be set to true, even if items does not have
            // any hidden items in it
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.ExamineItemCommandTest#provideItemInstancesToCreateHiddenItemsInstances")
        @DisplayName("examine item from room with hidden items in it")
        void examineRoomItemWithHiddenItems(Item hiddenItem1, Item hiddenItem2, Item hiddenItem3) {
            Item[] itemsToHide = {hiddenItem1, hiddenItem2, hiddenItem3};
            hiddenItems = new HiddenItems(itemsToHide);
            item = new Item("JACKET", hiddenItems);
            room.addItemToRoom(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "S tras??cimi rukami si prezer???? predmet "
                + item.getName()
                + " a podarilo sa ti n??js?? nasledovn?? veci (v??ha predmetu):"
                + SystemInfo.LINE_SEPARATOR
                + hiddenItem1.getName()
                + "("
                + hiddenItem1.getWeight()
                + "), "
                + hiddenItem2.getName()
                + "("
                + hiddenItem2.getWeight()
                + "), "
                + hiddenItem3.getName()
                + "("
                + hiddenItem3.getWeight()
                + ")"
                + SystemInfo.LINE_SEPARATOR
                + "Ale nie! Vyzer???? by?? z toho v??etk??ho v zna??nom strese a veci, ktor?? si pr??ve na??iel, ti popadali na zem"
                + SystemInfo.LINE_SEPARATOR
                + "a teraz sa pova??uj?? v??ade po miestnosti";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            // hidden items should be added to room after examination
            assertEquals(expectedNumberOfItemsInRoom, room.getNumberOfItemsInRoom());
            assertEquals(hiddenItem1, room.getItemByName(hiddenItem1.getName()));
            assertEquals(hiddenItem2, room.getItemByName(hiddenItem2.getName()));
            assertEquals(hiddenItem3, room.getItemByName(hiddenItem3.getName()));
            assertEquals(0, item.getHiddenItems().getNumberOfHiddenItems());
            // item should be set to "wasAlreadyExamined = true" and remain in room after examination
            assertEquals(item, room.getItemByName(item.getName()));
            assertTrue(item.wasItemAlreadyExamined());
        }

        @ParameterizedTest(name = "examined item: {0}")
        @EnumSource(ItemName.class)
        @DisplayName("examine item from room which was ALREADY EXAMINED before")
        void examineRoomItemWhichWasAlreadyExaminedBefore(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            item.setWasItemAlreadyExamined(true);
            room.addItemToRoom(item);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Tento predmet si u?? presk??mal v minulosti, ni?? nov??ho, ??o by ti pomohlo na ??om nen??jde??!";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
            assertEquals(
                item,
                room.getItemByName(item.getName())); // item should remain in room after examination
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("examine item which is NOT in inventory or room")
        void examineItemWhichIsNotInRoomOrInventory(String itemName) {
            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Ni?? tak?? tu nevid??m, nem????e by?? ten predmet schovan?? v r??mci nejak??ho objektu, ktor?? si e??te nepresk??mal?";
            String actualOutput = examineItemCommand.executeCommand(itemName);

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest(name = "examined item: {0}")
        @EnumSource(ItemName.class)
        @DisplayName("try to examine room item before room was examined")
        void examineRoomItemBeforeRoomWasExamined(ItemName itemName) {
            item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);
            room.setWasRoomAlreadyExamined(false);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom() + item.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Kde m??m ten predmet h??ada??? Asi by bolo fajn sa najprv poriadne rozhliadnu?? po miestnosti";
            String actualOutput = examineItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }
    }
}
