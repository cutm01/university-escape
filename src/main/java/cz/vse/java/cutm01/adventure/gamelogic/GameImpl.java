package cz.vse.java.cutm01.adventure.gamelogic;

import cz.vse.java.cutm01.adventure.main.SystemInfo;

/**
 * Třída GameImpl - třída představující logiku adventury.
 *
 * <p>Toto je hlavní třída logiky aplikace. Tato třída vytváří instanci třídy GamePlan, která
 * inicializuje mistnosti hry a vytváří seznam platných příkazů a instance tříd provádějící
 * jednotlivé příkazy. Vypisuje uvítací a ukončovací text hry. Také vyhodnocuje jednotlivé příkazy
 * zadané uživatelem.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
public class GameImpl implements Game {

    private final CommandsList validCommands; // obsahuje seznam přípustných příkazů
    private final GamePlan gamePlan;
    private boolean wasGameTerminatedUsingEndGameCommand = false;
    private boolean isGameOver = false;

    /**
     * Vytváří hru a inicializuje místnosti (prostřednictvím třídy GamePlan) a seznam platných
     * příkazů.
     */
    public GameImpl() {
        gamePlan = new GamePlan();

        validCommands = new CommandsList();
        validCommands.addCommand(new ApproachCommand(gamePlan));
        validCommands.addCommand(new DropCommand(gamePlan));
        validCommands.addCommand(new EndGameCommand(this));
        validCommands.addCommand(new ExamineItemCommand(gamePlan));
        validCommands.addCommand(new ExamineObjectCommand(gamePlan));
        validCommands.addCommand(new OverlookItemCommand(gamePlan));
        validCommands.addCommand(new GoCommand(gamePlan));
        validCommands.addCommand(new HelpCommand(validCommands, gamePlan));
        validCommands.addCommand(new LookAroundCommand(gamePlan));
        validCommands.addCommand(new ShowInventoryCommand(gamePlan));
        validCommands.addCommand(new TakeCommand(gamePlan));
        validCommands.addCommand(new TalkToCommand(gamePlan));
        validCommands.addCommand(new UseCommand(gamePlan));
    }

    /**
     * Method returns game prologue displayed in text version of game
     * @return String representing game prologue showed in text version of game
     */
    public String getPrologueInTextGameVersion() {
        return "Jún 2020, celý svet sa pomaly spamätáva z krízy, ktorú spôsobil koronavírus."
               + SystemInfo.LINE_SEPARATOR
               + "Od získania vysnívaného titulu ťa delí už iba úspešné zvládnutie štátnic."
               + SystemInfo.LINE_SEPARATOR
               + "Sedíš v prednáškovej miestnosti pripravený vylosovať si otázky k tvojej ústnej skúške, keď tu zrazu..."
               + SystemInfo.LINE_SEPARATOR
               + "Priestormi budovy sa ozve hlasný výbuch. Vyzerá to, že neznámy bomberman už ďalej nezvládol žiarliť na koronavírus,"
               + SystemInfo.LINE_SEPARATOR
               + "ktorý dokázal zavrieť všetky školy na dobu niekoľkých mesiacov bez jediného anonymného e-mailu"
               + SystemInfo.LINE_SEPARATOR
               + "a tak sa rozhodol dokonať svoje diabolské dielo – v budove školy odpálil bombu."
               + SystemInfo.LINE_SEPARATOR
               + "V rýchlosti berieš so sebou svoj batoh a snažíš sa dostať von skôr než celá budova skolabuje!"
               + SystemInfo.LINE_SEPARATOR
               + " ________________________________________________________________________"
               + SystemInfo.LINE_SEPARATOR
               + "|Pokiaľ si nevieš rady, môžeš kedykoľvek počas hry použiť príkaz napoveda|"
               + SystemInfo.LINE_SEPARATOR
               + "|________________________________________________________________________|"
               + SystemInfo.LINE_SEPARATOR
               + SystemInfo.LINE_SEPARATOR
               + gamePlan.getActualRoom().getRoomDescriptionWithExits();
    }

    /**
     * Method returns game prologue displayed in graphical version of game
     * @return String representing game prologue showed in graphical version of game
     */
    public String getPrologueInGraphicalGameVersion() {
        return "Jún 2020, celý svet sa pomaly spamätáva z krízy, ktorú spôsobil koronavírus."
                + SystemInfo.LINE_SEPARATOR
                + "Od získania vysnívaného titulu ťa delí už iba úspešné zvládnutie štátnic."
                + SystemInfo.LINE_SEPARATOR
                + "Sedíš v prednáškovej miestnosti pripravený vylosovať si otázky k tvojej ústnej skúške, keď tu zrazu..."
                + SystemInfo.LINE_SEPARATOR
                + "Priestormi budovy sa ozve hlasný výbuch. Vyzerá to, že neznámy bomberman už ďalej nezvládol žiarliť na koronavírus,"
                + SystemInfo.LINE_SEPARATOR
                + "ktorý dokázal zavrieť všetky školy na dobu niekoľkých mesiacov bez jediného anonymného e-mailu"
                + SystemInfo.LINE_SEPARATOR
                + "a tak sa rozhodol dokonať svoje diabolské dielo – v budove školy odpálil bombu."
                + SystemInfo.LINE_SEPARATOR
                + "V rýchlosti berieš so sebou svoj batoh a snažíš sa dostať von skôr než celá budova skolabuje!";
    }

    /**
     * Method returns game ending text which varies according to decision which player made during
     * the game
     *
     * @return String with game ending text
     */
    public String getGameEnding() {
        StringBuilder gameEnding = new StringBuilder();

        // player can either escape through courtyard...
        if (gamePlan.getPlayer().hasPlayerEscapedThroughCourtyard()) {
            gameEnding
                .append(
                    "Na poslednú chvíľu opúšťaš budovu a na ulici sa pripájaš k ostatným preživším."
                    + SystemInfo.LINE_SEPARATOR)
                .append("Gratulujem, úspešne sa ti podarilo dostať von!")
                .append(SystemInfo.LINE_SEPARATOR);
        }
        // ...or through window in Old Building using rope
        if (gamePlan.getPlayer().hasPlayerEscapedUsingWindow()) {
            gameEnding
                .append(
                    "Všetci so zatajeným dychom pozorujú ako sa zlaňuješ z okna Starej budovy")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Gratulujem, úspešne sa ti podarilo dostať von!")
                .append(SystemInfo.LINE_SEPARATOR);
        }

        // add game ending according to actions which player did during game:
        // player passed by coughing teacher and get Covid-19
        if (gamePlan.getPlayer().hasPlayerPassedByCoughingTeacher()) {
            gameEnding
                .append(
                    "Má to však háčik... po 2 týždňoch sa u teba začali prejavovať respiračné problémy")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("a musel si byť hospitalizovaný v Nemocnici Na Bulovce.")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "Nabudúce si dávaj väčší pozor okolo koho prechádzaš, a zváž či si nenasadiť nejaký ochranný prvok!")
                .append(SystemInfo.LINE_SEPARATOR);

        }
        // player attacked cleaning lady with pen, rope or fire extinguisher
        else if (gamePlan.getPlayer().hasPlayerAttackedCleaningLady()) {
            gameEnding
                .append(
                    "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tej úbohej pani")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostala von živá,")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("inak by si z kolabujúcej budovy putoval rovno do väzenia.")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!")
                .append(SystemInfo.LINE_SEPARATOR);
        }
        // player attacked IT admin with pen, rope or fire extinguisher
        else if (gamePlan.getPlayer().hasPlayerAttackedITAdmin()) {
            gameEnding
                .append(
                    "Má to však háčik...vyzerá to, že si niekto všimol to, čo si urobil tomu úbohému IT adminovi")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "a rozhodne máš čo vysvetľovať polícii! Jediné šťastie, že sa nakoniec dostal von živý,")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("inak by si z kolabujúcej budovy putoval rovno do väzenia.")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia!")
                .append(SystemInfo.LINE_SEPARATOR);
        }
        // player attacked door keeper with pen, rope or fire extinguisher
        else if (gamePlan.getPlayer().hasPlayerAttackedDoorKeeper()) {
            gameEnding
                .append(
                    "Avšak...to si si naozaj myslel, že potom čo si urobil tomu vrátnikovi si iba tak spokojne odkráčaš domov?")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Z budovy si sa síce dostal, ale ešte máš čo vysvetľovať polícii")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "Zúfalá situácia si vyžaduje zúfale riešenie, ale skús to nabudúce bez tých prejavov násilia")
                .append(SystemInfo.LINE_SEPARATOR);
        }
        // player tried to bribe door keeper with stolen money
        else if (gamePlan.getPlayer().hasPlayerBribedDoorKeeper()) {
            gameEnding
                .append(
                    "Vyzerá to, že si ťa niekto všimol pri tvojom pokuse uplatiť vrátnika, ba čo viac, peniaze, ktorými")
                .append(SystemInfo.LINE_SEPARATOR)
                .append(
                    "si sa pokúšal podplatiť vrátnika, zrejme patrili práve tejto osobe a ty tak ihneď po uniknutí")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("z budovy školy putuješ na políciu, kde budeš mať čo vysvetľovať.")
                .append(SystemInfo.LINE_SEPARATOR)
                .append("Do budúcna by sa určite šlo dostať von aj iným spôsobom!")
                .append(SystemInfo.LINE_SEPARATOR);
        }

        return gameEnding.toString();
    }

    /**
     * Vrací true, pokud hra skončila.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     * Pak otestuje zda příkaz je klíčovým slovem např. jdi. Pokud ano spustí samotné provádění
     * příkazu.
     *
     * @param userInput text, který zadal uživatel jako příkaz do hry.
     * @return vrací se řetězec, který se má vypsat na obrazovku
     */
    public String parseUserInput(String userInput) {
        String[] words = userInput.split("[ \t]+");
        String commandName = words[0];
        String[] commandParameters = new String[words.length - 1];
        System.arraycopy(words, 1, commandParameters, 0, commandParameters.length);
        String textToDisplay;
        if (validCommands.isCommandValid(commandName)) {
            Command command = validCommands.getCommand(commandName);
            textToDisplay = command.executeCommand(commandParameters);
        } else {
            textToDisplay = "Tento príkaz nepoznám, použi príkaz napoveda, pokiaľ si nevieš rady";
        }

        return textToDisplay;
    }

    public boolean wasGameTerminatedUsingEndGameCommand() {
        return wasGameTerminatedUsingEndGameCommand;
    }

    public void setWasGameTerminatedUsingEndGameCommand(boolean terminateGame) {
        this.wasGameTerminatedUsingEndGameCommand = terminateGame;
    }

    /**
     * Nastaví, že je konec hry, metodu využívá třída EndGameCommand, mohou ji použít i další
     * implementace rozhraní Prikaz.
     *
     * @param isGameOver hodnota false= konec hry, true = hra pokračuje
     */
    @SuppressWarnings("SameParameterValue")
    void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * Metoda vrátí odkaz na herní plán, je využita hlavně v testech, kde se jejím prostřednictvím
     * získává aktualní místnost hry.
     *
     * @return odkaz na herní plán
     */
    public GamePlan getGamePlan() {
        return gamePlan;
    }

    /**
     * Method retuns all commands which can be used in actual game session
     * @return CommandsList instance containing all valid game commands which can player use
     */
    public CommandsList getCommandsList() {
        return validCommands;
    }
}
