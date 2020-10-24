package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class RoomTest {

    private Room testRoom;
    private Item testItem;
    private InteractableObject testObject;
    private NonPlayerCharacter testNPC;

    @BeforeEach
    void setUp() {
        testRoom = new Room("RB_201", false);
    }

    private static Stream<Arguments> provideRoomInstancesWithTheirCorrespondingEnumConstant() {
        return Stream.of(
            Arguments.of(new Room(RoomName.RB_202.toString(), false), RoomName.RB_202.toString()),
            Arguments.of(
                new Room(RoomName.FIRST_FLOOR_HALL.toString(), false),
                RoomName.FIRST_FLOOR_HALL.toString()),
            Arguments.of(
                new Room(RoomName.SECOND_FLOOR_HALL.toString(), false),
                RoomName.SECOND_FLOOR_HALL.toString()),
            Arguments.of(new Room(RoomName.TOILETS.toString(), false), RoomName.TOILETS.toString()),
            Arguments.of(new Room(RoomName.OFFICE.toString(), false), RoomName.OFFICE.toString()),
            Arguments.of(
                new Room(RoomName.DRESSING_ROOM.toString(), false),
                RoomName.DRESSING_ROOM.toString()),
            Arguments.of(
                new Room(RoomName.NEW_BUILDING.toString(), false),
                RoomName.NEW_BUILDING.toString()),
            Arguments.of(
                new Room(RoomName.OLD_BUILDING.toString(), false),
                RoomName.OLD_BUILDING.toString()),
            Arguments.of(new Room(RoomName.LIBRARY.toString(), false), RoomName.LIBRARY.toString()),
            Arguments
                .of(new Room(RoomName.COURTYARD.toString(), false), RoomName.COURTYARD.toString()),
            Arguments.of(
                new Room(RoomName.LECTURE_ROOM.toString(), false),
                RoomName.LECTURE_ROOM.toString()),
            Arguments.of(new Room(RoomName.STREET.toString(), false), RoomName.STREET.toString()));
    }

    @Nested
    class RoomMethods {

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.RoomTest#provideRoomInstancesWithTheirCorrespondingEnumConstant")
        @DisplayName("get room name")
        void getName(Room room, String roomEnumConstant) {
            String expectedOutput = RoomName.getRoomName(roomEnumConstant);
            String actualOutput = room.getName();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.RoomTest#provideRoomInstancesWithTheirCorrespondingEnumConstant")
        @DisplayName("get room description")
        void getDescription(Room room, String roomEnumConstant) {
            String expectedOutput = RoomDescription.getRoomDescription(roomEnumConstant);
            String actualOutput = room.getDescription();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.RoomTest#provideRoomInstancesWithTheirCorrespondingEnumConstant")
        @DisplayName("get room description together with room exits")
        void getRoomDescriptionWithExits(Room room, String roomEnumConstant) {
            room.addExitFromRoom(testRoom);

            String expectedOutput =
                "Vstupuješ do miestnosti/priestoru "
                + RoomName.getRoomName(roomEnumConstant)
                + ", "
                + RoomDescription.getRoomDescription(roomEnumConstant)
                + "\n"
                + "Východy: "
                + room.getNeighboringRoomNames();
            String actualOutput = room.getRoomDescriptionWithExits();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.RoomTest#provideRoomInstancesWithTheirCorrespondingEnumConstant")
        @DisplayName("addExitFromRoom, getNeighboringRoomNames, getNeighboringRoomByName methods test")
        void exitFromRoomOperations(Room room, String roomEnumConstant) {
            testRoom.addExitFromRoom(room);

            String expectedOutput = RoomName.getRoomName(roomEnumConstant);
            String actualOutput = testRoom.getNeighboringRoomNames();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(room, testRoom.getNeighboringRoomByName(room.getName()));
        }
    }

    @Nested
    class ItemsManipulationMethods {

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("is item in room")
        void isItemInRoom(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);

            assertTrue(testRoom.isItemInRoom(testItem.getName()));
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("get number of items in room")
        void getNumberOfItemsInRoom(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);

            assertEquals(1, testRoom.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("get item by name")
        void getItemByName(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);

            assertEquals(testItem, testRoom.getItemByName(testItem.getName()));
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("add item room")
        void addItemToRoom(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);

            assertEquals(1, testRoom.getNumberOfItemsInRoom());
            assertEquals(testItem, testRoom.getItemByName(testItem.getName()));
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("remove item from room")
        void removeItemFromRoom(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);
            testRoom.removeItemFromRoom(testItem);

            assertEquals(0, testRoom.getNumberOfItemsInRoom());
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("get item names")
        void getItemNames(ItemName itemName) {
            testItem = new Item(itemName.toString(), null);
            testRoom.addItemToRoom(testItem);

            String expectedOutput = testItem.getName();
            String actualOutput = testRoom.getItemNames();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(1, testRoom.getNumberOfItemsInRoom());
        }
    }

    @Nested
    class InteractableObjectManipulationMethods {

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("is interactable object in room")
        void isInteractableObjectInRoom(InteractableObjectName objectName) {
            testObject = new InteractableObject(objectName.toString(), null);
            testRoom.addInteractableObjectToRoom(testObject);

            assertTrue(testRoom.isInteractableObjectInRoom(testObject.getName()));
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("get number of interactable objects in room")
        void getNumberOfInteractableObjectsInRoom(InteractableObjectName objectName) {
            testObject = new InteractableObject(objectName.toString(), null);
            testRoom.addInteractableObjectToRoom(testObject);

            assertEquals(1, testRoom.getNumberOfInteractableObjectsInRoom());
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("get interactable object by name")
        void getInteractableObjectByName(InteractableObjectName objectName) {
            testObject = new InteractableObject(objectName.toString(), null);
            testRoom.addInteractableObjectToRoom(testObject);

            assertEquals(testObject, testRoom.getInteractableObjectByName(testObject.getName()));
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("add interactable object to room")
        void addInteractableObjectToRoom(InteractableObjectName objectName) {
            testObject = new InteractableObject(objectName.toString(), null);
            testRoom.addInteractableObjectToRoom(testObject);

            assertEquals(1, testRoom.getNumberOfInteractableObjectsInRoom());
            assertEquals(testObject, testRoom.getInteractableObjectByName(testObject.getName()));
        }

        @ParameterizedTest
        @EnumSource(InteractableObjectName.class)
        @DisplayName("get interactable object names")
        void getInteractableObjectNames(InteractableObjectName objectName) {
            testObject = new InteractableObject(objectName.toString(), null);
            testRoom.addInteractableObjectToRoom(testObject);

            String expectedOutput = testObject.getName();
            String actualOutput = testRoom.getInteractableObjectNames();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(1, testRoom.getNumberOfInteractableObjectsInRoom());
        }
    }

    @Nested
    class NonPlayerCharacterManipulationMethods {

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("is non-player character in room")
        void isNonPlayerCharacterInRoom(NonPlayerCharacterName npcName) {
            testNPC = new NonPlayerCharacter(npcName.toString());
            testRoom.addNonPlayerCharacterToRoom(testNPC);

            assertTrue(testRoom.isNonPlayerCharacterInRoom(testNPC.getName()));
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("get number of non-player characters in room")
        void getNumberOfNonPlayerCharactersInRoom(NonPlayerCharacterName npcName) {
            testNPC = new NonPlayerCharacter(npcName.toString());
            testRoom.addNonPlayerCharacterToRoom(testNPC);

            assertEquals(1, testRoom.getNumberOfNonPlayerCharactersInRoom());
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("get non-player character by name")
        void getNonPlayerCharacterByName(NonPlayerCharacterName npcName) {
            testNPC = new NonPlayerCharacter(npcName.toString());
            testRoom.addNonPlayerCharacterToRoom(testNPC);

            assertEquals(testNPC, testRoom.getNonPlayerCharacterByName(testNPC.getName()));
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("add non-player character to room")
        void addNonPlayerCharacterToRoom(NonPlayerCharacterName npcName) {
            testNPC = new NonPlayerCharacter(npcName.toString());
            testRoom.addNonPlayerCharacterToRoom(testNPC);

            assertEquals(1, testRoom.getNumberOfNonPlayerCharactersInRoom());
            assertEquals(testNPC, testRoom.getNonPlayerCharacterByName(testNPC.getName()));
        }

        @ParameterizedTest
        @EnumSource(NonPlayerCharacterName.class)
        @DisplayName("get non-player character names")
        void getNonPlayerCharacterNames(NonPlayerCharacterName npcName) {
            testNPC = new NonPlayerCharacter(npcName.toString());
            testRoom.addNonPlayerCharacterToRoom(testNPC);

            String expectedOutput = testNPC.getName();
            String actualOutput = testRoom.getNonPlayerCharacterNames();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(1, testRoom.getNumberOfNonPlayerCharactersInRoom());
        }
    }
}
