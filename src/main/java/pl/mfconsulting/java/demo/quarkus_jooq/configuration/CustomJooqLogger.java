package pl.mfconsulting.java.demo.quarkus_jooq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

@Slf4j
public class CustomJooqLogger implements ExecuteListener {
    @Override
    public void renderStart(ExecuteContext ctx) {
        log.debug("Executing query: {}", ctx.sql());
    }
}
