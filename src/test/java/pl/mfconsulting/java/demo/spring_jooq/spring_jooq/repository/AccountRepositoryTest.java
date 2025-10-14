package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.repository;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.SpringJooqApplication;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model.AccountDT;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pl.mfconsulting.java.demo.spring_jooq.spring_jooq.generated.Tables.ACCOUNT;
import static pl.mfconsulting.java.demo.spring_jooq.spring_jooq.generated.Tables.ADDRESS;

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
        dsl.deleteFrom(ADDRESS).execute();
        dsl.deleteFrom(ACCOUNT).execute();

        dsl.execute("ALTER SEQUENCE account_id_seq RESTART WITH 1");
        dsl.execute("ALTER SEQUENCE ADDRESS_ID_SEQ RESTART WITH 1");

        createUser("login1", "name1", "surname1", "mail1");
        createUser("login2", "name2", "surname2", "mail2");

        createAddress("street1", "zip1", "city1", "type1");
        createAddress("street2", "zip2", "city2", "type2");

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
        var user = dsl.insertInto(ACCOUNT)
                .set(ACCOUNT.LOGIN, login)
                .set(ACCOUNT.FIRST_NAME, firstName)
                .set(ACCOUNT.LAST_NAME, lastName)
                .set(ACCOUNT.PASSWORD, DEFAULT_PASS)
                .set(ACCOUNT.EMAIL, email)
                .execute();

        int a = 0;
    }

    @Test
    @Transactional
    void findById_ShouldReturnAccountDetails() {
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
    void findAll_ShouldReturnTwoAccounts() {
        // When
        List<AccountDT> accounts = accountRepository.findAll();

        // Then
        assertEquals(2, accounts.size(), "Powinny zostać znalezione dwa konta.");
        assertTrue(accounts.stream().anyMatch(a -> a.getLogin().equals("login1")));
        assertTrue(accounts.stream().anyMatch(a -> a.getLogin().equals("login2")));
    }

    @Test
    @Transactional
    void createAccount_ShouldInsertNewAccountAndReturnGeneratedId() {
        // Given
        AccountDT newAccount = new AccountDT(3, "new.user", "Nowy", "Użytkownik", "new@test.pl", List.of());

        // When
        AccountDT created = accountRepository.create(newAccount);

        // Then
        assertNotNull(created.getId(), "Nowe konto powinno mieć wygenerowane ID.");
        assertEquals("new.user", created.getLogin());

        Optional<AccountDT> savedInDb = accountRepository.findByLogin(created.getLogin());
        assertTrue(savedInDb.isPresent());
        assertEquals(3, savedInDb.get().getId());
    }

    @Test
    @Transactional
    void findByIdWithAddresses_ShouldReturnAccountWithTwoAddresses() {
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
    void findByIdWithAddresses_ShouldReturnAccountWithEmptyAddressList() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findByLoginWithAddress("login2");

        // Then
        assertTrue(result.isPresent(), "Konto powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(2, account.getId());
        assertTrue(account.getAddresses().isEmpty(), "Lista adresów powinna być pusta.");
    }
}