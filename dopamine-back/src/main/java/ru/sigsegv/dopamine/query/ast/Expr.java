package ru.sigsegv.dopamine.query.ast;

public sealed interface Expr permits AndExpr, OrExpr, NotExpr, FuncExpr, WordExpr {
}
