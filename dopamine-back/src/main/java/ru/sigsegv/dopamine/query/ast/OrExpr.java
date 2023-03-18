package ru.sigsegv.dopamine.query.ast;

import java.util.List;

public record OrExpr(List<Expr> args) implements Expr {
}
