package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class GoCommandTest {

    private GamePlan gamePlan;
    private GoCommand goCommand;
    private Room room;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        gamePlan = game.getGamePlan();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        gamePlan.setActualRoom(room);

        goCommand = new GoCommand(gamePlan);
    }

    private static Stream<Arguments> provideRoomInstances() {
        return Stream.of(
            Arguments.of(new Room(RoomName.RB_202.toString(), false)),
            Arguments.of(new Room(RoomName.FIRST_FLOOR_HALL.toString(), false)),
            Arguments.of(new Room(RoomName.SECOND_FLOOR_HALL.toString(), false)),
            Arguments.of(new Room(RoomName.TOILETS.toString(), false)),
            Arguments.of(new Room(RoomName.OFFICE.toString(), false)),
            Arguments.of(new Room(RoomName.DRESSING_ROOM.toString(), false)),
            Arguments.of(new Room(RoomName.NEW_BUILDING.toString(), false)),
            Arguments.of(new Room(RoomName.OLD_BUILDING.toString(), false)),
            Arguments.of(new Room(RoomName.LIBRARY.toString(), false)),
            Arguments.of(new Room(RoomName.COURTYARD.toString(), false)),
            Arguments.of(new Room(RoomName.LECTURE_ROOM.toString(), false)),
            Arguments.of(new Room(RoomName.STREET.toString(), false)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(goCommand.getCommandName(), CommandName.GO.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(goCommand.getCommandName(), CommandName.GO.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void goWithZeroParameters() {
            String expectedOutput = "Do akej miestnosti chceš ísť? Upresni to, prosím ťa";
            String actualOutput = goCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 1 parameter")
        void goWithMoreThanParameter(String parameter) {
            String[] parameters = {parameter, parameter};

            String expectedOutput = "Spomaľ trochu a vyber si iba jednu miestnosť, do ktorej chceš ísť";
            String actualOutput = goCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource("cz.vse.java.cutm01.adventure.gamelogic.GoCommandTest#provideRoomInstances")
        @DisplayName("go to room which IS NOT neighboring room to actual room")
        void goToNotNeighboringRoom(Room neighboringRoom) {
            String expectedOutput = "Vstup do tejto miestnosti tu nevidím, skús to znovu";
            String actualOutput = goCommand.executeCommand(neighboringRoom.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), room);
        }

        @ParameterizedTest
        @ValueSource(strings = {"RB_202", "COURTYARD", "STREET"})
        @DisplayName("go to LOCKED room")
        void goToLockedRoom(String lockedRoomName) {
            Room lockedRoom = new Room(lockedRoomName, true);
            room.addExitFromRoom(lockedRoom);

            String expectedOutput = LockedRoomDescription.getLockedRoomDescription(lockedRoomName);
            String actualOutput = goCommand.executeCommand(lockedRoom.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), room);
        }

        @Test
        @DisplayName("go to room STREET (i.e. final room)")
        void goToFinalRoom() {
            Room street = new Room("STREET", false);
            room.addExitFromRoom(street);

            String expectedOutput =
                "Vstupuješ do miestnosti/priestoru "
                + street.getName()
                + ", "
                + street.getDescription()
                + "\n";
            String actualOutput = goCommand.executeCommand(street.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), street);
            assertTrue(gamePlan.getHasPlayerReachedFinalRoom());
        }

        @Test
        @DisplayName(
            "go to room OLD_BUILDING without wearing medical mask or suit - alternative ending should be set")
        void goToOldBuildingWithoutWearingMedicalMaskOrSuit() {
            Room oldBuilding = new Room("OLD_BUILDING", false);
            room.addExitFromRoom(oldBuilding);

            String expectedOutput =
                "Vstupuješ do miestnosti/priestoru "
                + oldBuilding.getName()
                + ", "
                + oldBuilding.getDescription()
                + "\n";
            String actualOutput = goCommand.executeCommand(oldBuilding.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), oldBuilding);
            assertTrue(gamePlan.getPlayer().getHasPlayerPassedByCoughingTeacher());
        }

        @Test
        @DisplayName(
            "go to room OLD_BUILDING WITH wearing medical mask - alternative ending should NOT be set")
        void goToOldBuildingWithWearingMedicalMaskOrSuit() {
            Room oldBuilding = new Room("OLD_BUILDING", false);
            room.addExitFromRoom(oldBuilding);
            gamePlan.getPlayer().setIsPlayerWearingMedicalMask(true);

            String expectedOutput =
                "Vstupuješ do miestnosti/priestoru "
                + oldBuilding.getName()
                + ", "
                + oldBuilding.getDescription()
                + "\n";
            String actualOutput = goCommand.executeCommand(oldBuilding.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), oldBuilding);
            assertFalse(gamePlan.getPlayer().getHasPlayerPassedByCoughingTeacher());
        }

        @ParameterizedTest
        @MethodSource("cz.vse.java.cutm01.adventure.gamelogic.GoCommandTest#provideRoomInstances")
        @DisplayName("go to room which IS neighboring room to actual room")
        void goToNeighboringRoom(Room neighboringRoom) {
            room.addExitFromRoom(neighboringRoom);

            String expectedOutput =
                "Vstupuješ do miestnosti/priestoru "
                + neighboringRoom.getName()
                + ", "
                + neighboringRoom.getDescription()
                + "\n";
            String actualOutput = goCommand.executeCommand(neighboringRoom.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(gamePlan.getActualRoom(), neighboringRoom);
        }
    }
}
