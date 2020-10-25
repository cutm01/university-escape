package cz.vse.java.cutm01.adventure.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.vse.java.cutm01.adventure.main.SystemInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameImplTest {

    private GameImpl game;

    @BeforeEach
    void setUp() {
        game = new GameImpl();
    }

    @Test
    @DisplayName("player escaped through courtyard, no NPC was attacked")
    void playerEscapedThroughCourtyardNoAttackedNPC() {
        Assertions.assertEquals(
            RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("penazenka"));

        game.parseUserInput("preskumaj_predmet penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("obciansky_preukaz"));

        game.parseUserInput("vezmi obciansky_preukaz");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        Assertions.assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        Assertions.assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi obciansky_preukaz");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through courtyard, cleaning lady was attacked")
    void playerEscapedThroughCourtyardCleaningLadyAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("penazenka"));

        game.parseUserInput("preskumaj_predmet penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("obciansky_preukaz"));

        game.parseUserInput("vezmi obciansky_preukaz");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("pristup_k upratovacka");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedCleaningLady());
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kluce"));
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("vezmi kluce");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kluce"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi kluce");
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kluce"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi obciansky_preukaz");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tej úbohej pani"
            + SystemInfo.LINE_SEPARATOR
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostala von živá,"
            + SystemInfo.LINE_SEPARATOR
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia."
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through courtyard, IT admin was attacked")
    void playerEscapedThroughCourtyardITAdminAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("penazenka"));

        game.parseUserInput("preskumaj_predmet penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("obciansky_preukaz"));

        game.parseUserInput("vezmi obciansky_preukaz");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));

        game.parseUserInput("vezmi pero");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi pero");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedITAdmin());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));

        game.parseUserInput("pristup_k stol");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("DESK"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt stol");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("spinka"));

        game.parseUserInput("vezmi spinka");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("spinka"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi spinka");
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("spinka"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi obciansky_preukaz");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tomu úbohému IT adminovi"
            + SystemInfo.LINE_SEPARATOR
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostal von živý,"
            + SystemInfo.LINE_SEPARATOR
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia."
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through courtyard, door keeper attacked")
    void playerEscapedThroughCourtyardDoorKeeperAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("pero"));

        game.parseUserInput("vezmi pero");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi pero");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedDoorKeeper());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Avšak...to si si naozaj myslel, že potom čo si urobil tomu vrátnikovi si iba tak spokojne odkráčaš domov?"
            + SystemInfo.LINE_SEPARATOR
            + "Z budovy si sa síce dostal, ale ešte máš čo vysvetľovať polícii"
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through courtyard, door keeper was bribed")
    void playerEscapedThroughCourtyardDoorKeeperBribed() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("penazenka"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("chod Vencovskeho_aula");
        assertEquals(
            RoomName.getRoomName("LECTURE_ROOM"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("cudzia_penazenka"));

        game.parseUserInput("preskumaj_predmet cudzia_penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("velky_obnos_penazi"));

        game.parseUserInput("vezmi velky_obnos_penazi");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi velky_obnos_penazi");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerBribedDoorKeeper());
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Vyzerá to, že si ťa niekto všimol pri tvojom pokuse uplatiť vrátnika, ba čo viac, peniaze, ktorými"
            + SystemInfo.LINE_SEPARATOR
            + "si sa pokúšal podplatiť vrátnika, zrejme patrili práve tejto osobe a ty tak ihneď po uniknutí"
            + SystemInfo.LINE_SEPARATOR
            + "z budovy školy putuješ na políciu, kde budeš mať čo vysvetľovať."
            + SystemInfo.LINE_SEPARATOR
            + "Do budúcna by sa určite šlo dostať von aj iným spôsobom!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through courtyard and get COVID-19")
    void playerEscapedThroughCourtyardAndGetCovid() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("penazenka"));

        game.parseUserInput("preskumaj_predmet penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("obciansky_preukaz"));

        game.parseUserInput("vezmi obciansky_preukaz");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        // go to Old Building through Connecting Corridor and return back same way
        // player "get" Covid-19 as he passed Coughing Teacher in Connecting Corridor without wearing
        // medical mask or suit
        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());
        // ---------------------------------------------------------------------------------------------------------
        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi obciansky_preukaz");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("obciansky_preukaz"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod ulica");
        assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());

        String expectedGameEnding =
            "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik... po 2 týždňoch sa u teba začali prejavovať respiračné problémy"
            + SystemInfo.LINE_SEPARATOR
            + "a musel si byť hospitalizovaný v Nemocnici Na Bulovce."
            + SystemInfo.LINE_SEPARATOR
            + "Nabudúce si dávaj väčší pozor okolo koho prechádzaš, a zváž či si nenasadiť nejaký ochranný prvok!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window, no NPC was attacked")
    void playerEscapedThroughWindowNoAttackedNPC() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("lavicka"));

        game.parseUserInput("pristup_k lavicka");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BENCH"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt lavicka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("rusko"));

        game.parseUserInput("vezmi rusko");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("pouzi rusko");
        assertTrue(game.getGamePlan().getPlayer().isPlayerWearingMedicalMask());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kniznica");
        assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(
            game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

        game.parseUserInput("pristup_k policka_s_knihami");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt policka_s_knihami");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

        game.parseUserInput("vezmi kniha_algoritmov");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi kniha_algoritmov");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window, cleaning lady was attacked")
    void playerEscapedThroughWindowCleaningLadyAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("lavicka"));

        game.parseUserInput("pristup_k lavicka");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BENCH"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt lavicka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("rusko"));

        game.parseUserInput("vezmi rusko");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("pouzi rusko");
        assertTrue(game.getGamePlan().getPlayer().isPlayerWearingMedicalMask());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kniznica");
        assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(
            game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

        game.parseUserInput("pristup_k policka_s_knihami");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt policka_s_knihami");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

        game.parseUserInput("vezmi kniha_algoritmov");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi kniha_algoritmov");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        // player attacked cleaning lady
        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("pristup_k upratovacka");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedCleaningLady());
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kluce"));
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());
        // ----------------------------------------------------------------------------------------------------------

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tej úbohej pani"
            + SystemInfo.LINE_SEPARATOR
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostala von živá,"
            + SystemInfo.LINE_SEPARATOR
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia."
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window, IT admin was attacked")
    void playerEscapedThroughWindowITAdminAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("pero"));

        game.parseUserInput("vezmi pero");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("lavicka"));

        game.parseUserInput("pristup_k lavicka");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BENCH"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt lavicka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("rusko"));

        game.parseUserInput("vezmi rusko");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("pouzi rusko");
        assertTrue(game.getGamePlan().getPlayer().isPlayerWearingMedicalMask());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        // player attacked IT admin here
        game.parseUserInput("pouzi pero");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedITAdmin());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
        // ----------------------------------------------------------------------------------------------------------
        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tomu úbohému IT adminovi"
            + SystemInfo.LINE_SEPARATOR
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostal von živý,"
            + SystemInfo.LINE_SEPARATOR
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia."
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window, door keeper was attacked")
    void playerEscapedThroughWindowDoorKeeperAttacked() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("pero"));

        game.parseUserInput("vezmi pero");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("lavicka"));

        game.parseUserInput("pristup_k lavicka");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BENCH"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt lavicka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("rusko"));

        game.parseUserInput("vezmi rusko");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("pouzi rusko");
        assertTrue(game.getGamePlan().getPlayer().isPlayerWearingMedicalMask());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kniznica");
        assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(
            game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

        game.parseUserInput("pristup_k policka_s_knihami");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt policka_s_knihami");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

        game.parseUserInput("vezmi kniha_algoritmov");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi kniha_algoritmov");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        // player attacked doorkeeper here
        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi pero");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerAttackedDoorKeeper());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());
        // ----------------------------------------------------------------------------------------------------------
        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Avšak...to si si naozaj myslel, že potom čo si urobil tomu vrátnikovi si iba tak spokojne odkráčaš domov?"
            + SystemInfo.LINE_SEPARATOR
            + "Z budovy si sa síce dostal, ale ešte máš čo vysvetľovať polícii"
            + SystemInfo.LINE_SEPARATOR
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window, door keeper was bribed")
    void playerEscapedThroughWindowDoorKeeperBribed() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("lavicka"));

        game.parseUserInput("pristup_k lavicka");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BENCH"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt lavicka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("rusko"));

        game.parseUserInput("vezmi rusko");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("pouzi rusko");
        assertTrue(game.getGamePlan().getPlayer().isPlayerWearingMedicalMask());
        assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kniznica");
        assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(
            game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

        game.parseUserInput("pristup_k policka_s_knihami");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt policka_s_knihami");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

        game.parseUserInput("vezmi kniha_algoritmov");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi kniha_algoritmov");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        // player bribed doorkeeper here
        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod toalety");
        assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

        game.parseUserInput("vezmi hasiaci_pristroj");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("zamknute_dvere"));

        game.parseUserInput("pristup_k zamknute_dvere");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("LOCKED_DOOR"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi hasiaci_pristroj");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").isRoomLocked());

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

        game.parseUserInput("chod Vencovskeho_aula");
        assertEquals(
            RoomName.getRoomName("LECTURE_ROOM"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("cudzia_penazenka"));

        game.parseUserInput("preskumaj_predmet cudzia_penazenka");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("velky_obnos_penazi"));

        game.parseUserInput("vezmi velky_obnos_penazi");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));

        game.parseUserInput("chod dvor");
        assertEquals(RoomName.getRoomName("COURTYARD"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("pristup_k vratnik");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi velky_obnos_penazi");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerBribedDoorKeeper());
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));
        assertFalse(
            game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").isRoomLocked());
        // ----------------------------------------------------------------------------------------------------------
        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Vyzerá to, že si ťa niekto všimol pri tvojom pokuse uplatiť vrátnika, ba čo viac, peniaze, ktorými"
            + SystemInfo.LINE_SEPARATOR
            + "si sa pokúšal podplatiť vrátnika, zrejme patrili práve tejto osobe a ty tak ihneď po uniknutí"
            + SystemInfo.LINE_SEPARATOR
            + "z budovy školy putuješ na políciu, kde budeš mať čo vysvetľovať."
            + SystemInfo.LINE_SEPARATOR
            + "Do budúcna by sa určite šlo dostať von aj iným spôsobom!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }

    @Test
    @DisplayName("player escaped through window and get COVID-19")
    void playerEscapedThroughWindowAndGetCovid() {
        assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerPassedByCoughingTeacher());
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kniznica");
        assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(
            game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

        game.parseUserInput("pristup_k policka_s_knihami");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("preskumaj_objekt policka_s_knihami");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

        game.parseUserInput("vezmi kniha_algoritmov");
        assertTrue(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod kancelaria");
        assertEquals(RoomName.getRoomName("OFFICE"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("IT_admin"));

        game.parseUserInput("pristup_k IT_admin");
        assertEquals(
            NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN"),
            game.getGamePlan().getActualNonPlayerCharacter().getName());

        game.parseUserInput("pouzi kniha_algoritmov");
        assertFalse(
            game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod RB_202");
        assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

        game.parseUserInput("vezmi lano");
        assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        game.parseUserInput("chod chodba_na_II._poschodi");
        assertEquals(
            RoomName.getRoomName("SECOND_FLOOR_HALL"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod chodba_na_I._poschodi");
        assertEquals(
            RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Nova_budova");
        assertEquals(
            RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod spojovacia_chodba");
        assertEquals(
            RoomName.getRoomName("CONNECTING_CORRIDOR"),
            game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("chod Stara_budova");
        assertEquals(
            RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

        game.parseUserInput("rozhliadnut_sa");
        assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("okno"));

        game.parseUserInput("pristup_k okno");
        assertEquals(
            InteractableObjectName.getInteractableObjectName("WINDOW"),
            game.getGamePlan().getActualInteractableObject().getName());

        game.parseUserInput("pouzi lano");
        assertTrue(game.getGamePlan().getPlayer().hasPlayerEscapedUsingWindow());
        assertTrue(game.getGamePlan().hasPlayerReachedFinalRoom());
        // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

        String expectedGameEnding =
            "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy"
            + SystemInfo.LINE_SEPARATOR
            + "Gratulujem, úspešne sa ti podarilo dostať von!"
            + SystemInfo.LINE_SEPARATOR
            + "Má to však háčik... po 2 týždňoch sa u teba začali prejavovať respiračné problémy"
            + SystemInfo.LINE_SEPARATOR
            + "a musel si byť hospitalizovaný v Nemocnici Na Bulovce."
            + SystemInfo.LINE_SEPARATOR
            + "Nabudúce si dávaj väčší pozor okolo koho prechádzaš, a zváž či si nenasadiť nejaký ochranný prvok!"
            + SystemInfo.LINE_SEPARATOR;
        String actualGameEnding = game.getGameEnding();
        assertEquals(expectedGameEnding, actualGameEnding);
    }
}
