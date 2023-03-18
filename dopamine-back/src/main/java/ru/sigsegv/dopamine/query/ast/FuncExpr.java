package ru.sigsegv.dopamine.query.ast;

public record FuncExpr(String name, Expr arg) implements Expr {
}
