package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

/**
 * Approach command implementation, player can use this command to approach interactable object or
 * non-player characters in room where he is currently standing
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class ApproachCommand implements Command {

    private static final String NAME = CommandName.APPROACH.getCommandName();
    private static final String DESCRIPTION = CommandDescription.APPROACH.getCommandDescription();
    private final GamePlan gamePlan;

    public ApproachCommand(GamePlan gamePlan) {
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
     * Method used to approach interactable objects or non-player characters in actual room
     *
     * @param commandParameters max 1 name of interactable object or non-player character player
     *                          wants to approach
     * @return String which informs player about performed action (i.e if he successfully approached
     * object or NPC)
     */
    @Override
    public String executeCommand(String... commandParameters) {
        if (commandParameters == null) {
            return "Niečo sa pokazilo, zadaj, prosím ťa, ten príkaz ešte raz";
        }

        if (!gamePlan.getActualRoom().wasRoomAlreadyExamined()) {
            return "Asi by bolo vhodné sa najprv poriadne rozhliadnuť po miestnosti";
        }

        if (commandParameters.length == 0) {
            return "Nerozumiem ti! K čomu to mám pristúpiť? Skús byť trochu viac konkrétnejší...";
        }

        if (commandParameters.length > 1) {
            return
                "No tak, spomaľ trochu, a vyber si iba jednu osobu alebo objekt, ku ktorému chceš pristúpiť!"
                + SystemInfo.LINE_SEPARATOR
                + "Nie si predsa elektrón vo svete kvantovej mechaniky a nemôžeš byť naraz na dvoch rôznych miestach";
        }

        String approachTo = commandParameters[0];
        if (gamePlan.getActualRoom().isInteractableObjectInRoom(approachTo)) {
            InteractableObject object = gamePlan.getActualRoom()
                .getInteractableObjectByName(approachTo);
            gamePlan.setActualInteractableObject(object);
            gamePlan.setActualNonPlayerCharacter(null);

            return "Pristupuješ k " + approachTo;
        }

        if (gamePlan.getActualRoom().isNonPlayerCharacterInRoom(approachTo)) {
            NonPlayerCharacter nonPlayerCharacter =
                gamePlan.getActualRoom().getNonPlayerCharacterByName(approachTo);
            gamePlan.setActualNonPlayerCharacter(nonPlayerCharacter);
            gamePlan.setActualInteractableObject(null);

            return "Pristupuješ k " + approachTo;
        }

        return "Žiadny taký objekt alebo osobu tu nevidím! Nepomýlil si sa náhodou?";
    }
}
