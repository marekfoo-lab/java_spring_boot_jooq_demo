package pl.mfconsulting.java.demo.spring.jooq.repository;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.mfconsulting.java.demo.spring.jooq.SpringJooqApplication;
import pl.mfconsulting.java.demo.spring.jooq.model.AccountDT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.mfconsulting.java.demo.spring.jooq.generated.Tables.*;

@SpringBootTest(classes = SpringJooqApplication.class)
@ActiveProfiles("test")
@Slf4j
class AccountRepositoryIntegrationTest {
    private static final String DEFAULT_PASS = "test_pass";

    @Autowired
    private AccountRepository accountRepository;

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

    @Test
    @Transactional
    void whenFindByIdThenReturnUser() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findById(Integer.valueOf(1));

        // Then
        assertTrue(result.isPresent(), "Konto powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(1, account.getId());
        assertEquals("name1", account.getFirstName());
        assertEquals("login1", account.getLogin());

        assertTrue(account.getAddresses().isEmpty());
    }

    @Test
    @Transactional
    void whenFindByLoginThenReturnUser() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findByLogin("login1");

        // Then
        assertTrue(result.isPresent(), "Konto powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(1, account.getId());
        assertEquals("name1", account.getFirstName());
        assertEquals("login1", account.getLogin());

        assertTrue(account.getAddresses().isEmpty());
    }

    @Test
    @Transactional
    void whenFindAllThenReturnTwoRecords() {
        // When
        List<AccountDT> accounts = accountRepository.findAll();

        // Then
        assertEquals(2, accounts.size(), "Powinny zostać znalezione dwa konta.");
        assertTrue(accounts.stream().anyMatch(a -> a.getLogin().equals("login1")));
        assertTrue(accounts.stream().anyMatch(a -> a.getLogin().equals("login2")));
    }

    @Test
    @Transactional
    void whenCreateAccountThenInsertNewAccountAndReturnGeneratedId() {
        // Given
        AccountDT newAccount = new AccountDT(3, "new.user", "Nowy", "Użytkownik", "new@test.pl", List.of());

        // When
        Optional<Integer> createdId = accountRepository.create(newAccount);

        // Then
        assertTrue(createdId.isPresent(), "Nowe konto powinno mieć wygenerowane ID.");
        assertTrue(createdId.get() > 0, "ID powinno być większe od 0");

        // When
        Optional<AccountDT> savedInDb = accountRepository.findByLogin("new.user");

        //Then
        assertTrue(savedInDb.isPresent(), "Konto powinno zostać zapisane w bazie danych");
        assertEquals("new.user", savedInDb.get().getLogin());
        assertEquals("Nowy", savedInDb.get().getFirstName());
        assertEquals("Użytkownik", savedInDb.get().getLastName());
        assertEquals("new@test.pl", savedInDb.get().getEmail());
    }

    @Test
    @Transactional
    void whenFindByLoginWithAddressesThenReturnAccountWithTwoAddresses() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findByLoginWithAddress("login1");

        // Then
        assertTrue(result.isPresent(), "Konto z adresami powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(1, account.getId());
        assertEquals(2, account.getAddresses().size(), "Powinny zostać znalezione dokładnie dwa adresy.");

        assertTrue(account.getAddresses().stream().anyMatch(a -> a.street().equals("street1") && a.addressType().equals("type1")));
        assertTrue(account.getAddresses().stream().anyMatch(a -> a.street().equals("street2") && a.addressType().equals("type2")));
    }

    @Test
    @Transactional
    void whenFindByLoginWithAddressesThenReturnAccountWithEmptyAddressList() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findByLoginWithAddress("login2");

        // Then
        assertTrue(result.isPresent(), "Konto powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(2, account.getId());
        assertTrue(account.getAddresses().isEmpty(), "Lista adresów powinna być pusta.");
    }

    private void createAddress(String street, String zip, String city, String type) {
        dsl.insertInto(ADDRESS)
                .set(ADDRESS.ACCOUNT_ID, 1)
                .set(ADDRESS.STREET, street)
                .set(ADDRESS.ZIP_CODE, zip)
                .set(ADDRESS.CITY, city)
                .set(ADDRESS.ADDRESS_TYPE, type)
                .execute();
    }

    private void createUser(String login, String firstName, String lastName, String email) {
        dsl.insertInto(ACCOUNT)
                .set(ACCOUNT.LOGIN, login)
                .set(ACCOUNT.FIRST_NAME, firstName)
                .set(ACCOUNT.LAST_NAME, lastName)
                .set(ACCOUNT.PASSWORD, DEFAULT_PASS)
                .set(ACCOUNT.EMAIL, email)
                .execute();
    }

    private void createRole(String roleName) {
        dsl.insertInto(ROLE)
                .set(ROLE.NAME, roleName)
                .execute();
    }

    private void addRoleToUser(Integer userId, Integer roleId) {
        dsl.insertInto(ROLE_ACCOUNT)
                .set(ROLE_ACCOUNT.ACCOUNT_ID, userId)
                .set(ROLE_ACCOUNT.ROLE_ID, roleId)
                .execute();
    }

    private void addLoginHistory(Integer userId, String ipAddress, LocalDateTime lastLogin, String isSuccess) {
        dsl.insertInto(LOGIN_HISTORY)
                .set(LOGIN_HISTORY.ACCOUNT_ID, userId)
                .set(LOGIN_HISTORY.LAST_LOGIN, lastLogin)
                .set(LOGIN_HISTORY.IP_ADDRESS, ipAddress)
                .set(LOGIN_HISTORY.IS_SUCCESS, isSuccess)
                .execute();
    }
}