package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommandsListTest {
  private static GameImpl game;
  private static GamePlan gamePlan;
  private static CommandsList commandsList;

  @BeforeEach
  void setUp() {
    game = new GameImpl();
    gamePlan = game.getGamePlan();
    commandsList = new CommandsList();
  }

  private static Stream<Arguments> provideCommandInstancesWithTheirNames() {
    return Stream.of(
        Arguments.of(new ApproachCommand(gamePlan), CommandName.APPROACH.getCommandName()),
        Arguments.of(new DropCommand(gamePlan), CommandName.DROP.getCommandName()),
        Arguments.of(new EndGameCommand(game), CommandName.END_GAME.getCommandName()),
        Arguments.of(new ExamineItemCommand(gamePlan), CommandName.EXAMINE_ITEM.getCommandName()),
        Arguments.of(
            new ExamineObjectCommand(gamePlan), CommandName.EXAMINE_OBJECT.getCommandName()),
        Arguments.of(new OverlookItemCommand(gamePlan), CommandName.OVERLOOK_ITEM.getCommandName()),
        Arguments.of(new GoCommand(gamePlan), CommandName.GO.getCommandName()),
        Arguments.of(new HelpCommand(commandsList, gamePlan), CommandName.HELP.getCommandName()),
        Arguments.of(new LookAroundCommand(gamePlan), CommandName.LOOK_AROUND.getCommandName()),
        Arguments.of(
            new ShowInventoryCommand(gamePlan), CommandName.SHOW_INVENTORY.getCommandName()),
        Arguments.of(new TakeCommand(gamePlan), CommandName.TAKE.getCommandName()),
        Arguments.of(new TalkToCommand(gamePlan), CommandName.TALK_TO.getCommandName()),
        Arguments.of(new UseCommand(gamePlan), CommandName.USE.getCommandName()));
  }

  private static Stream<Arguments> provideCommandInstancesWithTheirDescriptions() {
    return Stream.of(
        Arguments.of(
            new ApproachCommand(gamePlan), CommandDescription.APPROACH.getCommandDescription()),
        Arguments.of(new DropCommand(gamePlan), CommandDescription.DROP.getCommandDescription()),
        Arguments.of(new EndGameCommand(game), CommandDescription.END_GAME.getCommandDescription()),
        Arguments.of(
            new ExamineItemCommand(gamePlan),
            CommandDescription.EXAMINE_ITEM.getCommandDescription()),
        Arguments.of(
            new ExamineObjectCommand(gamePlan),
            CommandDescription.EXAMINE_OBJECT.getCommandDescription()),
        Arguments.of(
            new OverlookItemCommand(gamePlan),
            CommandDescription.OVERLOOK_ITEM.getCommandDescription()),
        Arguments.of(new GoCommand(gamePlan), CommandDescription.GO.getCommandDescription()),
        Arguments.of(
            new HelpCommand(commandsList, gamePlan),
            CommandDescription.HELP.getCommandDescription()),
        Arguments.of(
            new LookAroundCommand(gamePlan),
            CommandDescription.LOOK_AROUND.getCommandDescription()),
        Arguments.of(
            new ShowInventoryCommand(gamePlan),
            CommandDescription.SHOW_INVENTORY.getCommandDescription()),
        Arguments.of(new TakeCommand(gamePlan), CommandDescription.TAKE.getCommandDescription()),
        Arguments.of(
            new TalkToCommand(gamePlan), CommandDescription.TALK_TO.getCommandDescription()),
        Arguments.of(new UseCommand(gamePlan), CommandDescription.USE.getCommandDescription()));
  }

  @Nested
  class AddCommandAndGetCommand {
    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName(
        "add command to CommandsList and test if it's present in list - VALID command names")
    void addCommandAndTestItsPresenceWithValidCommandNames(Command command, String commandName) {
      commandsList.addCommand(command);

      assertEquals(command, commandsList.getCommand(commandName));
    }

    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName(
        "add command to CommandsList and test if it's present in list - NON-VALID command names")
    void addCommandAndTestItsPresenceWithNonValidCommandNames(Command command, String commandName) {
      commandsList.addCommand(command);

      assertNull(commandsList.getCommand("nonValidCommandName"));
      assertNull(commandsList.getCommand("!@#%^&*()_++{}|\":<>?|~"));
      assertNull(commandsList.getCommand("Command \t Name\tWith\t\tSpaces"));
    }

    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName(
        "add command to CommandsList and test if it's present in list - NULL as command name")
    void addCommandAndTestItsPresenceWithNullAsCommandName(Command command, String commandName) {
      commandsList.addCommand(command);

      assertNull(commandsList.getCommand(null));
    }
  }

  @Nested
  class IsCommandValid {
    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName("add command to CommandsList and test if it is valid - VALID command names")
    void addCommandAndTestItsValidityWithValidCommandNames(Command command, String commandName) {
      commandsList.addCommand(command);

      assertTrue(commandsList.isCommandValid(commandName));
    }

    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName("add command to CommandsList and test if it is valid - NON-VALID command names")
    void addCommandAndTestItsValidityWithNonValidCommandNames(Command command, String commandName) {
      commandsList.addCommand(command);

      assertFalse(commandsList.isCommandValid("nonValidCommandName"));
      assertFalse(commandsList.isCommandValid("!@#%^&*()_++{}|\":<>?|~"));
      assertFalse(commandsList.isCommandValid("Command \t Name\tWith\t\tSpaces"));
    }

    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirNames")
    @DisplayName("add command to CommandsList and test if it is valid - NULL as command name")
    void addCommandAndTestItsValidityWithNullAsCommandName(Command command, String commandName) {
      commandsList.addCommand(command);

      assertFalse(commandsList.isCommandValid(null));
    }
  }

  @Nested
  class GetCommandsWithTheirUsage {
    @SuppressWarnings("UnnecessaryLocalVariable")
    @ParameterizedTest(name = "tested command: \"{0}\"")
    @MethodSource(
        "cz.vse.java.cutm01.adventure.gamelogic.CommandsListTest#provideCommandInstancesWithTheirDescriptions")
    @DisplayName("get commands one by one and test if they are printed with correct descriptions")
    void getCommandsWithTheirUsageValidCommandsName(Command command, String commandDescription) {
      commandsList.addCommand(command);

      String expectedOutput = commandDescription;
      String actualOutput = commandsList.getCommandsWithTheirUsage();

      assertTrue(actualOutput.contains(expectedOutput));
    }
  }
}
