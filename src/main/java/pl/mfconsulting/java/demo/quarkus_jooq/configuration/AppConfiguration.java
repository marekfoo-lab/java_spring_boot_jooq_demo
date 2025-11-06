package pl.mfconsulting.java.demo.quarkus_jooq.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;

import javax.sql.DataSource;


@ApplicationScoped
public class AppConfiguration {
    @Inject
    DataSource dataSource;

    @Produces
    @ApplicationScoped
    @CustomDLS
    public DSLContext dslContext() {
        Settings settings = new Settings()
                .withMapRecordComponentParameterNames(true)
                .withMapConstructorPropertiesParameterNames(true);

        Configuration configuration = new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.POSTGRES)
                .set(settings)
                .set(new DefaultExecuteListenerProvider(new CustomJooqLogger()));

        return DSL.using(configuration);
    }
}
