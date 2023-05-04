import java.util.Arrays;
import java.util.Random;

public class Game {
  Random random = new Random();
  private final int[] lotteryNumbers = new int[5];

  public void winningLotteryNumber() {
    for (int i = 0; i < lotteryNumbers.length; i++) {
      int randNum = random.nextInt(42) + 1;
      for (int j = 0; j < lotteryNumbers.length; j++) {
        if (randNum == lotteryNumbers[j]) {
          randNum = random.nextInt(42) + 1;
          j = 0;
        }
      }
      lotteryNumbers[i] = randNum;
    }
    Arrays.sort(lotteryNumbers);
  }

  public int getMatches(int[] playerNumbers) {
    int matches = 0;
    for (int lotteryNumber : lotteryNumbers) {
      for (int playerNumber : playerNumbers) {
        if (lotteryNumber == playerNumber) {
          matches++;
          break;
        }
      }
    }
    return matches;
  }

  public double getWinnings(int matches) {
    return switch (matches) {
    case 2 -> 1;
    case 3 -> 10.86;
    case 4 -> 197.53;
    case 5 -> 212534.83;
    default -> -1;
    };
  }
}