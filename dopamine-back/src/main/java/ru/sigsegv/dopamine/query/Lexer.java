package ru.sigsegv.dopamine.query;

import java.util.regex.Pattern;

public class Lexer {
    private String remaining;
    private Token saved;

    public Lexer(String input) {
        remaining = input;
    }

    public boolean nextIs(TokenKind kind) {
        var token = peek();
        return token != null && token.kind() == kind;
    }

    public Token peek() {
        if (saved != null)
            return saved;
        saved = next();
        return saved;
    }

    private static Pattern REGEX = Pattern.compile("[\s,;:().]");

    public Token next() {
        if (saved != null) {
            var token = saved;
            saved = null;
            return token;
        }

        remaining = remaining.stripLeading();
        if (remaining.isEmpty()) return null;

        var firstChar = remaining.charAt(0);

        var token = switch (firstChar) {
            case '(' -> TokenKind.L_PAREN;
            case ')' -> TokenKind.R_PAREN;
            case ':' -> TokenKind.COLON;
            default -> null;
        };

        if (token != null) {
            remaining = remaining.substring(1);
            return new Token(token, String.valueOf(firstChar));
        }

        var word = "";

        var matcher = REGEX.matcher(remaining);
        if (matcher.find()) {
            word = remaining.substring(0, matcher.start());
            remaining = remaining.substring(matcher.start());
        } else {
            word = remaining;
            remaining = "";
        }

        token = switch (word) {
            case "ИЛИ" -> TokenKind.KW_OR;
            case "НЕ" -> TokenKind.KW_NOT;
            default -> TokenKind.WORD;
        };

        return new Token(token, word);
    }
}
