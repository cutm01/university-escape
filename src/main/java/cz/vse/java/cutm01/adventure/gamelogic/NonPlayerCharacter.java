package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * NonPlayerCharacter class represent characters in game, player can talk with them or give them
 * item
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class NonPlayerCharacter extends InteractableObject {

    private final String name;
    private final String speech;

    public NonPlayerCharacter(String nameDefinedInEnum) {
        this.name = NonPlayerCharacterName.getNonPlayerCharacterName(nameDefinedInEnum);
        this.speech = NonPlayerCharacterSpeech.getNonPlayerCharacterSpeech(nameDefinedInEnum);
    }

    public String getName() {
        return name;
    }

    public String getSpeech() {
        return speech;
    }
}
