package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class AppConfiguration {
    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        DefaultConfiguration configuration = (DefaultConfiguration) new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.POSTGRES)
                .set(new DefaultExecuteListener() {
                    @Override
                    public void renderStart(ExecuteContext ctx) {
                        log.debug("Executing query: {}", ctx.sql());
                    }
                });

        return DSL.using(configuration);
    }
}
