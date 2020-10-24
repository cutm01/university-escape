package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class OverlookItemCommandTest {

    private OverlookItemCommand overlookItemCommand;
    private Room room;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(
            true); // will be set to false later to test case where room is not examined yet
        gamePlan.setActualRoom(room);

        overlookItemCommand = new OverlookItemCommand(gamePlan);
    }

    private static Stream<Arguments> provideItemInstances() {
        return Stream.of(
            Arguments.of(new Item(ItemName.BOTTLE.toString(), null)),
            Arguments.of(new Item(ItemName.PEN.toString(), null)),
            Arguments.of(new Item(ItemName.WALLET.toString(), null)),
            Arguments.of(new Item(ItemName.ID_CARD.toString(), null)),
            Arguments.of(new Item(ItemName.MONEY.toString(), null)),
            Arguments.of(new Item(ItemName.MEDICAL_MASK.toString(), null)),
            Arguments.of(new Item(ItemName.ROPE.toString(), null)),
            Arguments.of(new Item(ItemName.ISIC.toString(), null)),
            Arguments.of(new Item(ItemName.SMALL_SNACK.toString(), null)),
            Arguments.of(new Item(ItemName.BIG_SNACK.toString(), null)),
            Arguments.of(new Item(ItemName.FIRE_EXTINGUISHER.toString(), null)),
            Arguments.of(new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null)),
            Arguments.of(new Item(ItemName.JACKET.toString(), null)),
            Arguments.of(new Item(ItemName.MUSIC_CD.toString(), null)),
            Arguments.of(new Item(ItemName.BOOK.toString(), null)),
            Arguments.of(new Item(ItemName.STOLEN_WALLET.toString(), null)),
            Arguments.of(new Item(ItemName.STOLEN_MONEY.toString(), null)),
            Arguments.of(new Item(ItemName.STOLEN_ISIC.toString(), null)),
            Arguments.of(new Item(ItemName.KEYS.toString(), null)),
            Arguments.of(new Item(ItemName.PAPER_CLIP.toString(), null)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(overlookItemCommand.getCommandName(),
            CommandName.OVERLOOK_ITEM.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(overlookItemCommand.getCommandName(),
            CommandName.OVERLOOK_ITEM.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void overlookItemWithZeroParameters() {
            String expectedOutput =
                "Špecifikuj, prosím, jeden predmet z miestnosti alebo svojho batohu, ktorý si chceš prehliadnuť";
            String actualOutput = overlookItemCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 1 parameter")
        void overlookItemWithMoreThanOneParameter(String parameter) {
            String[] parameters = {parameter, parameter};

            String expectedOutput =
                "Spomaľ trochu, naraz viem pracovať iba s jedným predmetom z miestnosti alebo tvojho batohu";
            String actualOutput = overlookItemCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.OverlookItemCommandTest#provideItemInstances")
        @DisplayName("overlook item FROM player INVENTORY")
        void overlookItemFromInventory(Item item) {
            inventory.addItemToInventory(item);

            String expectedOutput = item.getDescription();
            String actualOutput = overlookItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.OverlookItemCommandTest#provideItemInstances")
        @DisplayName("overlook item FROM actual game ROOM")
        void overlookItemFromRoom(Item item) {
            room.addItemToRoom(item);

            String expectedOutput = item.getDescription();
            String actualOutput = overlookItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.OverlookItemCommandTest#provideItemInstances")
        @DisplayName("overlook item BEFORE player EXAMINATION of actual game room")
        void overlookItemBeforeRoomExamination(Item item) {
            room.setWasRoomAlreadyExamined(false);

            String expectedOutput =
                "Kde mám ten predmet hľadať? Asi by bolo fajn sa najprv poriadne rozhliadnuť po miestnosti";
            String actualOutput = overlookItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.OverlookItemCommandTest#provideItemInstances")
        @DisplayName("overlook item which IS NOT IN INVENTORY OR ROOM")
        void overlookItemWhichIsNotInInventoryOrRoom(Item item) {
            String expectedOutput =
                "Žiadny taký predmet v miestnosti alebo tvojom batohu nevidím, nepomýlil si sa náhodou?";
            String actualOutput = overlookItemCommand.executeCommand(item.getName());

            assertEquals(expectedOutput, actualOutput);
        }
    }
}
