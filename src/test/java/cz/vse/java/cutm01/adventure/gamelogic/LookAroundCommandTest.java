package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

class LookAroundCommandTest {

    private LookAroundCommand lookAroundCommand;
    private Room room;

    //region STRING CONSTANTS USED IN TEST CASES
    /*
     * LookAroundCommand can be used only without parameter,
     * output depends on whether there are some NPCs, interactable objects
     * or items in current room and on whether room was already examined
     * using this command before
     */
    private static final String COMMAND_NAME = CommandName.LOOK_AROUND.getCommandName();
    private static final String COMMAND_DESCRIPTION = CommandDescription.LOOK_AROUND.getCommandDescription();

    //expected outputs after executing command
    private static final String MORE_THAN_ZERO_PARAMETERS_OUTPUT = "Zbytočne to komplikuješ! Skús zadať ten príkaz ešte raz bez akýchkoľvek parametrov";
    private static final String EMPTY_ROOM_OUTPUT = "Rozhliadol si sa po miestnosti, no neuvidel si nič, čo by ti pomohlo v tvojej snahe dostať sa von živý";
    private static final String ROOM_CONTAINING_ONLY_ITEMS_OUTPUT = "V miestnosti si si všimol nasledovné predmety: ";
    private static final String ROOM_CONTAINING_ONLY_INTERACTABLE_OBJECTS_OUTPUT = "Dobre sa rozhliadneš a uvidíš nasledovné objekty: ";
    private static final String ROOM_CONTAINING_ONLY_NON_PLAYER_CHARACTERS_OUTPUT = "Nie si tu sám, okrem teba je tu ešte ";
    private static final String ROOM_CONTAINING_ONLY_EXITS_OUTPUT = "Východy: ";

    //endregion STRING CONSTANTS USED IN TEST CASES

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        GamePlan gamePlan = game.getGamePlan();

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(false);
        gamePlan.setActualRoom(room);

        lookAroundCommand = new LookAroundCommand(gamePlan);
    }

    private static Stream<Arguments> provideRoomContent() {
        return Stream.of(
            Arguments.of(
                new Item(ItemName.BOTTLE.toString(), null),
                new InteractableObject(InteractableObjectName.BENCH.toString(), null),
                new NonPlayerCharacter(NonPlayerCharacterName.CLEANING_LADY.toString()),
                new Room(RoomName.RB_202.toString(), false)),
            Arguments.of(
                new Item(ItemName.PEN.toString(), null),
                new InteractableObject(InteractableObjectName.SNACK_VENDING_MACHINE.toString(),
                    null),
                new NonPlayerCharacter(NonPlayerCharacterName.COUGHING_TEACHER.toString()),
                new Room(RoomName.FIRST_FLOOR_HALL.toString(), false)),
            Arguments.of(
                new Item(ItemName.WALLET.toString(), null),
                new InteractableObject(InteractableObjectName.COAT_HANGER.toString(), null),
                new NonPlayerCharacter(NonPlayerCharacterName.IT_ADMIN.toString()),
                new Room(RoomName.SECOND_FLOOR_HALL.toString(), false)),
            Arguments.of(
                new Item(ItemName.ID_CARD.toString(), null),
                new InteractableObject(InteractableObjectName.BOOK_SHELF.toString(), null),
                new NonPlayerCharacter(NonPlayerCharacterName.DOOR_KEEPER.toString()),
                new Room(RoomName.TOILETS.toString(), false)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(lookAroundCommand.getCommandName(), CommandName.LOOK_AROUND.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(lookAroundCommand.getCommandName(), CommandName.LOOK_AROUND.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 0 parameters")
        void lookAroundWithMoreThanZeroParameters(String parameter) {
            String expectedOutput =
                "Zbytočne to komplikuješ! Skús zadať ten príkaz ešte raz bez akýchkoľvek parametrov";
            String actualOutput = lookAroundCommand.executeCommand(parameter);

            assertEquals(expectedOutput, actualOutput);
            assertFalse(room.wasRoomAlreadyExamined());
        }

        @Test
        @DisplayName("look around in room which DOES NOT CONTAIN ANYTHING")
        void lookAroundInEmptyRoom() {
            String expectedOutput =
                "Rozhliadol si sa po miestnosti, no neuvidel si nič, čo by ti pomohlo v tvojej snahe dostať sa von živý";
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("look around in room which CONTAINS ONLY ITEMS")
        void lookAroundInRoomContainingOnlyItems(ItemName itemName) {
            Item item = new Item(itemName.toString(), null);
            room.addItemToRoom(item);

            String expectedOutput =
                "V miestnosti si si všimol nasledovné predmety: " + item.getName();
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("look around in room which CONTAINS ONLY INTERACTABLE OBJECTS")
        void lookAroundInRoomContainingOnlyInteractableObjects(InteractableObjectName objectName) {
            InteractableObject interactableObject = new InteractableObject(objectName.toString(),
                null);
            room.addInteractableObjectToRoom(interactableObject);

            String expectedOutput =
                "Dobre sa rozhliadneš a uvidíš nasledovné objekty: " + interactableObject.getName();
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("look around in room which CONTAINS ONLY NON-PLAYER CHARACTERS")
        void lookAroundInRoomContainingOnlyNonPlayerCharacters(NonPlayerCharacterName npcName) {
            NonPlayerCharacter nonPlayerCharacter = new NonPlayerCharacter(npcName.toString());
            room.addNonPlayerCharacterToRoom(nonPlayerCharacter);

            String expectedOutput =
                "Nie si tu sám, okrem teba je tu ešte " + nonPlayerCharacter.getName();
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }

        @ParameterizedTest
        @EnumSource(RoomName.class)
        @DisplayName("look around in room which CONTAINS ONLY EXITS")
        void lookAroundInRoomContainingOnlyExits(RoomName exitRoomName) {
            Room exitRoom = new Room(exitRoomName.toString(), false);
            room.addExitFromRoom(exitRoom);

            String expectedOutput = "Východy: " + exitRoom.getName();
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }

        @ParameterizedTest
        @MethodSource("cz.vse.java.cutm01.adventure.gamelogic.LookAroundCommandTest#provideRoomContent")
        @DisplayName("look around in room which CONTAINS ITEMS, OBJECTS, NPCs and EXITS")
        void lookAroundInRoomContainingCombinationOfAllContent(
            Item item, InteractableObject object, NonPlayerCharacter npc, Room exitRoom) {
            room.addItemToRoom(item);
            room.addInteractableObjectToRoom(object);
            room.addNonPlayerCharacterToRoom(npc);
            room.addExitFromRoom(exitRoom);

            String expectedOutput =
                "V miestnosti si si všimol nasledovné predmety: "
                + item.getName()
                + SystemInfo.LINE_SEPARATOR
                + "Dobre sa rozhliadneš a uvidíš nasledovné objekty: "
                + object.getName()
                + SystemInfo.LINE_SEPARATOR
                + "Nie si tu sám, okrem teba je tu ešte "
                + npc.getName()
                + SystemInfo.LINE_SEPARATOR
                + "Východy: "
                + exitRoom.getName();
            String actualOutput = lookAroundCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertTrue(room.wasRoomAlreadyExamined());
        }
    }
}
