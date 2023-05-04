import java.util.ArrayList;
import java.util.Random;

public final class CommunityLSim {
  // instance variables
  ArrayList<Player> players;
  int numPeeps;
  Random random = new Random();

  ArrayList<Integer> peepsWhoPlay;

  // Constructor
  public CommunityLSim(int numP) {
    numPeeps = numP;
    // create the players
    players = new ArrayList<>();

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
    for (Player p : players) {
      if (p.getKind() == PlayerKind.WELL_PAID) {
        p.setMoney(p.getMoney() + 0.1f);
      } else if (p.getKind() == PlayerKind.POORLY_PAID) {
        p.setMoney(p.getMoney() + 0.03f);
      }
    }
  }

  public void reDoWhoPlays() {
    peepsWhoPlay = new ArrayList<>();
    randomUniqIndx((int) Math.ceil((double) numPeeps / 2 * 0.6), 0, numPeeps / 2);
    randomUniqIndx((int) Math.ceil((double) numPeeps / 2 * 0.4), numPeeps / 2, numPeeps);
  }

  /*
   * TODO generate some random indices for who might play the lottery in a given
   * range of indices
   */
  public void randomUniqIndx(int numI, int startRange, int endRange) {
    for (int i = 0; i < numI; i++) {
      int index = random.nextInt(endRange - startRange) + startRange;
      if (!peepsWhoPlay.contains(index)) {
        peepsWhoPlay.add(index);
      } else {
        i--;
      }
    }
  }

  private void redistributeMoney() {
    int rand = random.nextInt(11);
    if (rand <= 3) {
      int poor_redis_index = random.nextInt(numPeeps / 2);
      players.get(poor_redis_index).setMoney(players.get(poor_redis_index).getMoney() + 1);
    } else {
      int well_redis_index = random.nextInt(numPeeps - numPeeps / 2) + numPeeps / 2;
      players.get(well_redis_index).setMoney(players.get(well_redis_index).getMoney() + 1);
    }
  }

  private float find_poorest_person() {
    float highest = 0.0f;
    for (Player peep : players) {
      if (peep.getMoney() > highest)
        highest = peep.getMoney();
    }
    return highest;
  }

  private float find_richest_person() {
    float lowest = players.get(0).getMoney();
    for (Player peep : players) {
      if (peep.getMoney() < lowest)
        lowest = peep.getMoney();
    }
    return lowest;
  }

  public void simulateYears(int numYears) {
    /* now simulate lottery play for some years */
    Game game = new Game();
    game.winningLotteryNumber();
    for (int year = 0; year < numYears; year++) {
      reDoWhoPlays();
      addPocketChange();
      reDoWhoPlays();
      for (int peep : peepsWhoPlay) {
        Player p = players.get(peep);
        p.playRandom();
        int matches = game.getMatches(p.getLotteryNumbers());
        p.setMoney((float) (p.getMoney() + game.getWinnings(matches)));
        if (matches == 0 || matches == 1) {
          redistributeMoney();
        }
      }
      for (Player p : players) {
        p.updateMoneyEachYear();
      }
      System.out.printf("After year %d:\n", year);
      System.out.printf("The person with the most money has: %f\n", find_richest_person());
      System.out.printf("The person with the least money has: %f\n", find_poorest_person());
    } //years
  } // years
}