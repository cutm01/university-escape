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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExamineObjectCommandTest {

    private GamePlan gamePlan;
    private ExamineObjectCommand examineObjectCommand;
    private Room room;
    private InteractableObject interactableObject;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        gamePlan = game.getGamePlan();
        interactableObject = new InteractableObject("DESK", null);

        // create room which will be used in tests (i.e. it is actual room where player is currently in)
        room = new Room("RB_201", false);
        room.setWasRoomAlreadyExamined(true);
        gamePlan.setActualRoom(room);

        examineObjectCommand = new ExamineObjectCommand(gamePlan);
    }

    private static Stream<Arguments> provideItemInstancesToCreateHiddenItemsInstances() {
        return Stream.of(
            Arguments.of(
                new Item(ItemName.BOTTLE.toString(), null),
                new Item(ItemName.PEN.toString(), null),
                new Item(ItemName.WALLET.toString(), null)),
            Arguments.of(
                new Item(ItemName.ID_CARD.toString(), null),
                new Item(ItemName.MONEY.toString(), null),
                new Item(ItemName.MEDICAL_MASK.toString(), null)),
            Arguments.of(
                new Item(ItemName.ROPE.toString(), null),
                new Item(ItemName.ISIC.toString(), null),
                new Item(ItemName.SMALL_SNACK.toString(), null)),
            Arguments.of(
                new Item(ItemName.FIRE_EXTINGUISHER.toString(), null),
                new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null),
                new Item(ItemName.MUSIC_CD.toString(), null)),
            Arguments.of(
                new Item(ItemName.BOOK.toString(), null),
                new Item(ItemName.STOLEN_WALLET.toString(), null),
                new Item(ItemName.STOLEN_MONEY.toString(), null)),
            Arguments.of(
                new Item(ItemName.STOLEN_ISIC.toString(), null),
                new Item(ItemName.KEYS.toString(), null),
                new Item(ItemName.PAPER_CLIP.toString(), null)));
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(
            examineObjectCommand.getCommandName(), CommandName.EXAMINE_OBJECT.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(
            examineObjectCommand.getCommandName(), CommandName.EXAMINE_OBJECT.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void examineObjectWithZeroParameters() {
            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Nerozumiem ti!  Upresni, prosím ťa, ktorý objekt z miestnosti chceš preskúmať";
            String actualOutput = examineObjectCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: two parameters")
        void examineObjectWithTwoParameters(String parameter) {
            String[] parameters = {parameter, parameter};

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "To tu chceš splašene behať z jedného miesta na druhé? Lepšie by bolo preskúmavať naraz iba jeden objekt";
            String actualOutput = examineObjectCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("try examine interactable object which is NOT in room")
        void examineInteractableObjectWhichIsNotInRoom(String objectName) {
            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Nič také tu naozaj nevidím, skús sa ešte raz rozhliadnuť po miestnosti a osviežiť si pamäť";
            String actualOutput = examineObjectCommand.executeCommand(objectName);

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest(name = "examined object: {0}")
        @EnumSource(InteractableObjectName.class)
        @DisplayName("examine interactable object which player DID NOT approach firstly")
        void examineInteractableObjectWhichPlayerDidNotApproachFirstly(
            InteractableObjectName objectName) {
            interactableObject = new InteractableObject(objectName.toString(), null);
            room.addInteractableObjectToRoom(interactableObject);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Myslíš si snáď, že ti ten výbuch priniesol schopnosť telekinézy?"
                + SystemInfo.LINE_SEPARATOR
                + "Takto z diaľky to naozaj preskúmať nepôjde, pristúp, prosím ťa bližšie k objektu";
            String actualOutput = examineObjectCommand.executeCommand(interactableObject.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest(name = "examined object: {0}")
        @EnumSource(InteractableObjectName.class)
        @DisplayName("examine interactable object WITHOUT examination of room before")
        void examineInteractableObjectInNonExaminedRoom(InteractableObjectName objectName) {
            interactableObject = new InteractableObject(objectName.toString(), null);
            room.addInteractableObjectToRoom(interactableObject);
            room.setWasRoomAlreadyExamined(false);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Si si istý, že sa tu ten objekt skutočne nachádza? Skús sa najprv poriadne rozhliadnuť po miestnosti";
            String actualOutput = examineObjectCommand.executeCommand(interactableObject.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest(name = "examined object: {0}")
        @EnumSource(InteractableObjectName.class)
        @DisplayName("examine interactable object which WAS ALREADY EXAMINED")
        void examineAlreadyExaminedInteractableObject(InteractableObjectName objectName) {
            interactableObject = new InteractableObject(objectName.toString(), null);
            interactableObject.setWasInteractableObjectAlreadyExamined(true);
            room.addInteractableObjectToRoom(interactableObject);
            gamePlan.setActualInteractableObject(interactableObject);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Tento objekt si už preskúmal v minulosti, nič nového, čo by ti pomohlo na ňom nenájdeš!";
            String actualOutput = examineObjectCommand.executeCommand(interactableObject.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
        }

        @ParameterizedTest
        @MethodSource(
            "cz.vse.java.cutm01.adventure.gamelogic.ExamineObjectCommandTest#provideItemInstancesToCreateHiddenItemsInstances")
        @DisplayName("examine interactable object WITH hidden items")
        void examineInteractableObjectWithHiddenItems(
            Item hiddenItem1, Item hiddenItem2, Item hiddenItem3) {
            Item[] itemsToHide = {hiddenItem1, hiddenItem2, hiddenItem3};
            HiddenItems hiddenItems = new HiddenItems(itemsToHide);
            interactableObject = new InteractableObject("DESK", null);
            interactableObject.setHiddenItems(hiddenItems);
            room.addInteractableObjectToRoom(interactableObject);
            gamePlan.setActualInteractableObject(interactableObject);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Prezeráš "
                + interactableObject.getName()
                + " zo všetkých strán a vyzerá to, že niečo skrýva (váha predmetu):"
+ SystemInfo.LINE_SEPARATOR
                + interactableObject.getHiddenItems().getHiddenItemsDescription()
                + SystemInfo.LINE_SEPARATOR
                + "Rozmýšľaš, ako tieto veci môžeš ďalej využiť, no v tom sa z diaľky ozve hlasný krik a nájdené"
+ SystemInfo.LINE_SEPARATOR
                + "veci ti vypadnú z rúk a povaľujú sa po miestnosti. Vezmi si nejaké! Možno sa ti budú neskôr hodiť";
            String actualOutput = examineObjectCommand.executeCommand(interactableObject.getName());

            assertEquals(expectedOutput, actualOutput);
            // hidden items should be added to room after examination
            assertEquals(expectedNumberOfItemsInRoom, room.getNumberOfItemsInRoom());
            assertEquals(hiddenItem1, room.getItemByName(hiddenItem1.getName()));
            assertEquals(hiddenItem2, room.getItemByName(hiddenItem2.getName()));
            assertEquals(hiddenItem3, room.getItemByName(hiddenItem3.getName()));
            assertEquals(0, interactableObject.getHiddenItems().getNumberOfHiddenItems());
            // object should be set to "wasAlreadyExamined = true" and remain in room after examination
            assertEquals(
                interactableObject, room.getInteractableObjectByName(interactableObject.getName()));
            assertTrue(interactableObject.getWasInteractableObjectAlreadyExamined());
        }

        @ParameterizedTest(name = "examined object: {0}")
        @EnumSource(InteractableObjectName.class)
        @DisplayName("examine interactable object WITHOUT hidden items")
        void examineInteractableObjectWithoutHiddenItems(InteractableObjectName objectName) {
            interactableObject = new InteractableObject(objectName.toString(), null);
            room.addInteractableObjectToRoom(interactableObject);
            gamePlan.setActualInteractableObject(interactableObject);

            int expectedNumberOfItemsInRoom =
                room.getNumberOfItemsInRoom()
                + interactableObject.getHiddenItems().getNumberOfHiddenItems();
            String expectedOutput =
                "Podrobne preskúmavaš objekt "
                + interactableObject.getName()
                + ",no nenašiel si nič, čo by ti mohlo pomôcť";
            String actualOutput = examineObjectCommand.executeCommand(interactableObject.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemsInRoom,
                room.getNumberOfItemsInRoom()); // no hidden item should be added to room after
            // examination
            assertEquals(
                interactableObject,
                room.getInteractableObjectByName(
                    interactableObject
                        .getName())); // object should remain in room after examination
            assertTrue(
                interactableObject
                    .getWasInteractableObjectAlreadyExamined()); // should be set to true, even if object
            // does not have any hidden items in it
        }
    }
}
