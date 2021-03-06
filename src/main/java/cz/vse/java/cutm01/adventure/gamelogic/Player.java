package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Player class represent our player from game and contains constants which are used to achieve
 * different game endings
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public class Player {

    private final Inventory inventory;
    // following variables are used to show different endings to player after he reaches final room
    private boolean isPlayerWearingMedicalMask;
    private boolean isPlayerWearingMedicalSuit;
    private boolean hasPlayerEscapedUsingWindow;
    private boolean hasPlayerEscapedThroughCourtyard;
    private boolean hasPlayerAttackedCleaningLady;
    private boolean hasPlayerAttackedITAdmin;
    private boolean hasPlayerAttackedDoorKeeper;
    private boolean hasPlayerBribedDoorKeeper;
    // set to true if player passes by coughing teacher without wearing medical mask or suit
    private boolean hasPlayerPassedByCoughingTeacher;

    // region Constructors
    // --------------------------------------------------------------------------------
    public Player() {
        this.inventory = new Inventory();
        this.isPlayerWearingMedicalMask = false;
        this.isPlayerWearingMedicalSuit = false;
        this.hasPlayerEscapedUsingWindow = false;
        this.hasPlayerEscapedThroughCourtyard = false;
        this.hasPlayerAttackedCleaningLady = false;
        this.hasPlayerAttackedITAdmin = false;
        this.hasPlayerAttackedDoorKeeper = false;
        this.hasPlayerBribedDoorKeeper = false;
        this.hasPlayerPassedByCoughingTeacher = false;
    }
    // --------------------------------------------------------------------------------
    // endregion Constructors

    public Inventory getInventory() {
        return inventory;
    }

    // region Getter and Setters for variable alternating game ending
    // --------------------------------------------------------------------------------
    public boolean isPlayerWearingMedicalMask() {
        return isPlayerWearingMedicalMask;
    }

    public void setIsPlayerWearingMedicalMask(boolean playerWearingMedicalMask) {
        isPlayerWearingMedicalMask = playerWearingMedicalMask;
    }

    public boolean isPlayerWearingMedicalSuit() {
        return isPlayerWearingMedicalSuit;
    }

    public void setIsPlayerWearingMedicalSuit(boolean playerWearingMedicalSuit) {
        isPlayerWearingMedicalSuit = playerWearingMedicalSuit;
    }

    public boolean hasPlayerEscapedUsingWindow() {
        return hasPlayerEscapedUsingWindow;
    }

    public void setHasPlayerEscapedUsingWindow(boolean hasPlayerEscapedUsingWindow) {
        this.hasPlayerEscapedUsingWindow = hasPlayerEscapedUsingWindow;
    }

    public boolean hasPlayerEscapedThroughCourtyard() {
        return hasPlayerEscapedThroughCourtyard;
    }

    public void setHasPlayerEscapedThroughCourtyard(boolean hasPlayerEscapedThroughCourtyard) {
        this.hasPlayerEscapedThroughCourtyard = hasPlayerEscapedThroughCourtyard;
    }

    public boolean hasPlayerAttackedCleaningLady() {
        return hasPlayerAttackedCleaningLady;
    }

    public void setHasPlayerAttackedCleaningLady(boolean hasPlayerAttackedCleaningLady) {
        this.hasPlayerAttackedCleaningLady = hasPlayerAttackedCleaningLady;
    }

    public boolean hasPlayerAttackedITAdmin() {
        return hasPlayerAttackedITAdmin;
    }

    public void setHasPlayerAttackedITAdmin(boolean hasPlayerAttackedITAdmin) {
        this.hasPlayerAttackedITAdmin = hasPlayerAttackedITAdmin;
    }

    public boolean hasPlayerAttackedDoorKeeper() {
        return hasPlayerAttackedDoorKeeper;
    }

    public void setHasPlayerAttackedDoorKeeper(boolean hasPlayerAttackedDoorKeeper) {
        this.hasPlayerAttackedDoorKeeper = hasPlayerAttackedDoorKeeper;
    }

    public boolean hasPlayerBribedDoorKeeper() {
        return hasPlayerBribedDoorKeeper;
    }

    public void setHasPlayerBribedDoorKeeper(boolean hasPlayerBribedDoorKeeper) {
        this.hasPlayerBribedDoorKeeper = hasPlayerBribedDoorKeeper;
    }

    public boolean hasPlayerPassedByCoughingTeacher() {
        return hasPlayerPassedByCoughingTeacher;
    }

    public void setHasPlayerPassedByCoughingTeacher(boolean hasPlayerPassedByCoughingTeacher) {
        this.hasPlayerPassedByCoughingTeacher = hasPlayerPassedByCoughingTeacher;
    }
    // --------------------------------------------------------------------------------
    // endregion Getter and Setters for variable alternating game ending
}
