package ru.sigsegv.dopamine.query.ast;

import java.util.List;

public record AndExpr(List<Expr> args) implements Expr {
}
