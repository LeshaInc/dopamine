package ru.sigsegv.dopamine.query.builder;

import org.jooq.Condition;

@FunctionalInterface
public interface TermBuilder {
    Condition build(String word);
}
