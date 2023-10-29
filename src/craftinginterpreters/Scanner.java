package craftinginterpreters;

import java.util.ArrayList;
import java.util.List;

import static craftinginterpreters.TokenType.*;

public class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  Scanner (String source) {
    this.source = source;
  }

  List<Token> scanTokens () {
    while (!isAtEnd()) {
      // we're at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private boolean isAtEnd () {
    return current >= source.length();
  }

  private void scanToken () {
    final char CHAR = advance ();

    switch (CHAR) {
      case '(' : addToken(LEFT_PAREN); break;
      case ')' : addToken(RIGHT_PAREN); break;
      case '{' : addToken(LEFT_BRACE); break;
      case '}' : addToken(RIGHT_BRACE); break;
      case ',' : addToken(COMMA); break;
      case '.' : addToken(DOT); break;
      case '-' : addToken(MINUS); break;
      case '+' : addToken(PLUS); break;
      case ';' : addToken(SEMICOLON); break;
      case '*' : addToken(STAR); break;
      case '/' : addToken(FORWARD_SLASH); break;

      default: {
        Lox.error(line, "Unexpected character: '" + CHAR + "' found");
        break;
      }
    }
  }

  private char advance() {
    //  current++;
    //  return source.charAt(current - 1);
    return source.charAt(current++); // this should fetch the current and execute the increment afterward
  }

  private void addToken (TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}
