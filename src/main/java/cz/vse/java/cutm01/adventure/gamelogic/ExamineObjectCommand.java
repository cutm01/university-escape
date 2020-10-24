package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Examine item command implementation, player can use this command to examine interactable object
 * in actual game room in more detail.<br> Player can find hidden items this way
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class ExamineObjectCommand implements Command {

    private static final String NAME = CommandName.EXAMINE_OBJECT.getCommandName();
    private static final String DESCRIPTION =
        CommandDescription.EXAMINE_OBJECT.getCommandDescription();
    private final GamePlan gamePlan;

    public ExamineObjectCommand(GamePlan gamePlan) {
        this.gamePlan = gamePlan;
    }

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandDescription() {
        return DESCRIPTION;
    }

    /**
     * Method take max. 1 name of interactable object and return hidden items from this object<br>
     * Interactable object has to be in actual room and player has to stand nearby<br> Found items
     * are transferred to room's items and player can take them later
     *
     * @param commandParameters name of interactable object player wants to examine
     * @return String with description about found hidden items
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length == 0) {
            return "Nerozumiem ti!  Upresni, prosím ťa, ktorý objekt z miestnosti chceš preskúmať";
        }

        if (commandParameters.length > 1) {
            return "To tu chceš splašene behať z jedného miesta na druhé? Lepšie by bolo preskúmavať naraz iba jeden objekt";
        }

        String interactableObjectName = commandParameters[0];
        // player has firstly look around the room to ,,unlock" interaction with objects
        boolean wasRoomAlreadyExamined = gamePlan.getActualRoom().getWasRoomAlreadyExamined();
        if (!wasRoomAlreadyExamined) {
            return "Si si istý, že sa tu ten objekt skutočne nachádza? Skús sa najprv poriadne rozhliadnuť po miestnosti";
        }

        // interactable object not found in actual room
        boolean isInteractableObjectInRoom =
            gamePlan.getActualRoom().isInteractableObjectInRoom(interactableObjectName);
        if (!isInteractableObjectInRoom) {
            return "Nič také tu naozaj nevidím, skús sa ešte raz rozhliadnuť po miestnosti a osviežiť si pamäť";
        }

        // player has firstly approach object to ,,unlock" interaction with it
        boolean isPlayerStandingNearby =
            gamePlan.getActualInteractableObject() != null
            && gamePlan.getActualInteractableObject().getName().equals(interactableObjectName);
        if (!isPlayerStandingNearby) {
            return "Myslíš si snáď, že ti ten výbuch priniesol schopnosť telekinézy?\n"
                   + "Takto z diaľky to naozaj preskúmať nepôjde, pristúp, prosím ťa bližšie k objektu";
        }

        // provide information about found hidden items to player
        InteractableObject objectToExamine =
            gamePlan.getActualRoom().getInteractableObjectByName(interactableObjectName);
        boolean wasObjectAlreadyExamined = objectToExamine
            .getWasInteractableObjectAlreadyExamined();
        if (wasObjectAlreadyExamined) {
            return "Tento objekt si už preskúmal v minulosti, nič nového, čo by ti pomohlo na ňom nenájdeš!";
        }

        if (objectToExamine.getHiddenItems().getNumberOfHiddenItems() == 0) {
            gamePlan.getActualInteractableObject().setWasInteractableObjectAlreadyExamined(true);
            return "Podrobne preskúmavaš objekt "
                   + interactableObjectName
                   + ",no nenašiel si nič, čo by ti mohlo pomôcť";
        }

        String foundItemsInfo =
            "Prezeráš "
            + interactableObjectName
            + " zo všetkých strán a vyzerá to, že niečo skrýva (váha predmetu):\n"
            + objectToExamine.getHiddenItems().getHiddenItemsDescription()
            + "\nRozmýšľaš, ako tieto veci môžeš ďalej využiť, no v tom sa z diaľky ozve hlasný krik a nájdené\n"
            + "veci ti vypadnú z rúk a povaľujú sa po miestnosti. Vezmi si nejaké! Možno sa ti budú neskôr hodiť";
        objectToExamine
            .getHiddenItems()
            .moveHiddenItemsFromObjectToRoom(gamePlan, interactableObjectName);
        gamePlan.getActualInteractableObject().setWasInteractableObjectAlreadyExamined(true);

        return foundItemsInfo;
    }
}
