package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class TalkToCommandTest {

    private GamePlan gamePlan;
    private TalkToCommand talkToCommand;
    private Room room;
    private NonPlayerCharacter nonPlayerCharacter;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        gamePlan = game.getGamePlan();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        talkToCommand = new TalkToCommand(gamePlan);
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(talkToCommand.getCommandName(), CommandName.TALK_TO.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(talkToCommand.getCommandName(), CommandName.TALK_TO.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("player is NOT STANDING to any NPC")
        void talkToWhileNotStandingNextToNPC() {
            String expectedOutput =
                "Asi by bolo lepšie najprv k nejakej osobe pristúpiť a až potom sa s ňou začať rozprávať";
            String actualOutput = talkToCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 1 parameter")
        void talkToWithMoreThanOneParameter(String parameter) {
            nonPlayerCharacter = new NonPlayerCharacter("IT_ADMIN");
            gamePlan.setActualNonPlayerCharacter(nonPlayerCharacter);
            String[] parameters = {parameter, parameter};

            String expectedOutput =
                "Nie tak zhurta! Dokážem sa rozprávať naraz iba s jednou osobou, inak by v tom bol chaos";
            String actualOutput = talkToCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("talk to NPC which player STANDS NEXT TO (zero parameters)")
        void talkToNPCPlayerStandsNearby(NonPlayerCharacterName npcName) {
            nonPlayerCharacter = new NonPlayerCharacter(npcName.toString());
            room.addNonPlayerCharacterToRoom(nonPlayerCharacter);
            gamePlan.setActualNonPlayerCharacter(nonPlayerCharacter);

            String expectedOutput =
                NonPlayerCharacterSpeech.getNonPlayerCharacterSpeech(npcName.toString());
            String actualOutput = talkToCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("talk to DIFFERENT NPC than player stands next to (one parameter)")
        void talkToDifferentNPCThanPlayerStandsNearby(NonPlayerCharacterName npcName) {
            nonPlayerCharacter = new NonPlayerCharacter(npcName.toString());
            room.addNonPlayerCharacterToRoom(nonPlayerCharacter);
            gamePlan.setActualNonPlayerCharacter(nonPlayerCharacter);

            String expectedOutput =
                "Skús najprv pristúpiť k osobe, s ktorou sa chceš porozprávať. Aktuálne stojíš pri "
                + nonPlayerCharacter.getName()
                + "\n";
            String actualOutput =
                talkToCommand.executeCommand(
                    "neexistujucaOsoba"); // use different NPC name than player is currently standing next
            // to

            assertEquals(expectedOutput, actualOutput);
        }
    }
}
