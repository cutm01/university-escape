package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EndGameCommandTest {
  private GameImpl game;
  private EndGameCommand endGameCommand;

  @BeforeEach
  void setUp() {
    game = new GameImpl();
    endGameCommand = new EndGameCommand(game);
  }

  @Test
  @DisplayName("command name test")
  void getCommandName() {
    assertEquals(endGameCommand.getCommandName(), CommandName.END_GAME.getCommandName());
  }

  @Test
  @DisplayName("command description test")
  void getCommandDescription() {
    assertEquals(endGameCommand.getCommandName(), CommandName.END_GAME.getCommandName());
  }

  @Nested
  class ExecuteCommand {
    @ParameterizedTest
    @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
    @DisplayName("INVALID usage: test with one parameter")
    void endGameWithOneParameter(String parameter) {
      String expectedOutput =
          "Hra sa dá ukončiť aj bez tých zbytočných slov okolo, skús to ešte raz";
      String actualOutput = endGameCommand.executeCommand(parameter);

      assertEquals(expectedOutput, actualOutput);
      assertFalse(game.isGameOver()); // game SHOULD NOT end after
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
    @DisplayName("INVALID usage: test with two parameters")
    void endGameWithTwoParameters(String parameter) {
      String[] parameters = {parameter, parameter};

      String expectedOutput =
          "Hra sa dá ukončiť aj bez tých zbytočných slov okolo, skús to ešte raz";
      String actualOutput = endGameCommand.executeCommand(parameters);

      assertEquals(expectedOutput, actualOutput);
      assertFalse(game.isGameOver()); // game SHOULD NOT end after
    }

    @Test
    @DisplayName("VALID usage: test with zero parameters")
    void endGameWithZeroParameters() {
      String expectedOutput = "Rozhodol si sa to vzdať? Nuž, čo sa dá robiť...Ďakujem za zahranie!";
      String actualOutput = endGameCommand.executeCommand();

      assertEquals(expectedOutput, actualOutput);
      assertTrue(game.isGameOver()); // game SHOULD end after
    }
  }
}
