package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class CommandsList - eviduje seznam přípustných příkazů adventury. Používá se pro rozpoznávání
 * příkazů a vrácení odkazu na třídu implementující konkrétní příkaz. Každý nový příkaz (instance
 * implementující rozhraní Prikaz) se musí do seznamu přidat metodou vlozPrikaz.
 *
 * <p>Tato třída je součástí jednoduché textové hry.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2016/2017
 */
class CommandsList {

    private final Map<String, Command> commandsMap; // mapa pro uložení přípustných příkazů
    private int
        longestCommandNameLength; // used in getCommandsWithTheirUsage for proper formatting of output
    // text

    /**
     * Konstruktor
     */
    public CommandsList() {
        commandsMap = new HashMap<>();
        this.longestCommandNameLength = 0;
    }

    /**
     * Vkládá nový příkaz.
     *
     * @param command Instance třídy implementující rozhraní Command
     */
    public void addCommand(Command command) {
        commandsMap.put(command.getCommandName(), command);

        // used in getCommandsWithTheirUsage for proper formatting of output text
        int commandNameLength = command.getCommandName().length();
        if (commandNameLength > longestCommandNameLength) {
            longestCommandNameLength = commandNameLength;
        }
    }

    /**
     * Vrací odkaz na instanci třídy implementující rozhraní Command, která provádí příkaz uvedený
     * jako parametr.
     *
     * @param commandName klíčové slovo příkazu, který chce hráč zavolat
     * @return instance třídy, která provede požadovaný příkaz
     */
    public Command getCommand(String commandName) {
        return commandsMap.getOrDefault(commandName, null);
    }

    /**
     * Kontroluje, zda zadaný řetězec je přípustný příkaz.
     *
     * @param commandName Řetězec, který se má otestovat, zda je přípustný příkaz
     * @return Vrací hodnotu true, pokud je zadaný řetězec přípustný příkaz
     */
    public boolean isCommandValid(String commandName) {
        return commandsMap.containsKey(commandName);
    }

    /**
     * Method is used to print all command names from command list together with their proper usage
     * (description).<br> Each command is printed together with his description on separate line
     *
     * @return String with command names with description of their usage
     */
    public String getCommandsWithTheirUsage() {
        StringBuilder commandsWithTheirUsage = new StringBuilder();
        Iterator<String> itemIterator = commandsMap.keySet().iterator();

        String commandName;
        String commandDescription;
        int numberOfSpacesForTextDivide;
        while (itemIterator.hasNext()) {
            commandName = itemIterator.next();
            commandDescription = commandsMap.get(commandName).getCommandDescription();

            commandsWithTheirUsage.append(commandName);
            // command's name and description will be separated by printing 4 spaces
            numberOfSpacesForTextDivide = longestCommandNameLength - commandName.length() + 4;
            commandsWithTheirUsage.append(" ".repeat(Math.max(0, numberOfSpacesForTextDivide)));
            commandsWithTheirUsage.append(commandDescription);
            commandsWithTheirUsage.append("\n");
        }

        return commandsWithTheirUsage.toString();
    }
}
