package ru.sigsegv.dopamine.util;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    private final ConnectionFactory cfi;

    public JooqConfig(ConnectionFactory cfi) {
        this.cfi = cfi;
    }

    @Bean
    public DSLContext jOOQDSLContext() {
        return DSL.using(cfi, SQLDialect.POSTGRES).dsl();
    }
}
