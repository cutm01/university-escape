package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Commands player can use during game with their description
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum CommandDescription {
    APPROACH("hráč pristúpi k osobe alebo objektu v miestnosti, aby s ňou mohol interagovať"),
    DROP("hráč odhodí 1 alebo viac predmetov (oddelených medzerou) zo svojho inventáru"),
    END_GAME("hráč ukončí hru"),
    EXAMINE_ITEM("hráč podrobne preskúma 1 predmet zo svojho batohu alebo miestnosti"),
    EXAMINE_OBJECT("hráč podrobne preskúma 1 objekt, ktorý sa nachádza v miestnosti"),
    OVERLOOK_ITEM("hráč si v rýchlosti prehliadne 1 predmet zo svojho batohu alebo miestnosti"),
    GO("hráč prejde do ďalšej miestnosti"),
    HELP("zobrazí cieľ hry, hernú mapu a zoznam príkazov spolu s ich použitím"),
    LOOK_AROUND(
        "hráč sa rozhliadne po miestnosti v snahe nájsť v nej predmety, osoby alebo objekty"),
    SHOW_INVENTORY("zobrazí aktuálny obsah hráčovho batohu"),
    TAKE("hráč vezme 1 alebo viac predmetov (oddelených medzerou) z miestnosti"),
    TALK_TO("hráč sa prihovorí osobe, pri ktorej aktuálne stojí"),
    USE("hráč použije 1 predmet zo svojho inventáru, musí však stáť pri nejakej osobe alebo objekte");

    private final String commandDescription;

    CommandDescription(final String commandDescription) {
        this.commandDescription = commandDescription;
    }

    public String getCommandDescription() {
        return commandDescription;
    }
}
