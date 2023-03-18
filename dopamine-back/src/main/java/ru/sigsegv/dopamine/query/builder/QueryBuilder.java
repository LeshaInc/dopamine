package ru.sigsegv.dopamine.query.builder;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import ru.sigsegv.dopamine.query.ast.*;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private final Map<String, TermBuilder> functions = new HashMap<>();
    private final TermBuilder termBuilder;

    public QueryBuilder(TermBuilder termBuilder) {
        this.termBuilder = termBuilder;
    }

    public void addFunction(String name, TermBuilder termBuilder) {
        functions.put(name, termBuilder);
    }

    public Condition build(Expr expr) {
        if (expr instanceof AndExpr andExpr)
            return buildAnd(andExpr);
        if (expr instanceof OrExpr orExpr)
            return buildOr(orExpr);
        if (expr instanceof NotExpr notExpr)
            return buildNot(notExpr);
        if (expr instanceof FuncExpr funcExpr)
            return buildFunc(funcExpr);
        if (expr instanceof WordExpr wordExpr)
            return buildWord(wordExpr);
        return DSL.trueCondition();
    }

    private Condition buildAnd(AndExpr expr) {
        var args = expr.args().iterator();
        var cond = build(args.next());
        while (args.hasNext()) {
            cond = cond.and(build(args.next()));
        }
        return cond;
    }

    private Condition buildOr(OrExpr expr) {
        var args = expr.args().iterator();
        var cond = build(args.next());
        while (args.hasNext()) {
            cond = cond.or(build(args.next()));
        }
        return cond;
    }

    private Condition buildNot(NotExpr expr) {
        return build(expr.arg()).not();
    }

    private Condition buildFunc(FuncExpr expr) {
        var func = functions.getOrDefault(expr.name(), null);
        if (func == null) return DSL.trueCondition();

        var qb = new QueryBuilder(func);
        return qb.build(expr.arg());
    }

    private Condition buildWord(WordExpr expr) {
        return termBuilder.build(expr.word());
    }
}
