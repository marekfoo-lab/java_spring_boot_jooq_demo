package pl.mfconsulting.java.demo.spring.jooq.configuration;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static pl.mfconsulting.java.demo.spring.jooq.generated.Tables.*;


@TestConfiguration
@Profile("itest")
public abstract class ConfigurationOfIntegration {
    private static final String DEFAULT_PASS = "test_pass";

    @Autowired
    private DSLContext dsl;

    @BeforeEach
    @Transactional
    void setupTestData() {
        dsl.deleteFrom(ROLE_ACCOUNT).execute();
        dsl.deleteFrom(ROLE).execute();
        dsl.deleteFrom(LOGIN_HISTORY).execute();
        dsl.deleteFrom(ADDRESS).execute();
        dsl.deleteFrom(ACCOUNT).execute();

        dsl.execute("ALTER SEQUENCE ACCOUNT_ID_SEQ RESTART WITH 1");
        dsl.execute("ALTER SEQUENCE ADDRESS_ID_SEQ RESTART WITH 1");
        dsl.execute("ALTER SEQUENCE ROLE_ID_SEQ RESTART WITH 1");
        dsl.execute("ALTER SEQUENCE LOGIN_HISTORY_ID_SEQ RESTART WITH 1");

        createUser("login1", "name1", "surname1", "mail1");
        createUser("login2", "name2", "surname2", "mail2");

        createAddress("street1", "zip1", "city1", "type1");
        createAddress("street2", "zip2", "city2", "type2");

        createRole("role1");
        createRole("role2");

        addRoleToUser(1, 1);
        addRoleToUser(2, 2);


        addLoginHistory(1, "192.168.0.1", LocalDateTime.now(), "1");
        addLoginHistory(1, "192.168.0.1", LocalDateTime.now(), "2");
        addLoginHistory(2, "192.168.0.1", LocalDateTime.now(), "1");
    }

    protected void createAddress(String street, String zip, String city, String type) {
        dsl.insertInto(ADDRESS)
                .set(ADDRESS.ACCOUNT_ID, 1)
                .set(ADDRESS.STREET, street)
                .set(ADDRESS.ZIP_CODE, zip)
                .set(ADDRESS.CITY, city)
                .set(ADDRESS.ADDRESS_TYPE, type)
                .execute();
    }

    protected void createUser(String login, String firstName, String lastName, String email) {
        dsl.insertInto(ACCOUNT)
                .set(ACCOUNT.LOGIN, login)
                .set(ACCOUNT.FIRST_NAME, firstName)
                .set(ACCOUNT.LAST_NAME, lastName)
                .set(ACCOUNT.PASSWORD, DEFAULT_PASS)
                .set(ACCOUNT.EMAIL, email)
                .execute();
    }

    protected void createRole(String roleName) {
        dsl.insertInto(ROLE)
                .set(ROLE.NAME, roleName)
                .execute();
    }

    protected void addRoleToUser(Integer userId, Integer roleId) {
        dsl.insertInto(ROLE_ACCOUNT)
                .set(ROLE_ACCOUNT.ACCOUNT_ID, userId)
                .set(ROLE_ACCOUNT.ROLE_ID, roleId)
                .execute();
    }

    protected void addLoginHistory(Integer userId, String ipAddress, LocalDateTime lastLogin, String isSuccess) {
        dsl.insertInto(LOGIN_HISTORY)
                .set(LOGIN_HISTORY.ACCOUNT_ID, userId)
                .set(LOGIN_HISTORY.LAST_LOGIN, lastLogin)
                .set(LOGIN_HISTORY.IP_ADDRESS, ipAddress)
                .set(LOGIN_HISTORY.IS_SUCCESS, isSuccess)
                .execute();
    }
}
