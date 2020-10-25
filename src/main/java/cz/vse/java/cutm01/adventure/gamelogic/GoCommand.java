package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Třída GoCommand implementuje pro hru příkaz jdi. Tato třída je součástí jednoduché textové hry.
 *
 * @author Jarmila Pavlickova, Luboš Pavlíček
 * @version pro školní rok 2016/2017
 */
class GoCommand implements Command {

    private static final String NAME = CommandName.GO.getCommandName();
    private static final String DESCRIPTION = CommandDescription.GO.getCommandDescription();
    private final GamePlan gamePlan;

    /**
     * Konstruktor třídy
     *
     * @param gamePlan herní plán, ve kterém se bude ve hře "chodit"
     */
    public GoCommand(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
    }

    /**
     * Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání) @ return nazev
     * prikazu
     */
    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandDescription() {
        return DESCRIPTION;
    }

    /**
     * Provádí příkaz "jdi". Zkouší se vyjít do zadaného prostoru. Pokud prostor existuje, vstoupí
     * se do nového prostoru. Pokud zadaný sousední prostor (východ) není, vypíše se chybové
     * hlášení.
     *
     * @param commandParameters - jako parametr obsahuje jméno prostoru (východu), do kterého se má
     *                          jít.
     * @return zpráva, kterou vypíše hra hráči
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length == 0) {
            // pokud chybí druhé slovo (sousední prostor), tak ....
            return "Do akej miestnosti chceš ísť? Upresni to, prosím ťa";
        }

        if (commandParameters.length > 1) {
            return "Spomaľ trochu a vyber si iba jednu miestnosť, do ktorej chceš ísť";
        }

        String roomName = commandParameters[0];

        // zkoušíme přejít do sousedního prostoru
        Room roomToEnter = gamePlan.getActualRoom().getNeighboringRoomByName(roomName);

        if (roomToEnter == null) {
            return "Vstup do tejto miestnosti tu nevidím, skús to znovu";
        }

        if (roomToEnter.getIsRoomLocked()) {
            return roomToEnter.getLockedRoomDescription();
        }

        gamePlan.setActualRoom(roomToEnter);
        gamePlan.setActualInteractableObject(null);
        gamePlan.setActualNonPlayerCharacter(null);

        // player passes by coughing teacher in CONNECTING_CORRIDOR room
        // without wearing medical mask or suit
        if (gamePlan.getActualRoom().getName().equals(RoomName.getRoomName("OLD_BUILDING"))
            && !gamePlan.getPlayer().getIsPlayerWearingMedicalSuit()
            && !gamePlan.getPlayer().getIsPlayerWearingMedicalMask()) {
            gamePlan.getPlayer().setHasPlayerPassedByCoughingTeacher(true);
        }

        // game ends after reaching room Street
        if (roomToEnter.getName().equals(RoomName.getRoomName("STREET"))) {
            gamePlan.setHasPlayerReachedFinalRoom(true);
            gamePlan.getPlayer().setHasPlayerEscapedThroughCourtyard(true);
        }

        return roomToEnter.getRoomDescriptionWithExits();
    }
}
