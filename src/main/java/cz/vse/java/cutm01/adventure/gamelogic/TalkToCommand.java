package cz.vse.java.cutm01.adventure.gamelogic;

/**
 * TalkTo command implementation, player can use this command to talk with non-player characters in
 * game
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
class TalkToCommand implements Command {
  private static final String NAME = CommandName.TALK_TO.getCommandName();
  private static final String DESCRIPTION = CommandDescription.TALK_TO.getCommandDescription();
  private final GamePlan gamePlan;

  public TalkToCommand(GamePlan gamePlan) {
    this.gamePlan = gamePlan;
  }

  @Override
  public String getCommandName() {
    return NAME;
  }

  @Override
  public String getCommandDescription() {
    return DESCRIPTION;
  }

  /**
   * Method is used to interact (talk) with non-player characters in game
   *
   * @param commandParameters max. 1 name of non-player character player wants to talk with
   * @return NPC's speech or "error" output if NPC was not found in actual room, or more than one
   *     names were used
   */
  @Override
  public String executeCommand(String... commandParameters) {
    boolean isPlayerStandingNextToNPC = gamePlan.getActualNonPlayerCharacter() != null;
    if (!isPlayerStandingNextToNPC) {
      return "Asi by bolo lepšie najprv k nejakej osobe pristúpiť a až potom sa s ňou začať rozprávať";
    }

    if (commandParameters.length == 0) {
      return gamePlan.getActualNonPlayerCharacter().getSpeech();
    }

    if (commandParameters.length == 1) {
      String characterToTalkWith = commandParameters[0];
      String characterPlayersStandsBy = gamePlan.getActualNonPlayerCharacter().getName();
      boolean isSameCharacter = characterToTalkWith.equals(characterPlayersStandsBy);
      if (!isSameCharacter) {
        return "Skús najprv pristúpiť k osobe, s ktorou sa chceš porozprávať. Aktuálne stojíš pri "
            + characterPlayersStandsBy
            + "\n";
      }
      return gamePlan.getActualNonPlayerCharacter().getSpeech();
    }

    return "Nie tak zhurta! Dokážem sa rozprávať naraz iba s jednou osobou, inak by v tom bol chaos";
  }
}
