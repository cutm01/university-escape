package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Trida Room - popisuje jednotlivé prostory (místnosti) hry
 *
 * <p>Tato třída je součástí jednoduché textové hry.
 *
 * <p>"Room" reprezentuje jedno místo (místnost, prostor, ..) ve scénáři hry. Room může mít
 * sousední prostory připojené přes východy. Pro každý východ si prostor ukládá odkaz na sousedící
 * prostor.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
public class Room {

    private final String name;
    private final String description;
    private final Set<Room> neighboringRooms;
    // contains also interactableObject's hidden items which are added to room after player examines it
    private final Map<String, Item> items;
    private final Map<String, InteractableObject> interactableObjects;
    private final Map<String, NonPlayerCharacter> nonPlayerCharacters;
    private boolean wasRoomAlreadyExamined;
    private boolean isRoomLocked;
    // is shown to player when he tries to enter locked room
    private final String lockedRoomDescription;

    // region Constructors
    // --------------------------------------------------------------------------------
    public Room(String nameDefinedInEnum, boolean isRoomLocked) {
        this.name = RoomName.getRoomName(nameDefinedInEnum);
        this.description = RoomDescription.getRoomDescription(nameDefinedInEnum);
        neighboringRooms = new HashSet<>();
        items = new LinkedHashMap<>();
        interactableObjects = new LinkedHashMap<>();
        nonPlayerCharacters = new LinkedHashMap<>();
        wasRoomAlreadyExamined = false;
        this.isRoomLocked = isRoomLocked;
        this.lockedRoomDescription =
            LockedRoomDescription.getLockedRoomDescription(
                nameDefinedInEnum); // null if room is not locked
    }
    // --------------------------------------------------------------------------------
    // endregion Constructors

    // region Getters and Setters
    // --------------------------------------------------------------------------------

    /**
     * Vrací název prostoru (byl zadán při vytváření prostoru jako parametr konstruktoru)
     *
     * @return název prostoru
     */
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Vrací "dlouhý" popis prostoru, který může vypadat následovně: Jsi v mistnosti/prostoru
     * vstupni hala budovy VSE na Jiznim meste. vychody: chodba bufet ucebna
     *
     * @return Dlouhý popis prostoru
     */
    public String getRoomDescriptionWithExits() {
        StringBuilder textToReturn = new StringBuilder();
        textToReturn
            .append("Vstupuješ do miestnosti/priestoru ")
            .append(name)
            .append(", ")
            .append(description)
            .append(SystemInfo.LINE_SEPARATOR);
        if (getNeighboringRoomNames().length() > 0) {
            textToReturn.append("Východy: ").append(getNeighboringRoomNames());
        }

        return textToReturn.toString();
    }

    /**
     * Vrací kolekci obsahující prostory, se kterými tento prostor sousedí. Takto získaný seznam
     * sousedních prostor nelze upravovat (přidávat, odebírat východy) protože z hlediska správného
     * návrhu je to plně záležitostí třídy Room.
     *
     * @return Nemodifikovatelná kolekce prostorů (východů), se kterými tento prostor sousedí.
     */
    public Collection<Room> getNeighboringRooms() {
        return Collections.unmodifiableCollection(neighboringRooms);
    }

    public boolean wasRoomAlreadyExamined() {
        return wasRoomAlreadyExamined;
    }

    public void setWasRoomAlreadyExamined(boolean wasRoomAlreadyExamined) {
        this.wasRoomAlreadyExamined = wasRoomAlreadyExamined;
    }

    public boolean isRoomLocked() {
        return isRoomLocked;
    }

    public void setIsRoomLocked(boolean isRoomLocked) {
        this.isRoomLocked = isRoomLocked;
    }

    public String getLockedRoomDescription() {
        return lockedRoomDescription;
    }
    // --------------------------------------------------------------------------------
    // endregion Getters and Setters

    // region Methods equals() and hashCode()
    // --------------------------------------------------------------------------------

