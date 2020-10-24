package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * Třída HelpCommand implementuje pro hru příkaz napoveda. Tato třída je součástí jednoduché textové
 * hry.
 *
 * @author Jarmila Pavlickova, Luboš Pavlíček
 * @version pro školní rok 2016/2017
 */
class HelpCommand implements Command {

  private static final String NAME = CommandName.HELP.getCommandName();
  private static final String DESCRIPTION = CommandDescription.HELP.getCommandDescription();
  private final CommandsList validCommands;
  private final GamePlan gamePlan;

  /**
   * Konstruktor třídy
   *
   * @param validCommands seznam příkazů, které je možné ve hře použít, aby je nápověda mohla
   *     zobrazit uživateli.
   */
  public HelpCommand(CommandsList validCommands, GamePlan gamePlan) {
    this.validCommands = validCommands;
    this.gamePlan = gamePlan;
  }

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
   * Vrací základní nápovědu po zadání příkazu "napoveda". Nyní se vypisuje vcelku primitivní zpráva
   * a seznam dostupných příkazů.
   *
   * @return napoveda ke hre
   */
  @Override
  public String executeCommand(String... commandParameters) {
    return "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!\n\n"
        + "K dispozícii máš nasledovné príkazy:\n"
        + validCommands.getCommandsWithTheirUsage()
        + "\nAktuálne sa nachádzaš v "
        + gamePlan.getActualRoom().getName()
        + ", mapa budovy:\n"
        + getGameMap();
  }

  /**
   * Method provides game map as a part of HelpCommand
   *
   * @return String representing game map, i.e game rooms and how they are connected
   */
  private String getGameMap() {
    return "................................................................................\n"
        + ".            ________    _______   _____                             ________  .\n"
        + ".           |        |  |       | |     |                           |        | .\n"
        + ".           | RB_201 |  |toalety| |satna|                           |kniznica| .\n"
        + ".           |__   ___|  |__   __| |_   _|                           |___   __| .\n"
        + ".           ___| |____    _| |_____ | |__                            ___| |___ .\n"
        + ". ______   |          |  |               |   ________               |         |.\n"
        + ".|      |__|  chodba_ |__|   chodba_na_  |__|        |______________|         |.\n"
        + ".|RB_202 __   na_II._  __   I._poschodi   __   Nova_   spojovacia_    Stara_  |.\n"
        + ".|______|  | poschodi |  |               |  | budova     chodba       budova  |.\n"
        + ".          |__________|  |______   ______|  |         ______________          |.\n"
        + ".                            ___| |____     |___   __|              |_________|.\n"
        + ".                           |          |      __| |__    ___________           .\n"
        + ".                           |kancelaria|     |       |  |           |          .\n"
        + ".                           |__________|     |       |__|Vencovskeho|          .\n"
        + ".                                            | dvor   __    aula    |          .\n"
        + ".                                            |       |  |___________|          .\n"
        + ".                                            |__    _|                         .\n"
        + ".                                            ___|  |___                        .\n"
        + ".                                           |  ulica   |                       .\n"
        + ".                                           |__________|                       .\n"
        + "................................................................................";
  }
}
