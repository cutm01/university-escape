package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Use command implementation, player can use this command to perform special action with given item
 * from his inventory
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class UseCommand implements Command {

    private static final String NAME = CommandName.USE.getCommandName();
    private static final String DESCRIPTION = CommandDescription.USE.getCommandDescription();
    private final GamePlan gamePlan;

    public UseCommand(GamePlan gamePlan) {
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
     * Method is used to perform special action with game items<br> These items can be used while
     * standing next to interactable object or NPC in actual game room
     *
     * @param commandParameters max. 1 name of item which players want to use
     * @return performed special action text or "error" message if item was not found, or player is
     * not standing nearby NPC or interactable object
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters.length == 0) {
            return "Bez uvedenia toho, ktorý predmet zo svojho batohu chceš použiť, to naozaj nepôjde";
        }

        if (commandParameters.length > 1) {
            return "Nie tak zhurta! Dokážem pracovať iba s jedným predmetom";
        }

        String itemToUseName = commandParameters[0];
        boolean isItemInPlayerInventory =
            gamePlan.getPlayer().getInventory().isItemInInventory(itemToUseName);
        if (!isItemInPlayerInventory) {
            return "Tento predmet v tvojom batohu nevidím! Nezabudol si ho náhodou v nejakej inej miestnosti?";
        }

        // medical mask and protective medical suit can be used without standing next to object or NPC
        if (itemToUseName.equals(ItemName.getItemName("MEDICAL_MASK"))) {
            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemToUseName);
            gamePlan.getPlayer().setIsPlayerWearingMedicalMask(true);

            boolean isPlayerWearingMedicalSuit = gamePlan.getPlayer()
                .getIsPlayerWearingMedicalSuit();
            if (isPlayerWearingMedicalSuit) {
                return "Opatrnosti nikdy nie je dosť a preto si sa rozhodol nasadiť si k celotelovému obleku aj rúško";
            }
            return "Nasádzaš si rúško. Povinnosť nosiť rúško už síce pominula, ale koronavírus rozhodne nie!";
        }

        if (itemToUseName.equals(ItemName.getItemName("PROTECTIVE_MEDICAL_SUIT"))) {
            gamePlan.getPlayer().getInventory().removeItemFromInventory(itemToUseName);
            gamePlan.getPlayer().setIsPlayerWearingMedicalSuit(true);

            return "Obliekaš si celotelový ochranný oblek. Možno na konci skončíš zavalený sutinami budovy, ale negatívny na Covid-19!";
        }

        boolean isPlayerStandingNextToNPC = gamePlan.getActualNonPlayerCharacter() != null;
        if (isPlayerStandingNextToNPC) {
            setAlternativeGameEnding(itemToUseName);
            return gamePlan.getActualNonPlayerCharacter()
                .performSpecialAction(itemToUseName, gamePlan);
        }

        boolean isPlayerStandingNextToInteractableObject =
            gamePlan.getActualInteractableObject() != null;
        if (isPlayerStandingNextToInteractableObject) {
            setAlternativeGameEnding(itemToUseName);
            return gamePlan.getActualInteractableObject()
                .performSpecialAction(itemToUseName, gamePlan);
        }

        return "Nenapadá mi ako tento predmet využiť, čo tak najprv pristúpiť k nejakej osobe alebo objektu a skúsiť to znovu?";
    }

    /**
     * Method is used to set variables in Player class, which can alter game ending according to
     * actions which player performed (e.q. attack NPC with fire extinguisher)
     *
     * @param itemName name of item which player used to perform special action
     */
    private void setAlternativeGameEnding(String itemName) {
        String playerStandsBy = null;
        if (gamePlan.getActualNonPlayerCharacter() != null) {
            playerStandsBy = gamePlan.getActualNonPlayerCharacter().getName();
        }
        if (gamePlan.getActualInteractableObject() != null) {
            playerStandsBy = gamePlan.getActualInteractableObject().getName();
        }

        // player attacked cleaning lady, IT admin or door keeper with one rope, pen or fire
        // extinguisher
        if (itemName.equals(ItemName.getItemName("ROPE"))
            || itemName.equals(ItemName.getItemName("PEN"))
            || itemName.equals(ItemName.getItemName("FIRE_EXTINGUISHER"))) {
            if (NonPlayerCharacterName.getNonPlayerCharacterName("CLEANING_LADY")
                .equals(playerStandsBy)) {
                gamePlan.getPlayer().setHasPlayerAttackedCleaningLady(true);
            }
            if (NonPlayerCharacterName.getNonPlayerCharacterName("IT_ADMIN")
                .equals(playerStandsBy)) {
                gamePlan.getPlayer().setHasPlayerAttackedITAdmin(true);
            }
            if (NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER")
                .equals(playerStandsBy)) {
                gamePlan.getPlayer().setHasPlayerAttackedDoorKeeper(true);
            }
        }

        // player bribed the door keeper
        if (itemName.equals(ItemName.getItemName("STOLEN_MONEY"))
            && NonPlayerCharacterName.getNonPlayerCharacterName("DOOR_KEEPER")
                .equals(playerStandsBy)) {
            gamePlan.getPlayer().setHasPlayerBribedDoorKeeper(true);
        }

        // player escaped through windows using rope and game ends
        if (itemName.equals(ItemName.getItemName("ROPE"))
            && InteractableObjectName.getInteractableObjectName("WINDOW").equals(playerStandsBy)) {
            gamePlan.getPlayer().setHasPlayerEscapedUsingWindow(true);
            gamePlan.setHasPlayerReachedFinalRoom(true);
        }
    }
}
