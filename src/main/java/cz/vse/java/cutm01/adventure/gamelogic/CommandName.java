package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Commands player can use during game with their names, i.e. Strings which player has to type in
 * order to perform command
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum CommandName {
    APPROACH("pristup_k"),
    DROP("odhod"),
    END_GAME("koniec"),
    EXAMINE_ITEM("preskumaj_predmet"),
    EXAMINE_OBJECT("preskumaj_objekt"),
    OVERLOOK_ITEM("prehliadnut_si"),
    GO("chod"),
    HELP("napoveda"),
    LOOK_AROUND("rozhliadnut_sa"),
    SHOW_INVENTORY("obsah_batohu"),
    TAKE("vezmi"),
    TALK_TO("prihovor_sa"),
    USE("pouzi");

    private final String commandName;

    CommandName(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
