package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HelpCommandTest {

    private static GameImpl game;
    private static GamePlan gamePlan;
    private HelpCommand helpCommand;
    private Room room;
    private static CommandsList commandsList;

    @BeforeEach
    void setUp() {
        game = new GameImpl();
        gamePlan = game.getGamePlan();
        commandsList = new CommandsList();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        gamePlan.setActualRoom(room);

        helpCommand = new HelpCommand(commandsList, gamePlan);
    }

    private static Stream<Arguments> provideCommandInstances() {
        return Stream.of(
            Arguments.of(
                new ApproachCommand(gamePlan),
                new DropCommand(gamePlan),
                new EndGameCommand(game),
                new ExamineItemCommand(gamePlan),
                new ExamineObjectCommand(gamePlan),
                new OverlookItemCommand(gamePlan),
                new GoCommand(gamePlan),
                new HelpCommand(commandsList, gamePlan),
                new LookAroundCommand(gamePlan),
                new ShowInventoryCommand(gamePlan),
                new TakeCommand(gamePlan),
                new TalkToCommand(gamePlan),
                new UseCommand(gamePlan)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(helpCommand.getCommandName(), CommandName.HELP.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(helpCommand.getCommandName(), CommandName.HELP.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @ParameterizedTest
        @MethodSource("cz.vse.java.cutm01.adventure.gamelogic.HelpCommandTest#provideCommandInstances")
        @DisplayName(
            "output text contains game map, commands with descriptions, actual game room and goal of game")
        void helpCommandOutputTextContainsGameInfo(ArgumentsAccessor commandInstances) {
            for (int i = 0; i < commandInstances.size(); ++i) {
                commandsList.addCommand((Command) commandInstances.toList().get(i));
            }

            String partOfExpectedOutput =
                "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!\n\n"
                + "K dispozícii máš nasledovné príkazy:\n"
                + commandsList.getCommandsWithTheirUsage()
                + "\nAktuálne sa nachádzaš v "
                + room.getName()
                + ", mapa budovy:\n";
            String gameMap =
                "................................................................................\n"
                + ".            ________    _______   _____                             ________  .\n"
                + ".           |        |  |       | |     |                           |        | .\n"
                + ".           | RB_201 |  |toalety| |satna|                           |kniznica| .\n"
                + ".           |__   ___|  |__   __| |_   _|                           |___   __| .\n"
                + ".           ___| |____    _| |_____ | |__                            ___| |___ .\n"
                + ". ______   |          |  |               |   ________               |         |.\n"
                + ".|      |__|  chodba_ |__|   chodba_na_  |__|        |______________|         |.\n"
                + ".|RB_202 __   na_II._  __   I._poschodi   __   Nova_   spojovacia_    Stara_  |.\n"
                + ".|______|  | poschodi |  |               |  | budova     chodba       budova  |.\n"
                + ".          |__________|  |______   ______|  |         ______________          |.\n"
                + ".                            ___| |____     |___   __|              |_________|.\n"
                + ".                           |          |      __| |__    ___________           .\n"
                + ".                           |kancelaria|     |       |  |           |          .\n"
                + ".                           |__________|     |       |__|Vencovskeho|          .\n"
                + ".                                            | dvor   __    aula    |          .\n"
                + ".                                            |       |  |___________|          .\n"
                + ".                                            |__    _|                         .\n"
                + ".                                            ___|  |___                        .\n"
                + ".                                           |  ulica   |                       .\n"
                + ".                                           |__________|                       .\n"
                + "................................................................................";
            String actualOutput = helpCommand.executeCommand();

            assertTrue(actualOutput.contains(partOfExpectedOutput));
            assertTrue(actualOutput.contains(gameMap));
        }
    }
}
