/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package cz.vse.java.cutm01.adventure.main;

import cz.vse.java.cutm01.adventure.gamelogic.Game;
import cz.vse.java.cutm01.adventure.gamelogic.GameImpl;
import cz.vse.java.cutm01.adventure.ui.GameTextInterface;

/*******************************************************************************
 * Třída  Start je hlavní třídou projektu,
 * který představuje jednoduchou textovou adventuru určenou k dalším úpravám a rozšiřování
 *
 * @author Jarmila Pavlíčková
 * @version ZS 2016/2017
 */
public class Start {
    /***************************************************************************
     * Metoda, prostřednictvím níž se spouští celá aplikace.
     *
     * @param args Parametry příkazového řádku
     */
    public static void main(String[] args) {
        Game game = new GameImpl();
        GameTextInterface ui = new GameTextInterface(game);
        ui.play();
    }
}
