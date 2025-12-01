package pl.mfconsulting.java.demo.spring.jooq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
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
        Settings settings = new Settings()
                .withMapRecordComponentParameterNames(true)
                .withMapConstructorPropertiesParameterNames(true);

        DefaultConfiguration configuration = (DefaultConfiguration) new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.POSTGRES)
                .set(settings)
                .set(new MyDefaultExecuteListener());

        return DSL.using(configuration);
    }

    private static class MyDefaultExecuteListener extends DefaultExecuteListener {
        @Override
        public void renderStart(ExecuteContext ctx) {
            log.debug("Executing query: {}", ctx.sql());
        }
    }
}
