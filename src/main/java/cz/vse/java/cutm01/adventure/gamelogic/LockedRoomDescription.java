package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Locked game rooms with their description which is shown to player when he tries to enter them
 * before they are unlocked
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum LockedRoomDescription {
  RB_202("Niečo ti bráni vstúpiť do miestnosti, nie sú dvere zablokované?"),
  COURTYARD("Niečo ti bráni vstúpiť do miestnosti, nie sú zamknuté dvere?"),
  STREET("Vyzerá to, že ťa niekto nechce pustiť von");

  private final String lockedRoomDescription;
  private static final Map<String, String> LOCKED_ROOM_DESCRIPTIONS = new HashMap<>();

  static {
    for (LockedRoomDescription r : values()) {
      LOCKED_ROOM_DESCRIPTIONS.put(r.toString(), r.lockedRoomDescription);
    }
  }

  LockedRoomDescription(final String lockedRoomDescription) {
    this.lockedRoomDescription = lockedRoomDescription;
  }

  public static String getLockedRoomDescription(String roomName) {
    return LOCKED_ROOM_DESCRIPTIONS.get(roomName);
  }
}
