package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class ShowInventoryCommandTest {

    private ShowInventoryCommand showInventoryCommand;
    private Inventory inventory;
    private Item testItem;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();

        showInventoryCommand = new ShowInventoryCommand(gamePlan);
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(
            showInventoryCommand.getCommandName(), CommandName.SHOW_INVENTORY.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(
            showInventoryCommand.getCommandName(), CommandName.SHOW_INVENTORY.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("show EMPTY inventory content with MORE THAN ZERO PARAMETERS")
        void showEmptyInventoryContentWithMoreThanZeroParameters(String parameter) {
            String expectedOutput =
                "Chceš ukázať obsah batohu, pochopil som to správne? Nech sa ti páči,\n"
                + "a nabudúce to skús, prosím ťa, bez tých zbytočných rečí okolo:\n"
                + "V batohu sa aktuálne nič nenachádza";
            String actualOutput = showInventoryCommand.executeCommand(parameter);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("show NON-EMPTY inventory content with MORE THAN ZERO PARAMETERS")
        void showNonEmptyInventoryContentWithMoreThanZeroParameters(ItemName itemName) {
            String[] parameters = {"abcdefg", "!@#$%^&**()", "null", "name with space"};
            testItem = new Item(itemName.toString(), null);
            inventory.addItemToInventory(testItem);

            String expectedOutput =
                "Chceš ukázať obsah batohu, pochopil som to správne? Nech sa ti páči,\n"
                + "a nabudúce to skús, prosím ťa, bez tých zbytočných rečí okolo:\n"
                + "V batohu máš nasledovné predmety (váha predmetu):\n"
                + testItem.getName()
                + "("
                + testItem.getWeight()
                + ")\n"
                + "Aktuálna váha batohu je "
                + inventory.getInventoryWeight()
                + ", maximálna kapacita je "
                + inventory.getInventoryCapacity();
            String actualOutput;

            for (String p : parameters) {
                actualOutput = showInventoryCommand.executeCommand(p);
                assertEquals(expectedOutput, actualOutput);
            }
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("show EMPTY inventory content WITH ZERO PARAMETERS")
        void showEmptyInventoryContentWithZeroParameters(ItemName itemName) {
            String expectedOutput = "V batohu sa aktuálne nič nenachádza";
            String actualOutput = showInventoryCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("show NON-EMPTY inventory content WITH ZERO PARAMETERS")
        void showNonEmptyInventoryContentWithZeroParameters(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            inventory.addItemToInventory(testItem);

            String expectedOutput =
                "V batohu máš nasledovné predmety (váha predmetu):\n"
                + testItem.getName()
                + "("
                + testItem.getWeight()
                + ")\n"
                + "Aktuálna váha batohu je "
                + inventory.getInventoryWeight()
                + ", maximálna kapacita je "
                + inventory.getInventoryCapacity();
            String actualOutput = showInventoryCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }
    }
}