    /**
     * Metoda equals pro porovnání dvou prostorů. Překrývá se metoda equals ze třídy Object. Dva
     * prostory jsou shodné, pokud mají stejný název. Tato metoda je důležitá z hlediska správného
     * fungování seznamu východů (Set).
     *
     * <p>Bližší popis metody equals je u třídy Object.
     *
     * @param o object, který se má porovnávat s aktuálním
     * @return hodnotu true, pokud má zadaný prostor stejný název, jinak false
     */
    @Override
    public boolean equals(Object o) {
        // porovnáváme zda se nejedná o dva odkazy na stejnou instanci
        if (this == o) {
            return true;
        }
        // porovnáváme jakého typu je parametr
        if (!(o instanceof Room)) {
            return false; // pokud parametr není typu Room, vrátíme false
        }
        // přetypujeme parametr na typ Room
        Room roomToCompare = (Room) o;

        // metoda equals třídy java.util.Objects porovná hodnoty obou názvů.
        // Vrátí true pro stejné názvy a i v případě, že jsou oba názvy null,
        // jinak vrátí false.

        return (java.util.Objects.equals(this.name, roomToCompare.name));
    }

    /**
     * metoda hashCode vraci ciselny identifikator instance, ktery se pouziva pro optimalizaci
     * ukladani v dynamickych datovych strukturach. Pri prekryti metody equals je potreba prekryt i
     * metodu hashCode. Podrobny popis pravidel pro vytvareni metody hashCode je u metody hashCode
     * ve tride Object
     */
    @Override
    public int hashCode() {
        int result = 3;
        int roomNameHash = java.util.Objects.hashCode(this.name);
        result = 37 * result + roomNameHash;
        return result;
    }
    // --------------------------------------------------------------------------------
    // endregion Methods equals() and hashCode()

    // region Methods related to neighboringRooms
    // --------------------------------------------------------------------------------

    /**
     * Definuje východ z prostoru (sousední/vedlejsi prostor). Vzhledem k tomu, že je použit Set pro
     * uložení východů, může být sousední prostor uveden pouze jednou (tj. nelze mít dvoje dveře do
     * stejné sousední místnosti). Druhé zadání stejného prostoru tiše přepíše předchozí zadání
     * (neobjeví se žádné chybové hlášení). Lze zadat též cestu ze do sebe sama.
     *
     * @param roomName prostor, který sousedi s aktualnim prostorem.
     */
    public void addExitFromRoom(Room roomName) {
        neighboringRooms.add(roomName);
    }

    /**
     * Vrací textový řetězec, který popisuje sousední východy
     *
     * @return Popis východů - názvů sousedních prostorů
     */
    public String getNeighboringRoomNames() {
        StringBuilder roomNames = new StringBuilder();
        Iterator<Room> neighboringRoomsIterator = neighboringRooms.iterator();
        Room room;
        while (neighboringRoomsIterator.hasNext()) {
            room = neighboringRoomsIterator.next();
            roomNames.append(room.getName());

            if (neighboringRoomsIterator.hasNext()) {
                roomNames.append("    ");
            }
        }

        return roomNames.toString();
    }

    /**
     * Vrací prostor, který sousedí s aktuálním prostorem a jehož název je zadán jako parametr.
     * Pokud prostor s udaným jménem nesousedí s aktuálním prostorem, vrací se hodnota null.
     *
     * @param neighboringRoomName Jméno sousedního prostoru (východu)
     * @return Room, který se nachází za příslušným východem, nebo hodnota null, pokud prostor
     * zadaného jména není sousedem.
     */
    public Room getNeighboringRoomByName(String neighboringRoomName) {
        List<Room> rooms =
            neighboringRooms.stream()
                .filter(room -> room.getName().equals(neighboringRoomName))
                .collect(Collectors.toList());
        if (rooms.isEmpty()) {
            return null;
        } else {
            return rooms.get(0);
        }
    }
    // --------------------------------------------------------------------------------
    // endregion Methods related to neighboringRooms

    // region Methods related to items in room
    // --------------------------------------------------------------------------------
    public List<Item> getItemsInRoom() {
        return new ArrayList<>(items.values());
    }

    public boolean isItemInRoom(String itemName) {
        return items.containsKey(itemName);
    }

    public int getNumberOfItemsInRoom() {
        return items.size();
    }

    public Item getItemByName(String itemName) {
        if (!isItemInRoom(itemName)) {
            return null;
        }

        return items.get(itemName);
    }

    public void addItemToRoom(Item item) {
        items.put(item.getName(), item);
    }

    public void removeItemFromRoom(Item item) {
        items.remove(item.getName());
    }

