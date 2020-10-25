package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

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
     *                      zobrazit uživateli.
     */
    public HelpCommand(CommandsList validCommands, GamePlan gamePlan) {
        this.validCommands = validCommands;
        this.gamePlan = gamePlan;
    }

    /**
     * Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání) @ return nazev
     * prikazu
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
     * Vrací základní nápovědu po zadání příkazu "napoveda". Nyní se vypisuje vcelku primitivní
     * zpráva a seznam dostupných příkazů.
     *
     * @return napoveda ke hre
     */
    @Override
    public String executeCommand(String... commandParameters) {
        return
            "Tvojou úlohou je dostať sa z areálu školy von na ulicu a zachrániť si tak život!"
            + SystemInfo.LINE_SEPARATOR + SystemInfo.LINE_SEPARATOR
            + "K dispozícii máš nasledovné príkazy:"
            + SystemInfo.LINE_SEPARATOR
            + validCommands.getCommandsWithTheirUsage()
            + SystemInfo.LINE_SEPARATOR
            + "Aktuálne sa nachádzaš v "
            + gamePlan.getActualRoom().getName()
            + ", mapa budovy:"
            + SystemInfo.LINE_SEPARATOR
            + getGameMap();
    }

    /**
     * Method provides game map as a part of HelpCommand
     *
     * @return String representing game map, i.e game rooms and how they are connected
     */
    private String getGameMap() {
        return "................................................................................" + SystemInfo.LINE_SEPARATOR
               + ".            ________    _______   _____                             ________  ." + SystemInfo.LINE_SEPARATOR
               + ".           |        |  |       | |     |                           |        | ." + SystemInfo.LINE_SEPARATOR
               + ".           | RB_201 |  |toalety| |satna|                           |kniznica| ." + SystemInfo.LINE_SEPARATOR
               + ".           |__   ___|  |__   __| |_   _|                           |___   __| ." + SystemInfo.LINE_SEPARATOR
               + ".           ___| |____    _| |_____ | |__                            ___| |___ ." + SystemInfo.LINE_SEPARATOR
               + ". ______   |          |  |               |   ________               |         |." + SystemInfo.LINE_SEPARATOR
               + ".|      |__|  chodba_ |__|   chodba_na_  |__|        |______________|         |." + SystemInfo.LINE_SEPARATOR
               + ".|RB_202 __   na_II._  __   I._poschodi   __   Nova_   spojovacia_    Stara_  |." + SystemInfo.LINE_SEPARATOR
               + ".|______|  | poschodi |  |               |  | budova     chodba       budova  |." + SystemInfo.LINE_SEPARATOR
               + ".          |__________|  |______   ______|  |         ______________          |." + SystemInfo.LINE_SEPARATOR
               + ".                            ___| |____     |___   __|              |_________|." + SystemInfo.LINE_SEPARATOR
               + ".                           |          |      __| |__    ___________           ." + SystemInfo.LINE_SEPARATOR
               + ".                           |kancelaria|     |       |  |           |          ." + SystemInfo.LINE_SEPARATOR
               + ".                           |__________|     |       |__|Vencovskeho|          ." + SystemInfo.LINE_SEPARATOR
               + ".                                            | dvor   __    aula    |          ." + SystemInfo.LINE_SEPARATOR
               + ".                                            |       |  |___________|          ." + SystemInfo.LINE_SEPARATOR
               + ".                                            |__    _|                         ." + SystemInfo.LINE_SEPARATOR
               + ".                                            ___|  |___                        ." + SystemInfo.LINE_SEPARATOR
               + ".                                           |  ulica   |                       ." + SystemInfo.LINE_SEPARATOR
               + ".                                           |__________|                       ." + SystemInfo.LINE_SEPARATOR
               + "................................................................................";
    }
}
