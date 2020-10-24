package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod toalety");
    assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

    game.parseUserInput("vezmi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod toalety");
    assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

    game.parseUserInput("vezmi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

    game.parseUserInput("pristup_k upratovacka");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedCleaningLady());
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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tej úbohej pani\n"
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostala von živá,\n"
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia.\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedITAdmin());
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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tomu úbohému IT adminovi\n"
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostal von živý,\n"
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia.\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod toalety");
    assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

    game.parseUserInput("vezmi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

    game.parseUserInput("pristup_k vratnik");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi pero");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedDoorKeeper());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
    assertFalse(
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Avšak...to si si naozaj myslel, že potom čo si urobil tomu vrátnikovi si iba tak spokojne odkráčaš domov?\n"
            + "Z budovy si sa síce dostal, ale ešte máš čo vysvetľovať polícii\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod toalety");
    assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

    game.parseUserInput("vezmi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("pristup_k vratnik");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi velky_obnos_penazi");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerBribedDoorKeeper());
    assertFalse(
        game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));
    assertFalse(
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Vyzerá to, že si ťa niekto všimol pri tvojom pokuse uplatiť vrátnika, ba čo viac, peniaze, ktorými\n"
            + "si sa pokúšal podplatiť vrátnika, zrejme patrili práve tejto osobe a ty tak ihneď po uniknutí\n"
            + "z budovy školy putuješ na políciu, kde budeš mať čo vysvetľovať.\n"
            + "Do budúcna by sa určite šlo dostať von aj iným spôsobom!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod toalety");
    assertEquals(RoomName.getRoomName("TOILETS"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("hasiaci_pristroj"));

    game.parseUserInput("vezmi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod ulica");
    assertEquals(RoomName.getRoomName("STREET"), game.getGamePlan().getActualRoom().getName());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());

    String expectedGameEnding =
        "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším.\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik... po 2 týždňoch sa u teba začali prejavovať respiračné problémy\n"
            + "a musel si byť hospitalizovaný v Nemocnici Na Bulovce.\n"
            + "Nabudúce si dávaj väčší pozor okolo koho prechádzaš, a zváž či si nenasadiť nejaký ochranný prvok!\n";
    String actualGameEnding = game.getGameEnding();
    assertEquals(expectedGameEnding, actualGameEnding);
  }

  @Test
  @DisplayName("player escaped through window, no NPC was attacked")
  void playerEscapedThroughWindowNoAttackedNPC() {
    assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getIsPlayerWearingMedicalMask());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod kniznica");
    assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

    game.parseUserInput("pristup_k policka_s_knihami");
    assertEquals(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
        game.getGamePlan().getActualInteractableObject().getName());

    game.parseUserInput("preskumaj_objekt policka_s_knihami");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

    game.parseUserInput("vezmi kniha_algoritmov");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n";
    String actualGameEnding = game.getGameEnding();
    assertEquals(expectedGameEnding, actualGameEnding);
  }

  @Test
  @DisplayName("player escaped through window, cleaning lady was attacked")
  void playerEscapedThroughWindowCleaningLadyAttacked() {
    assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getIsPlayerWearingMedicalMask());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod kniznica");
    assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

    game.parseUserInput("pristup_k policka_s_knihami");
    assertEquals(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
        game.getGamePlan().getActualInteractableObject().getName());

    game.parseUserInput("preskumaj_objekt policka_s_knihami");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

    game.parseUserInput("vezmi kniha_algoritmov");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

    game.parseUserInput("pristup_k upratovacka");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi hasiaci_pristroj");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedCleaningLady());
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kluce"));
    assertFalse(
        game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());
    // ----------------------------------------------------------------------------------------------------------

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tej úbohej pani\n"
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostala von živá,\n"
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia.\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getIsPlayerWearingMedicalMask());
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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedITAdmin());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
    // ----------------------------------------------------------------------------------------------------------
    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tomu úbohému IT adminovi\n"
            + "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostal von živý,\n"
            + "inak by si z kolabujúcej budovy putoval rovno do väzenia.\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!\n";
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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getIsPlayerWearingMedicalMask());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod kniznica");
    assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

    game.parseUserInput("pristup_k policka_s_knihami");
    assertEquals(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
        game.getGamePlan().getActualInteractableObject().getName());

    game.parseUserInput("preskumaj_objekt policka_s_knihami");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

    game.parseUserInput("vezmi kniha_algoritmov");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isNonPlayerCharacterInRoom("vratnik"));

    game.parseUserInput("pristup_k vratnik");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi pero");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerAttackedDoorKeeper());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("pero"));
    assertFalse(
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());
    // ----------------------------------------------------------------------------------------------------------
    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Avšak...to si si naozaj myslel, že potom čo si urobil tomu vrátnikovi si iba tak spokojne odkráčaš domov?\n"
            + "Z budovy si sa síce dostal, ale ešte máš čo vysvetľovať polícii\n"
            + "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia\n";
    String actualGameEnding = game.getGameEnding();
    assertEquals(expectedGameEnding, actualGameEnding);
  }

  @Test
  @DisplayName("player escaped through window, door keeper was bribed")
  void playerEscapedThroughWindowDoorKeeperBribed() {
    assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getIsPlayerWearingMedicalMask());
    assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("rusko"));

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod kniznica");
    assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

    game.parseUserInput("pristup_k policka_s_knihami");
    assertEquals(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
        game.getGamePlan().getActualInteractableObject().getName());

    game.parseUserInput("preskumaj_objekt policka_s_knihami");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

    game.parseUserInput("vezmi kniha_algoritmov");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("hasiaci_pristroj"));

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
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("dvor").getIsRoomLocked());

    game.parseUserInput("chod dvor");
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

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
    assertEquals(RoomName.getRoomName("COURTYARD"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("pristup_k vratnik");
    assertEquals(
        NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER"),
        game.getGamePlan().getActualNonPlayerCharacter().getName());

    game.parseUserInput("pouzi velky_obnos_penazi");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerBribedDoorKeeper());
    assertFalse(
        game.getGamePlan().getPlayer().getInventory().isItemInInventory("velky_obnos_penazi"));
    assertFalse(
        game.getGamePlan().getActualRoom().getNeighboringRoomByName("ulica").getIsRoomLocked());
    // ----------------------------------------------------------------------------------------------------------
    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Vyzerá to, že si ťa niekto všimol pri tvojom pokuse uplatiť vrátnika, ba čo viac, peniaze, ktorými\n"
            + "si sa pokúšal podplatiť vrátnika, zrejme patrili práve tejto osobe a ty tak ihneď po uniknutí\n"
            + "z budovy školy putuješ na políciu, kde budeš mať čo vysvetľovať.\n"
            + "Do budúcna by sa určite šlo dostať von aj iným spôsobom!\n";
    String actualGameEnding = game.getGameEnding();
    assertEquals(expectedGameEnding, actualGameEnding);
  }

  @Test
  @DisplayName("player escaped through window and get COVID-19")
  void playerEscapedThroughWindowAndGetCovid() {
    assertEquals(RoomName.getRoomName("RB_201"), game.getGamePlan().getActualRoom().getName());
    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Stara_budova");
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerPassedByCoughingTeacher());
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod kniznica");
    assertEquals(RoomName.getRoomName("LIBRARY"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isInteractableObjectInRoom("policka_s_knihami"));

    game.parseUserInput("pristup_k policka_s_knihami");
    assertEquals(
        InteractableObjectName.getInteractableObjectName("BOOK_SHELF"),
        game.getGamePlan().getActualInteractableObject().getName());

    game.parseUserInput("preskumaj_objekt policka_s_knihami");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("kniha_algoritmov"));

    game.parseUserInput("vezmi kniha_algoritmov");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("kniha_algoritmov"));

    game.parseUserInput("chod Stara_budova");
    assertEquals(
        RoomName.getRoomName("OLD_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod RB_202");
    assertEquals(RoomName.getRoomName("RB_202"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("rozhliadnut_sa");
    assertTrue(game.getGamePlan().getActualRoom().isItemInRoom("lano"));

    game.parseUserInput("vezmi lano");
    assertTrue(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    game.parseUserInput("chod chodba_na_II._poschodi");
    assertEquals(
        RoomName.getRoomName("SECOND_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod chodba_na_I._poschodi");
    assertEquals(
        RoomName.getRoomName("FIRST_FLOOR_HALL"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod Nova_budova");
    assertEquals(
        RoomName.getRoomName("NEW_BUILDING"), game.getGamePlan().getActualRoom().getName());

    game.parseUserInput("chod spojovacia_chodba");
    assertEquals(
        RoomName.getRoomName("CONNECTING_CORRIDOR"), game.getGamePlan().getActualRoom().getName());

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
    assertTrue(game.getGamePlan().getPlayer().getHasPlayerEscapedUsingWindow());
    assertTrue(game.getGamePlan().getHasPlayerReachedFinalRoom());
    // assertFalse(game.getGamePlan().getPlayer().getInventory().isItemInInventory("lano"));

    String expectedGameEnding =
        "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy\n"
            + "Gratulujem, úspešne sa ti podarilo dostať von!\n"
            + "Má to však háčik... po 2 týždňoch sa u teba začali prejavovať respiračné problémy\n"
            + "a musel si byť hospitalizovaný v Nemocnici Na Bulovce.\n"
            + "Nabudúce si dávaj väčší pozor okolo koho prechádzaš, a zváž či si nenasadiť nejaký ochranný prvok!\n";
    String actualGameEnding = game.getGameEnding();
    assertEquals(expectedGameEnding, actualGameEnding);
  }
}
