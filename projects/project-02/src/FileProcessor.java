import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileProcessor {
  /**
   * Processes arithmetic expressions line-by-line in the given file.
   *
   * @param filePath Path to a file containing arithmetic expressions.
   */
  public static void processFile(String filePath) {
    File inputFile = new File(filePath);
    try (Scanner scan = new Scanner(inputFile)) {
      int count = 0;
      while (scan.hasNext()) {
        String line = scan.nextLine();
        line = line.replaceAll("\\s+", "");
        if (line.length() > 0) {
          count++;
          StringBuilder sb1 = new StringBuilder();
          StringBuilder sb2 = new StringBuilder();
          char operator = 0;
          boolean numAlrBuilt = false;
          for (char ch : line.toCharArray()) {
            if ('0' <= ch && ch <= '9') {
              (!numAlrBuilt ? sb1 : sb2).append(ch);
            } else if (ch == '+' || ch == '*' || ch == '^') {
              numAlrBuilt = true;
              operator = ch;
            }
          }
          BigNum num1 = new BigNum(sb1.toString());
          BigNum num2 = new BigNum(sb2.toString());
          BigNum result = null;
          switch (operator) {
          case '+' -> result = BigNum.add(num1, num2);
          case '*' -> result = BigNum.multiply(num1, num2);
          case '^' -> result = BigNum.exponent(num1, Integer.parseInt(num2.toString()));
          }
          if (count > 1) {
            System.out.print("\n");
          }
          System.out.printf("%s %s %s = %s", sb1, operator, sb2, result);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + inputFile.getPath());
    }
  }
}
