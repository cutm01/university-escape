package cz.vse.java.cutm01.adventure.ui;

import cz.vse.java.cutm01.adventure.gamelogic.CommandName;
import cz.vse.java.cutm01.adventure.gamelogic.Game;

import java.util.Scanner;

/**
 * Class GameTextInterface
 *
 * <p>Toto je uživatelského rozhraní aplikace Adventura Tato třída vytváří instanci třídy GameImpl,
 * která představuje logiku aplikace. Čte vstup zadaný uživatelem a předává tento řetězec logice a
 * vypisuje odpověď logiky na konzoli.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
public class GameTextInterface {
  private final Game game;

  /** Vytváří hru. */
  public GameTextInterface(Game game) {
    this.game = game;
  }

  /**
   * Hlavní metoda hry. Vypíše úvodní text a pak opakuje čtení a zpracování příkazu od hráče do
   * konce hry (dokud metoda konecHry() z logiky nevrátí hodnotu true). Nakonec vypíše text epilogu.
   */
  public void play() {
    System.out.println(game.getPrologue());

    // základní cyklus programu - opakovaně se čtou příkazy a poté
    // se provádějí do konce hry.

    while (!game.isGameOver()) {
      // player reached the final room and game ends
      if (game.getGamePlan().getHasPlayerReachedFinalRoom()) {
        break;
      }
      String userInput = readUserInput();
      System.out.println(game.parseUserInput(userInput));
    }

    if (game.getWasGameTerminatedUsingEndGameCommand()) {
      System.out.println(
          "Hra bola ukončená príkazom " + CommandName.END_GAME.getCommandName() + "\n");
    } else {
      System.out.println(game.getGameEnding());
    }
  }

  /**
   * Metoda přečte příkaz z příkazového řádku
   *
   * @return Vrací přečtený příkaz jako instanci třídy String
   */
  private String readUserInput() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("> ");
    return scanner.nextLine();
  }
}