    /**
     * Method returns names of all items which are currently placed in room
     * @return Set of game item names currently placed in room, names are returned in format from ItemName Enum
     * and therefore they can be used as game command argument
     */
    public Set<String> getRoomItemsNames() {
        return items.keySet();
    }

    /**
     * Method return names of all items inside the room which player can take<br> Names of items are
     * returned as one String, where each one is separated by comma
     *
     * @return names of items which player can take
     */
    public String getItemNamesAsString() {
        StringBuilder itemNames = new StringBuilder();
        Iterator<String> itemIterator = items.keySet().iterator();

        while (itemIterator.hasNext()) {
            itemNames.append(itemIterator.next());

            if (itemIterator.hasNext()) {
                itemNames.append(", ");
            }
        }

        return itemNames.toString();
    }
    // --------------------------------------------------------------------------------
    // endregion Methods related to items in room

    // region Methods related to interactable objects in room
    // --------------------------------------------------------------------------------
    public boolean isInteractableObjectInRoom(String objectName) {
        return interactableObjects.containsKey(objectName);
    }

    public int getNumberOfInteractableObjectsInRoom() {
        return interactableObjects.size();
    }

    public InteractableObject getInteractableObjectByName(String objectName) {
        if (!isInteractableObjectInRoom(objectName)) {
            return null;
        }

        return interactableObjects.get(objectName);
    }

    public void addInteractableObjectToRoom(InteractableObject object) {
        interactableObjects.put(object.getName(), object);
    }

    /**
     * Method returns names of all interactable objects which are currently placed in room
     * @return Set of interactable object names currently placed in room, names are returned in format from InteractableObjectName Enum
     * and therefore they can be used as game command arguments
     */
    public Set<String> getRoomInteractableObjectsNames() {
        return interactableObjects.keySet();
    }

    /**
     * Method return names of all interactable object inside the room which player can use<br> Names
     * of objects are returned as one String, where each one is separated by comma
     *
     * @return names of interactable objects which player can interact with
     */
    public String getInteractableObjectNamesAsString() {
        StringBuilder objectNames = new StringBuilder();
        Iterator<String> objectIterator = interactableObjects.keySet().iterator();

        while (objectIterator.hasNext()) {
            objectNames.append(objectIterator.next());

            if (objectIterator.hasNext()) {
                objectNames.append(", ");
            }
        }

        return objectNames.toString();
    }
    // --------------------------------------------------------------------------------
    // endregion Methods related to interactable objects in room

    // region Methods related non-player characters in room
    // --------------------------------------------------------------------------------
    public boolean isNonPlayerCharacterInRoom(String characterName) {
        return nonPlayerCharacters.containsKey(characterName);
    }

    public int getNumberOfNonPlayerCharactersInRoom() {
        return nonPlayerCharacters.size();
    }

    public NonPlayerCharacter getNonPlayerCharacterByName(String characterName) {
        if (!isNonPlayerCharacterInRoom(characterName)) {
            return null;
        }

        return nonPlayerCharacters.get(characterName);
    }

    public void addNonPlayerCharacterToRoom(NonPlayerCharacter character) {
        nonPlayerCharacters.put(character.getName(), character);
    }

    /**
     * Method returns names of all non-player characters which are currently placed in room
     * @return Set of non-player characterst names currently placed in room, names are returned in format from NonPlayerCharacterName Enum
     * and therefore they can be used as game command arguments
     */
    public Set<String> getRoomNonPlayerCharactersNames() {
        return nonPlayerCharacters.keySet();
    }

    /**
     * Method return names of all non-player characters inside the room the player can talk to<br>
     * Names of characters are returned as one String, where each one is separated by comma
     *
     * @return names of non-player characters the player can talk to
     */
    public String getNonPlayerCharacterNamesAsString() {
        StringBuilder nonPlayerCharacterNames = new StringBuilder();
        Iterator<String> characterIterator = nonPlayerCharacters.keySet().iterator();

        while (characterIterator.hasNext()) {
            nonPlayerCharacterNames.append(characterIterator.next());

            if (characterIterator.hasNext()) {
                nonPlayerCharacterNames.append(", ");
            }
        }

        return nonPlayerCharacterNames.toString();
    }
    // --------------------------------------------------------------------------------
    // endregion Methods related to non-player characters in room
}
