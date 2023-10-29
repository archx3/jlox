package craftinginterpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
  static boolean hadError = false;

  public static void main(String[] args) throws IOException {
    final int ARGS_LEN = args.length;

    if (ARGS_LEN > 1) {
      System.out.println("Usage : jlox [script]");
      System.exit(64);
    } else if (ARGS_LEN == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

    //indicate an error in the exit code
    if (hadError) System.exit(65);
  }

  private static void runPrompt () throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
      System.out.println("> ");
      String line = reader.readLine();

      if (line == null) break;

      run(line);
      hadError = false;
    }
  }

  private static void run (String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    // let's just print the tokens for now
    for (Token token : tokens) {
      System.out.println(token);
    }
  }

  static void error (int line, String message) {
    report(line, "", message);
  }

  private static void report (int line, String where, String message) {
    System.err.println("Error at line: " + line + " col: " + where + ": " +  message );
    hadError = true;
  }
}
