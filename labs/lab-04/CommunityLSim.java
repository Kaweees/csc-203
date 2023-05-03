import java.util.ArrayList;
import java.util.Random;

public final class CommunityLSim {
  // instance variables
  ArrayList<Player> players;
  int numPeeps;
  Random random = new Random();
  // TODO: you will need to add more instance variables

  // Constructor
  public CommunityLSim(int numP) {
    numPeeps = numP;
    // create the players
    players = new ArrayList<Player>();

    // generate a community of 30
    for (int i = 0; i < numPeeps; i++) {
      if (i < numPeeps / 2.0)
        players.add(new Player(PlayerKind.POORLY_PAID, (float) (99 + Math.random())));
      else
        players.add(new Player(PlayerKind.WELL_PAID, (float) (100.1 + Math.random())));
    }

  }

  public int getSize() {
    return numPeeps;
  }

  public Player getPlayer(int i) {
    return players.get(i);
  }

  // TODO
  public void addPocketChange() {

  }

  private void reDoWhoPlays() {

  }

  /*
   * TODO generate some random indices for who might play the lottery in a given
   * range of indices
   */
  public void randomUniqIndx(int numI, int startRange, int endRange) {}

  public void simulateYears(int numYears) {
    /* now simulate lottery play for some years */
    for (int year = 0; year < numYears; year++) {
      reDoWhoPlays();
      addPocketChange();
      // TODO: add code so that each member of the community who plays, plays
      // right now just everyone updates their list of funds each year
      for (Player p : players) {
        p.updateMoneyEachYear();
      }
    } // years
  }

}
