import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class contains many functions written in a procedural style. You will
 * reduce the size of this class over the next several weeks by refactoring this
 * codebase to follow an OOP style.
 */
public final class Functions {
  public static final Random rand = new Random();

  public static final String STUMP_KEY = "stump";
  public static final String SAPLING_KEY = "sapling";
  public static final String OBSTACLE_KEY = "obstacle";
  public static final String DUDE_KEY = "dude";
  public static final String HOUSE_KEY = "house";
  public static final String FAIRY_KEY = "fairy";
  public static final String TREE_KEY = "tree";

  public static final double TREE_ANIMATION_MAX = 0.600;
  public static final double TREE_ANIMATION_MIN = 0.050;
  public static final double TREE_ACTION_MAX = 1.400;
  public static final double TREE_ACTION_MIN = 1.000;
  public static final int TREE_HEALTH_MAX = 3;
  public static final int TREE_HEALTH_MIN = 1;

  public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz",
      "dirt_vert_left", "dirt_vert_right", "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));
}
