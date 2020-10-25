package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class UseCommandTest {

    private GamePlan gamePlan;
    private UseCommand useCommand;
    private Room room;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        GameImpl game = new GameImpl();
        gamePlan = game.getGamePlan();
        inventory = gamePlan.getPlayer().getInventory();
        room = gamePlan.getActualRoom();

        useCommand = new UseCommand(gamePlan);
    }

    @Test
    @DisplayName("command name test")
    void getCommandName() {
        assertEquals(useCommand.getCommandName(), CommandName.USE.getCommandName());
    }

    @Test
    @DisplayName("command description test")
    void getCommandDescription() {
        assertEquals(useCommand.getCommandName(), CommandName.USE.getCommandName());
    }

    @Nested
    class ExecuteCommand {

        @Test
        @DisplayName("INVALID usage: zero parameters")
        void useWithZeroParameters() {
            String expectedOutput =
                "Bez uvedenia toho, ktorý predmet zo svojho batohu chceš použiť, to naozaj nepôjde";
            String actualOutput = useCommand.executeCommand();

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"abcdefg", "!@#$%^&**()", "null", "name with space"})
        @DisplayName("INVALID usage: more than 1 parameter")
        void useWithMoreThanOneParameter(String parameter) {
            String[] parameters = {parameter, parameter};

            String expectedOutput = "Nie tak zhurta! Dokážem pracovať iba s jedným predmetom";
            String actualOutput = useCommand.executeCommand(parameters);

            assertEquals(expectedOutput, actualOutput);
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("player IS NOT standing to any interactable object or NPC")
        void useWhileNotStandingNextToInteractableObjectOrNPC(ItemName itemName) {
            // following two items have special usage and will be skipped for this test case
            if (!itemName.toString().equals("MEDICAL_MASK")
                && !itemName.toString().equals("PROTECTIVE_MEDICAL_SUIT")) {
                Item itemToUse = new Item(itemName.toString(), null);
                inventory.addItemToInventory(itemToUse);

                String expectedOutput =
                    "Nenapadá mi ako tento predmet využiť, čo tak najprv pristúpiť k nejakej osobe alebo objektu a skúsiť to znovu?";
                String actualOutput = useCommand.executeCommand(itemToUse.getName());

                assertEquals(expectedOutput, actualOutput);
                assertEquals(
                    itemToUse,
                    inventory.getItemByName(
                        itemToUse.getName())); // item should remain in player's inventory
            }
        }

        @Test
        @DisplayName("use medical mask")
        void useMedicalMask() {
            Item itemToUse = new Item(ItemName.MEDICAL_MASK.toString(), null);
            inventory.addItemToInventory(itemToUse);

            int expectedNumberOfItemInRoom = room.getNumberOfItemsInRoom();
            String expectedOutput =
                "Nasádzaš si rúško. Povinnosť nosiť rúško už síce pominula, ale koronavírus rozhodne nie!";
            String actualOutput = useCommand.executeCommand(itemToUse.getName());

            assertEquals(expectedOutput, actualOutput);
            assertTrue(gamePlan.getPlayer().isPlayerWearingMedicalMask());
            assertFalse(
                inventory.isItemInInventory(itemToUse.getName())); // medical mask should be removed
            assertEquals(
                expectedNumberOfItemInRoom,
                room.getNumberOfItemsInRoom()); // medical mask should not be added to actual game room
        }

        @Test
        @DisplayName("use protective medical suit")
        void useProtectiveMedicalSuit() {
            Item itemToUse = new Item(ItemName.PROTECTIVE_MEDICAL_SUIT.toString(), null);
            inventory.addItemToInventory(itemToUse);

            int expectedNumberOfItemInRoom = room.getNumberOfItemsInRoom();
            String expectedOutput =
                "Obliekaš si celotelový ochranný oblek. Možno na konci skončíš zavalený sutinami budovy, ale negatívny na Covid-19!";
            String actualOutput = useCommand.executeCommand(itemToUse.getName());

            assertEquals(expectedOutput, actualOutput);
            assertTrue(gamePlan.getPlayer().isPlayerWearingMedicalSuit());
            assertFalse(
                inventory.isItemInInventory(
                    itemToUse.getName())); // protective medical suit should be removed
            assertEquals(
                expectedNumberOfItemInRoom,
                room.getNumberOfItemsInRoom()); // protective medical suit should not be added to actual
            // game room
        }

        @Test
        @DisplayName("use medical mask when player is already wearing protective medical suit")
        void useMedicalMaskWithAlreadyEquippedProtectiveMedicalSuit() {
            Item itemToUse = new Item(ItemName.MEDICAL_MASK.toString(), null);
            inventory.addItemToInventory(itemToUse);
            gamePlan.getPlayer().setIsPlayerWearingMedicalSuit(true);

            int expectedNumberOfItemInRoom = room.getNumberOfItemsInRoom();
            String expectedOutput =
                "Opatrnosti nikdy nie je dosť a preto si sa rozhodol nasadiť si k celotelovému obleku aj rúško";
            String actualOutput = useCommand.executeCommand(itemToUse.getName());

            assertEquals(expectedOutput, actualOutput);
            assertTrue(gamePlan.getPlayer().isPlayerWearingMedicalMask());
            assertTrue(gamePlan.getPlayer().isPlayerWearingMedicalSuit());
            assertFalse(
                inventory.isItemInInventory(itemToUse.getName())); // medical mask should be removed
            assertEquals(
                expectedNumberOfItemInRoom,
                room.getNumberOfItemsInRoom()); // medical mask should not be added to actual game room
        }

        @ParameterizedTest
        @EnumSource(ItemName.class)
        @DisplayName("use ITEM which IS NOT IN INVENTORY")
        void useItemWhichIsNotInInventory(ItemName itemName) {
            Item itemToUse = new Item(itemName.toString(), null);

            int expectedNumberOfItemInRoom = room.getNumberOfItemsInRoom();
            String expectedOutput =
                "Tento predmet v tvojom batohu nevidím! Nezabudol si ho náhodou v nejakej inej miestnosti?";
            String actualOutput = useCommand.executeCommand(itemToUse.getName());

            assertEquals(expectedOutput, actualOutput);
            assertEquals(
                expectedNumberOfItemInRoom,
                room.getNumberOfItemsInRoom()); // no items should be added to actual game room
        }

        @ParameterizedTest
        @ValueSource(strings = {"ROPE", "PEN", "FIRE_EXTINGUISHER"})
        @DisplayName("use item to attack cleaning lady")
        void useItemToAttackCleaningLady(String itemName) {
            Item itemToUse = new Item(itemName, null);
            inventory.addItemToInventory(itemToUse);
            NonPlayerCharacter cleaningLady = new NonPlayerCharacter("CLEANING_LADY");
            cleaningLady.setSpecialActions(
                SpecialActions.CLEANING_LADY.getItemsWithActionTheyPerform(),
                SpecialActions.CLEANING_LADY.getRoomToUnlockAfterItemUsage(),
                SpecialActions.CLEANING_LADY.getItemsToAddToRoomAfterItemUsage(),
                SpecialActions.CLEANING_LADY.getOutputTextAfterItemUsage());
            room.addNonPlayerCharacterToRoom(cleaningLady);
            gamePlan.setActualNonPlayerCharacter(cleaningLady);

            useCommand.executeCommand(itemToUse.getName());

            assertTrue(gamePlan.getPlayer().hasPlayerAttackedCleaningLady());
        }

        @ParameterizedTest
        @ValueSource(strings = {"ROPE", "PEN", "FIRE_EXTINGUISHER"})
        @DisplayName("use item to attack IT admin")
        void useItemToAttackITAdmin(String itemName) {
            Item itemToUse = new Item(itemName, null);
            inventory.addItemToInventory(itemToUse);
            NonPlayerCharacter admin = new NonPlayerCharacter("IT_ADMIN");
            admin.setSpecialActions(
                SpecialActions.IT_ADMIN.getItemsWithActionTheyPerform(),
                SpecialActions.IT_ADMIN.getRoomToUnlockAfterItemUsage(),
                SpecialActions.IT_ADMIN.getItemsToAddToRoomAfterItemUsage(),
                SpecialActions.IT_ADMIN.getOutputTextAfterItemUsage());
            room.addNonPlayerCharacterToRoom(admin);
            gamePlan.setActualNonPlayerCharacter(admin);
            // ,,move" to office where IT admin sits for correct test run
            gamePlan.setActualRoom(
                room.getNeighboringRoomByName("chodba_na_II._poschodi")
                    .getNeighboringRoomByName("chodba_na_I._poschodi")
                    .getNeighboringRoomByName("kancelaria"));

            useCommand.executeCommand(itemToUse.getName());

            assertTrue(gamePlan.getPlayer().hasPlayerAttackedITAdmin());
        }

        @ParameterizedTest
        @ValueSource(strings = {"ROPE", "PEN", "FIRE_EXTINGUISHER"})
        @DisplayName("use item to attack doorkeeper")
        void useItemToAttackDoorkeeper(String itemName) {
            Item itemToUse = new Item(itemName, null);
            inventory.addItemToInventory(itemToUse);
            NonPlayerCharacter doorkeeper = new NonPlayerCharacter("DOOR_KEEPER");
            doorkeeper.setSpecialActions(
                SpecialActions.DOOR_KEEPER.getItemsWithActionTheyPerform(),
                SpecialActions.DOOR_KEEPER.getRoomToUnlockAfterItemUsage(),
                SpecialActions.DOOR_KEEPER.getItemsToAddToRoomAfterItemUsage(),
                SpecialActions.DOOR_KEEPER.getOutputTextAfterItemUsage());
            room.addNonPlayerCharacterToRoom(doorkeeper);
            gamePlan.setActualNonPlayerCharacter(doorkeeper);
            // ,,move" to courtyard where door keeper sits for correct test run
            gamePlan.setActualRoom(
                room.getNeighboringRoomByName("chodba_na_II._poschodi")
                    .getNeighboringRoomByName("chodba_na_I._poschodi")
                    .getNeighboringRoomByName("Nova_budova")
                    .getNeighboringRoomByName("dvor"));

            useCommand.executeCommand(itemToUse.getName());

            assertTrue(gamePlan.getPlayer().hasPlayerAttackedDoorKeeper());
        }

        @ParameterizedTest
        @ValueSource(strings = {"STOLEN_MONEY"})
        @DisplayName("use stolen money to bribe doorkeeper")
        void useStolenMoneyToBribeDoorkeeper(String itemName) {
            Item itemToUse = new Item(itemName, null);
            inventory.addItemToInventory(itemToUse);
            NonPlayerCharacter doorkeeper = new NonPlayerCharacter("DOOR_KEEPER");
            doorkeeper.setSpecialActions(
                SpecialActions.DOOR_KEEPER.getItemsWithActionTheyPerform(),
                SpecialActions.DOOR_KEEPER.getRoomToUnlockAfterItemUsage(),
                SpecialActions.DOOR_KEEPER.getItemsToAddToRoomAfterItemUsage(),
                SpecialActions.DOOR_KEEPER.getOutputTextAfterItemUsage());
            room.addNonPlayerCharacterToRoom(doorkeeper);
            gamePlan.setActualNonPlayerCharacter(doorkeeper);
            // ,,move" to courtyard where door keeper sits for correct test run
            gamePlan.setActualRoom(
                room.getNeighboringRoomByName("chodba_na_II._poschodi")
                    .getNeighboringRoomByName("chodba_na_I._poschodi")
                    .getNeighboringRoomByName("Nova_budova")
                    .getNeighboringRoomByName("dvor"));

            useCommand.executeCommand(itemToUse.getName());

            assertTrue(gamePlan.getPlayer().hasPlayerBribedDoorKeeper());
        }

        @ParameterizedTest
        @ValueSource(strings = {"ROPE"})
        @DisplayName("use rope to escape through window")
        void useRopeToEscapeThroughWindow(String itemName) {
            Item itemToUse = new Item(itemName, null);
            inventory.addItemToInventory(itemToUse);

            InteractableObject window = new InteractableObject("WINDOW", null);
            room.addInteractableObjectToRoom(window);
            gamePlan.setActualInteractableObject(window);

            useCommand.executeCommand(itemToUse.getName());

            assertTrue(gamePlan.getPlayer().hasPlayerEscapedUsingWindow());
            assertTrue(gamePlan.hasPlayerReachedFinalRoom());
        }
    }
}
