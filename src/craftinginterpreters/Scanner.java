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

      case '!' : {
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
      }
      case '=' : {
        addToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      }
      case '<' : {
        addToken(match('=') ? LESS_EQUAL : LESS);
        break;
      }
      case '>' : {
        addToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      }
      case '/' : {
        if (match('/')) {
          while (!isAtEnd() && peek() != '\n') {
            advance();
          }
        } else {
          addToken(FORWARD_SLASH);
          break;
        }
      }

      default: {
        Lox.error(line, "Unexpected character: '" + CHAR + "' found");
        break;
      }
    }
  }

  /**
   * Matches the given expected character with the current character.
   * Technically, match id doing a lookahead too;
   * advance() and peek() are the fundamental operators, and match() combines them.
   *
   * @param expected the character to be matched with the current character
   * @return true if the characters match, false otherwise
   */
  private boolean match (char expected) {
    if (isAtEnd()) return false;

    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  private char advance () {
    //  current++;
    //  return source.charAt(current - 1);
    return source.charAt(current++); // this should fetch the current and execute the increment afterward
  }

  private char peek () {
    if (isAtEnd()) return '\0'; // terminator null character or the null byte
    return source.charAt(current);
  }

  private void addToken (TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}
