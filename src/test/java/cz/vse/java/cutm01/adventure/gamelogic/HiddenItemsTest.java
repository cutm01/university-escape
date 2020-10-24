package cz.vse.java.cutm01.adventure.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HiddenItemsTest {
  private GamePlan gamePlan;
  private Room room;
  private HiddenItems hiddenItemsInItem;
  private HiddenItems hiddenItemsInInteractableObject;
  private Item item;
  private InteractableObject interactableObject;

  @BeforeEach
  void setUp() {
    GameImpl game = new GameImpl();
    gamePlan = game.getGamePlan();

    // create room which will be used in tests (i.e. it is actual room where player is currently in)
    room = new Room("RB_201", false);
    room.setWasRoomAlreadyExamined(true);
    gamePlan.setActualRoom(room);
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

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("getNumberOfHiddenItems method")
  void getNumberOfHiddenItems(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    hiddenItemsInInteractableObject = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    interactableObject = new InteractableObject("DESK", hiddenItemsInInteractableObject);

    assertEquals(itemsToHide.length, item.getHiddenItems().getNumberOfHiddenItems());
    assertEquals(itemsToHide.length, interactableObject.getHiddenItems().getNumberOfHiddenItems());
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("removeHiddenItem method")
  void removeHiddenItem(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    hiddenItemsInInteractableObject = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    interactableObject = new InteractableObject("DESK", hiddenItemsInInteractableObject);

    // asserts for removing hidden items from Item instance
    item.getHiddenItems().removeHiddenItem(item1);
    assertEquals(2, item.getHiddenItems().getNumberOfHiddenItems());

    item.getHiddenItems().removeHiddenItem(item2);
    assertEquals(1, item.getHiddenItems().getNumberOfHiddenItems());

    item.getHiddenItems().removeHiddenItem(item3);
    assertEquals(0, item.getHiddenItems().getNumberOfHiddenItems());
    assertTrue(item.getHiddenItems().getAllHiddenItems().isEmpty());

    // asserts for removing hidden items from InteractableObject instance
    interactableObject.getHiddenItems().removeHiddenItem(item1);
    assertEquals(2, interactableObject.getHiddenItems().getNumberOfHiddenItems());

    interactableObject.getHiddenItems().removeHiddenItem(item2);
    assertEquals(1, interactableObject.getHiddenItems().getNumberOfHiddenItems());

    interactableObject.getHiddenItems().removeHiddenItem(item3);
    assertEquals(0, interactableObject.getHiddenItems().getNumberOfHiddenItems());
    assertTrue(interactableObject.getHiddenItems().getAllHiddenItems().isEmpty());
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("getAllHiddenItems method")
  void getAllHiddenItems(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    hiddenItemsInInteractableObject = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    interactableObject = new InteractableObject("DESK", hiddenItemsInInteractableObject);

    // asserts for HiddenItems in Item instance
    assertTrue(item.getHiddenItems().getAllHiddenItems().contains(item1));
    assertTrue(item.getHiddenItems().getAllHiddenItems().contains(item2));
    assertTrue(item.getHiddenItems().getAllHiddenItems().contains(item3));

    // asserts for HiddenItems in InteractableObject instance
    assertTrue(interactableObject.getHiddenItems().getAllHiddenItems().contains(item1));
    assertTrue(interactableObject.getHiddenItems().getAllHiddenItems().contains(item2));
    assertTrue(interactableObject.getHiddenItems().getAllHiddenItems().contains(item3));
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("gedHiddenItemsDescription method")
  void getHiddenItemsDescription(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    hiddenItemsInInteractableObject = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    interactableObject = new InteractableObject("DESK", hiddenItemsInInteractableObject);

    // asserts for HiddenItems in Item instance
    String expectedOutputForItem =
        item1.getName()
            + "("
            + item1.getWeight()
            + "), "
            + item2.getName()
            + "("
            + item2.getWeight()
            + "), "
            + item3.getName()
            + "("
            + item3.getWeight()
            + ")";
    String actualOutputForItem = item.getHiddenItems().getHiddenItemsDescription();

    assertEquals(expectedOutputForItem, actualOutputForItem);

    // asserts for HiddenItems in InteractableObject instance
    String expectedOutputForObject =
        item1.getName()
            + "("
            + item1.getWeight()
            + "), "
            + item2.getName()
            + "("
            + item2.getWeight()
            + "), "
            + item3.getName()
            + "("
            + item3.getWeight()
            + ")";
    String actualOutputForObject = interactableObject.getHiddenItems().getHiddenItemsDescription();

    assertEquals(expectedOutputForObject, actualOutputForObject);
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("moveHiddenItemsFromItemToRoom method")
  void moveHiddenItemsFromItemToRoom(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    room.addItemToRoom(item);

    int expectedNumberOfItemsInRoom =
        gamePlan.getActualRoom().getNumberOfItemsInRoom()
            + item.getHiddenItems().getNumberOfHiddenItems();
    item.getHiddenItems().moveHiddenItemsFromItemToRoom(gamePlan, item.getName());
    assertEquals(0, item.getHiddenItems().getNumberOfHiddenItems());
    assertEquals(expectedNumberOfItemsInRoom, gamePlan.getActualRoom().getNumberOfItemsInRoom());
    assertEquals(item1, gamePlan.getActualRoom().getItemByName(item1.getName()));
    assertEquals(item2, gamePlan.getActualRoom().getItemByName(item2.getName()));
    assertEquals(item3, gamePlan.getActualRoom().getItemByName(item3.getName()));
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("moveHiddenItemsFromInventory method")
  void moveHiddenItemsFromInventoryToRoom(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInItem = new HiddenItems(itemsToHide);
    item = new Item("JACKET", hiddenItemsInItem);
    gamePlan.getPlayer().getInventory().addItemToInventory(item);

    int expectedNumberOfItemsInRoom =
        gamePlan.getActualRoom().getNumberOfItemsInRoom()
            + item.getHiddenItems().getNumberOfHiddenItems();
    item.getHiddenItems().moveHiddenItemsFromInventoryToRoom(gamePlan, item.getName());
    assertEquals(0, item.getHiddenItems().getNumberOfHiddenItems());
    assertEquals(expectedNumberOfItemsInRoom, gamePlan.getActualRoom().getNumberOfItemsInRoom());
    assertEquals(item1, gamePlan.getActualRoom().getItemByName(item1.getName()));
    assertEquals(item2, gamePlan.getActualRoom().getItemByName(item2.getName()));
    assertEquals(item3, gamePlan.getActualRoom().getItemByName(item3.getName()));
  }

  @ParameterizedTest
  @MethodSource("provideItemInstancesToCreateHiddenItemsInstances")
  @DisplayName("moveHiddenItemsFromObjectToRoom method")
  void moveHiddenItemsFromObjectToRoom(Item item1, Item item2, Item item3) {
    Item[] itemsToHide = {item1, item2, item3};
    hiddenItemsInInteractableObject = new HiddenItems(itemsToHide);
    interactableObject = new InteractableObject("DESK", hiddenItemsInInteractableObject);
    room.addInteractableObjectToRoom(interactableObject);

    int expectedNumberOfItemsInRoom =
        gamePlan.getActualRoom().getNumberOfItemsInRoom()
            + interactableObject.getHiddenItems().getNumberOfHiddenItems();
    interactableObject
        .getHiddenItems()
        .moveHiddenItemsFromObjectToRoom(gamePlan, interactableObject.getName());
    assertEquals(0, interactableObject.getHiddenItems().getNumberOfHiddenItems());
    assertEquals(expectedNumberOfItemsInRoom, gamePlan.getActualRoom().getNumberOfItemsInRoom());
    assertEquals(item1, gamePlan.getActualRoom().getItemByName(item1.getName()));
    assertEquals(item2, gamePlan.getActualRoom().getItemByName(item2.getName()));
    assertEquals(item3, gamePlan.getActualRoom().getItemByName(item3.getName()));
  }
}
