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
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HelpCommandTest {

    private static GameImpl game;
    private static GamePlan gamePlan;
    private HelpCommand helpCommand;
    private Room room;
    private static CommandsList commandsList;

    private static final String GAME_MAP_IN_TEXT_FORMAT =
        "................................................................................" + SystemInfo.LINE_SEPARATOR
        + ".            ________    _______   _____                             ________  ." + SystemInfo.LINE_SEPARATOR
        + ".           |        |  |       | |     |                           |        | ." + SystemInfo.LINE_SEPARATOR
        + ".           | RB_201 |  |toalety| |satna|                           |kniznica| ." + SystemInfo.LINE_SEPARATOR
        + ".           |__   ___|  |__   __| |_   _|                           |___   __| ." + SystemInfo.LINE_SEPARATOR
        + ".           ___| |____    _| |_____ | |__                            ___| |___ ." + SystemInfo.LINE_SEPARATOR
        + ". ______   |          |  |               |   ________               |         |." + SystemInfo.LINE_SEPARATOR
        + ".|      |__|  chodba_ |__|   chodba_na_  |__|        |______________|         |." + SystemInfo.LINE_SEPARATOR
        + ".|RB_202 __   na_II._  __   I._poschodi   __   Nova_   spojovacia_    Stara_  |." + SystemInfo.LINE_SEPARATOR
        + ".|______|  | poschodi |  |               |  | budova     chodba       budova  |." + SystemInfo.LINE_SEPARATOR
        + ".          |__________|  |______   ______|  |         ______________          |." + SystemInfo.LINE_SEPARATOR
        + ".                            ___| |____     |___   __|              |_________|." + SystemInfo.LINE_SEPARATOR
        + ".                           |          |      __| |__    ___________           ." + SystemInfo.LINE_SEPARATOR
        + ".                           |kancelaria|     |       |  |           |          ." + SystemInfo.LINE_SEPARATOR
        + ".                           |__________|     |       |__|Vencovskeho|          ." + SystemInfo.LINE_SEPARATOR
        + ".                                            | dvor   __    aula    |          ." + SystemInfo.LINE_SEPARATOR
        + ".                                            |       |  |___________|          ." + SystemInfo.LINE_SEPARATOR
        + ".                                            |__    _|                         ." + SystemInfo.LINE_SEPARATOR
        + ".                                            ___|  |___                        ." + SystemInfo.LINE_SEPARATOR
        + ".                                           |  ulica   |                       ." + SystemInfo.LINE_SEPARATOR
        + ".                                           |__________|                       ." + SystemInfo.LINE_SEPARATOR
        + "................................................................................";

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
                "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!"
                + SystemInfo.LINE_SEPARATOR
                + SystemInfo.LINE_SEPARATOR
                + "K dispozícii máš nasledovné príkazy:"
                + SystemInfo.LINE_SEPARATOR
                + commandsList.getCommandsWithTheirUsage()
                + SystemInfo.LINE_SEPARATOR
                + "Aktuálne sa nachádzaš v "
                + room.getName()
                + ", mapa budovy:"
                + SystemInfo.LINE_SEPARATOR;
            String actualOutput = helpCommand.executeCommand();

            assertTrue(actualOutput.contains(partOfExpectedOutput));
            assertTrue(actualOutput.contains(GAME_MAP_IN_TEXT_FORMAT));
        }
    }
}
