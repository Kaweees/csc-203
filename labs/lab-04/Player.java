import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Player {
  // instance variables
  private PlayerKind kind;
  private float money;
  private ArrayList<Float> moneyOverTime;
  Random random = new Random();
  private int red, green, blue;

  private int[] lotteryNumbers = new int[5];

  // constructor
  public Player(PlayerKind pK, float startFunds) {
    kind = pK;
    money = startFunds;
    moneyOverTime = new ArrayList<Float>();
    moneyOverTime.add(startFunds);
    red = random.nextInt(100);
    green = random.nextInt(100);
    blue = random.nextInt(100);

    // overall blue tint to POORLY_PAID
    if (kind == PlayerKind.WELL_PAID) {
      red += 100;
    } else {
      blue += 100;
    }
  }

  public int getR() {
    return red;
  }

  public int getG() {
    return green;
  }

  public int getB() {
    return blue;
  }

  public int[] getLotteryNumbers() {
    return lotteryNumbers;
  }

  public float getMoney() {
    return money;
  }

  public void setMoney(float newMoney) {
    money = newMoney;
  }

  public PlayerKind getKind() {
    return kind;
  }

  public ArrayList<Float> getFunds() {
    return moneyOverTime;
  }

  public void updateMoneyEachYear() {
    moneyOverTime.add(money);
  }

  public void playRandom() {
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
}
