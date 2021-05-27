package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class ApproachCommandTest {

    private GamePlan gamePlan;
    private ApproachCommand approachCommand;
    private Room room;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        gamePlan = game.getGamePlan();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        // later will be set to false in "room was not examined yet" test
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        approachCommand = new ApproachCommand(gamePlan);
    }

    @Test
    @DisplayName("command name test")
    void getCommandNameTest() {
        assertEquals(approachCommand.getCommandName(), CommandName.APPROACH.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescriptionTest() {
        assertEquals(approachCommand.getCommandName(), CommandName.APPROACH.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void approachWithZeroParameters() {
            String expectedOutput =
                "Nerozumiem ti! K čomu to mám pristúpiť? Skús byť trochu viac konkrétnejší...";
            String actualOutput = approachCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 1 parameter")
        void approachWithMoreThanOneParameter(String parameter) {
            String[] parameters = {parameter, parameter};

            String expectedOutput =
                "No tak, spomaľ trochu, a vyber si iba jednu osobu alebo objekt, ku ktorému chceš pristúpiť!"
                + SystemInfo.LINE_SEPARATOR
                + "Nie si predsa elektrón vo svete kvantovej mechaniky a nemôžeš byť naraz na dvoch rôznych miestach";
            String actualOutput = approachCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
        }

        @Test
        @DisplayName("null as parameter")
        void approachWithNullAsParameter() {
            String expectedOutput = "Niečo sa pokazilo, zadaj, prosím ťa, ten príkaz ešte raz";
            String actualOutput = approachCommand.executeCommand(null);

            assertEquals(expectedOutput, actualOutput);
        }

        @Test
        @DisplayName("room was not examined yet")
        void approachWhenRoomWasNotExaminedYet() {
            gamePlan.getActualRoom().setWasRoomAlreadyExamined(false);

            String expectedOutput = "Asi by bolo vhodné sa najprv poriadne rozhliadnuť po miestnosti";
            String actualOutput = approachCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("VALID interactable object as parameter")
        void approachValidInteractableObject(InteractableObjectName object) {
            InteractableObject testingObject = new InteractableObject(object.toString(), null);
            String testingObjectName = testingObject.getName();
            room.addInteractableObjectToRoom(testingObject);

            String expectedOutput = "Pristupuješ k " + testingObjectName;
            String actualOutput =
                approachCommand.executeCommand(
                    InteractableObjectName.getInteractableObjectName(object.toString()));

            assertEquals(expectedOutput, actualOutput);
            // actualNonPlayerCharacter who player stands by should be set to null after player approach
            // InteractableObject in room
            assertNull(gamePlan.getActualNonPlayerCharacter());
            assertEquals(testingObject, gamePlan.getActualInteractableObject());
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("NON-VALID interactable object as parameter")
            // interactable objects from enum will be not added to room in this test
        void approachNonValidInteractableObject(InteractableObjectName object) {
            String expectedOutput = "Žiadny taký objekt alebo osobu tu nevidím! Nepomýlil si sa náhodou?";
            String actualOutput =
                approachCommand.executeCommand(
                    InteractableObjectName.getInteractableObjectName(object.toString()));

            assertEquals(expectedOutput, actualOutput);
            // both actualNonPlayerCharacter and actualInteractableObject should be null, as player
            // haven't approached anything yet
            assertNull(gamePlan.getActualNonPlayerCharacter());
            assertNull(gamePlan.getActualInteractableObject());
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("VALID NPC as parameter")
        void approachValidNonPlayerCharacter(NonPlayerCharacterName character) {
            NonPlayerCharacter testingNPC = new NonPlayerCharacter(character.toString());
            String testingNPCName = testingNPC.getName();
            room.addNonPlayerCharacterToRoom(testingNPC);

            String expectedOutput = "Pristupuješ k " + testingNPCName;
            String actualOutput =
                approachCommand.executeCommand(
                    NonPlayerCharacterName.getNonPlayerCharacterName(character.toString()));

            assertEquals(expectedOutput, actualOutput);
            // actualInteractableObject who player stands by should be set to null after player approach
            // NPC in room
            assertEquals(testingNPC, gamePlan.getActualNonPlayerCharacter());
            assertNull(gamePlan.getActualInteractableObject());
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("NON-VALID NPC as parameter")
        void approachNonValidNonPlayerCharacter(NonPlayerCharacterName character) {
            String expectedOutput = "Žiadny taký objekt alebo osobu tu nevidím! Nepomýlil si sa náhodou?";
            String actualOutput =
                approachCommand.executeCommand(
                    NonPlayerCharacterName.getNonPlayerCharacterName(character.toString()));

            assertEquals(expectedOutput, actualOutput);
            // both actualNonPlayerCharacter and actualInteractableObject should be null, as player
            // haven't approached anything yet
            assertNull(gamePlan.getActualNonPlayerCharacter());
            assertNull(gamePlan.getActualInteractableObject());
        }
    }
}
