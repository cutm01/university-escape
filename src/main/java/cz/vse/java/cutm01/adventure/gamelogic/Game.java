/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Rozhraní které musí implementovat hra, je na ně navázáno uživatelské rozhraní
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
public interface Game {
    // == VEŘEJNÉ KONSTANTY =====================================================
    // == DEKLAROVANÉ METODY ====================================================

    /**
     * Method returns game prologue displayed in text version of game
     * @return String representing game prologue showed in text version of game
     */
    String getPrologueInTextGameVersion();

    /**
     * Method returns game prologue displayed in graphical version of game
     * @return String representing game prologue showed in graphical version of game
     */
    public String getPrologueInGraphicalGameVersion();

    /**
     * Vrátí závěrečnou zprávu pro hráče.
     *
     * @return vrací se řetězec, který se má vypsat na obrazovku
     */
    String getGameEnding();

    /**
     * Vrací informaci o tom, zda hra již skončila, je jedno zda výhrou, prohrou nebo příkazem
     * konec.
     *
     * @return vrací true, pokud hra skončila
     */
    boolean isGameOver();

    /**
     * Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     * Pak otestuje zda příkaz je klíčovým slovem např. jdi. Pokud ano spustí samotné provádění
     * příkazu.
     *
     * @param userInput text, který zadal uživatel jako příkaz do hry.
     * @return vrací se řetězec, který se má vypsat na obrazovku
     */
    String parseUserInput(String userInput);

    /**
     * Metoda vrátí odkaz na herní plán, je využita hlavně v testech, kde se jejím prostřednictvím
     * získává aktualní místnost hry.
     *
     * @return odkaz na herní plán
     */
    GamePlan getGamePlan();

    boolean wasGameTerminatedUsingEndGameCommand();

    // == ZDĚDĚNÉ METODY ========================================================
    // == INTERNÍ DATOVÉ TYPY ===================================================
}
