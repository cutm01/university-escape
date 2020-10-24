package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Game items (which player can discover during game) with their name, i.e. Strings which player has
 * to type in order to take, drop or perform special actions with them
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum ItemName {
  BOTTLE("flasa"),
  PEN("pero"),
  WALLET("penazenka"),
  ID_CARD("obciansky_preukaz"),
  MONEY("peniaze"),
  MEDICAL_MASK("rusko"),
  ROPE("lano"),
  ISIC("ISIC"),
  SMALL_SNACK("mala_bageta"),
  BIG_SNACK("velka_bageta"),
  FIRE_EXTINGUISHER("hasiaci_pristroj"),
  PROTECTIVE_MEDICAL_SUIT("celotelovy_lekarsky_oblek"),
  JACKET("bunda"),
  MUSIC_CD("CD_Helenky_Vondráčkovej"),
  BOOK("kniha_algoritmov"),
  STOLEN_WALLET("cudzia_penazenka"),
  STOLEN_MONEY("velky_obnos_penazi"),
  STOLEN_ISIC("cudzi_ISIC"),
  KEYS("kluce"),
  PAPER_CLIP("spinka");

  private final String itemName;
  private static final Map<String, String> ITEM_NAMES = new HashMap<>();

  static {
    for (ItemName i : values()) {
      ITEM_NAMES.put(i.toString(), i.itemName);
    }
  }

  ItemName(final String itemName) {
    this.itemName = itemName;
  }

  public static String getItemName(String itemName) {
    return ITEM_NAMES.get(itemName);
  }
}
