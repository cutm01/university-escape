package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Non-player character speech which is shown to player after he uses TalkTo command
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum NonPlayerCharacterSpeech {
    CLEANING_LADY(
        "Pokúšaš sa prihovoriť upratovačke, ale tá ťa cez sluchátka, z ktorých jej hrá Dlouhá noc od Helenky Vondráčkovej vôbec nepočuje"),
    COUGHING_TEACHER(
        "Učiteľ neprestajne kašle, je to z toho prachu po výbuchu alebo má nejaké respiračné problémy? Každopádne si s ním asi veľmi nepokecáš"),
    IT_ADMIN(
        "Snažíš sa IT adminovi vysvetliť čo sa pred chvíľou stalo, ale ten stále rozpráva niečo o nejakej knihe, že by ju chcel priniesť?"),
    DOOR_KEEPER(
        "Vrátnik stále rozpráva o tom, že chce vidieť tvoje doklady, inak ťa nepustí von. Z čoho je taký podráždený?");

    private final String nonPlayerCharacterSpeech;
    private static final Map<String, String> NPC_SPEECH = new HashMap<>();

    static {
        for (NonPlayerCharacterSpeech npc : values()) {
            NPC_SPEECH.put(npc.toString(), npc.nonPlayerCharacterSpeech);
        }
    }

    NonPlayerCharacterSpeech(final String nonPlayerCharacterName) {
        this.nonPlayerCharacterSpeech = nonPlayerCharacterName;
    }

    public static String getNonPlayerCharacterSpeech(String characterName) {
        return NPC_SPEECH.get(characterName);
    }
}
