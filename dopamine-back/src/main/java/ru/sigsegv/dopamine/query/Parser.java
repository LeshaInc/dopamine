package ru.sigsegv.dopamine.query;

import ru.sigsegv.dopamine.query.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Parser {
    private final Lexer lexer;

    public Parser(String input) {
        lexer = new Lexer(input);
    }

    public static Expr parse(String input) {
        var parser = new Parser(input);
        return parser.expr();
    }

    public Expr expr() {
        return infixExpr(TokenKind.KW_OR, OrExpr::new, () ->
                infixExpr(null, AndExpr::new, this::term));
    }

    private Expr infixExpr(TokenKind separator, Function<List<Expr>, Expr> ctor, Supplier<Expr> supplier) {
        var exprs = new ArrayList<Expr>();

        while (true) {
            if (separator != null && !exprs.isEmpty()) {
                if (lexer.nextIs(separator)) {
                    lexer.next();
                } else {
                    break;
                }
            }

            var term = supplier.get();
            if (term == null) break;
            exprs.add(term);
        }

        if (exprs.isEmpty())
            return null;
        if (exprs.size() == 1)
            return exprs.get(0);
        return ctor.apply(exprs);
    }

    private Expr term() {
        if (lexer.nextIs(TokenKind.KW_NOT)) {
            lexer.next();
            return new NotExpr(term());
        }

        if (lexer.nextIs(TokenKind.L_PAREN)) {
            lexer.next();
            var expr = expr();
            lexer.next();
            return expr;
        }

        if (!lexer.nextIs(TokenKind.WORD))
            return null;

        var word = lexer.next().content();

        if (lexer.nextIs(TokenKind.COLON)) {
            lexer.next();
            var arg = term();
            return new FuncExpr(word, arg);
        }

        return new WordExpr(word);
    }
}
