package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Třída EndGameCommand implementuje pro hru příkaz konec. Tato třída je součástí jednoduché textové
 * hry.
 *
 * @author Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
class EndGameCommand implements Command {

  private static final String NAME = CommandName.END_GAME.getCommandName();
  private static final String DESCRIPTION = CommandDescription.END_GAME.getCommandDescription();

  private final GameImpl game;

  /**
   * Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání) @ return nazev prikazu
   */
  @Override
  public String getCommandName() {
    return NAME;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  /**
   * Konstruktor třídy
   *
   * @param game odkaz na hru, která má být příkazem konec ukončena
   */
  public EndGameCommand(GameImpl game) {
    this.game = game;
  }

  /**
   * V případě, že příkaz má jen jedno slovo "konec" hra končí(volá se metoda setKonecHry(true))
   * jinak pokračuje např. při zadání "konec a".
   *
   * @return zpráva, kterou vypíše hra hráči
   */
  @Override
  public String executeCommand(String... commandParameters) {
    if (commandParameters.length > 0) {
      return "Hra sa dá ukončiť aj bez tých zbytočných slov okolo, skús to ešte raz";
    }

    game.setIsGameOver(true);
    game.setWasGameTerminatedUsingEndGameCommand(true);
    return "Rozhodol si sa to vzdať? Nuž, čo sa dá robiť...Ďakujem za zahranie!";
  }
}
