import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class fileParser {
  public static void main(String[] args) throws FileNotFoundException {
    File fileIn = new File(args[0]);
    PrintWriter fileOut = new PrintWriter("drawMe.txt");

    Scanner in = new Scanner(fileIn);
    List<Point> coords = new ArrayList<>();

    while (in.hasNextLine()) {
      String[] line = in.nextLine().split(", ");
      double x = Double.parseDouble(line[0]);
      double y = Double.parseDouble(line[1]);
      double z = Double.parseDouble(line[2]);
      coords.add(new Point(x, y, z));
    }

    List<Point> pointsFiltered = coords.stream().filter(pt -> pt.z <= 2.0) // Remove
                                                                           // points
                                                                           // with
                                                                           // z
                                                                           // less
                                                                           // than
                                                                           // 2.0
        .map(pt -> new Point(pt.x * 0.5, pt.y * 0.5, pt.z * 0.5)) // Scale
                                                                  // down
                                                                  // points
                                                                  // by
                                                                  // half
        .map(pt -> new Point(pt.x - 150, pt.y - 37, pt.z)) // Translate
                                                           // points
                                                           // by
                                                           // (-150,
                                                           // -37)
        .toList();
    for (Point pt : pointsFiltered) {
      fileOut.println(pt);
    }
    fileOut.close();
    in.close();
  }
}
