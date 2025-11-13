package pl.mfconsulting.java.demo.quarkus.jooq.repository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.mfconsulting.java.demo.quarkus.jooq.configuration.CustomDLS;
import pl.mfconsulting.java.demo.quarkus.jooq.model.AccountDT;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.mfconsulting.java.demo.quarkus.jooq.generated.Tables.ACCOUNT;
import static pl.mfconsulting.java.demo.quarkus.jooq.generated.Tables.ADDRESS;

@QuarkusTest
@Slf4j
class AccountRepositoryIntegrationTest {
    private static final String DEFAULT_PASS = "test_pass";

    @Inject
    private AccountRepository accountRepository;

    @Inject
    @CustomDLS
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

    @Test
    @Transactional
    void whenFindByIdThenReturnUser() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findById(Integer.valueOf(1));

        // Then
        assertTrue(result.isPresent(), "Konto powinno zostać znalezione.");
        AccountDT account = result.get();
        assertEquals(1, account.id());
        assertEquals("name1", account.firstName());
        assertEquals("login1", account.login());

        assertTrue(account.addresses().isEmpty());
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
        assertEquals(1, account.id());
        assertEquals("name1", account.firstName());
        assertEquals("login1", account.login());

        assertTrue(account.addresses().isEmpty());
    }

    @Test
    @Transactional
    void whenFindAllThenReturnTwoRecords() {
        // When
        List<AccountDT> accounts = accountRepository.findAll();

        // Then
        assertEquals(2, accounts.size(), "Powinny zostać znalezione dwa konta.");
        assertTrue(accounts.stream().anyMatch(a -> a.login().equals("login1")));
        assertTrue(accounts.stream().anyMatch(a -> a.login().equals("login2")));
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
        assertEquals("new.user", savedInDb.get().login());
        assertEquals("Nowy", savedInDb.get().firstName());
        assertEquals("Użytkownik", savedInDb.get().lastName());
        assertEquals("new@test.pl", savedInDb.get().email());
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
        assertEquals(1, account.id());
        assertEquals(2, account.addresses().size(), "Powinny zostać znalezione dokładnie dwa adresy.");

        assertTrue(account.addresses().stream().anyMatch(a -> a.street().equals("street1") && a.addressType().equals("type1")));
        assertTrue(account.addresses().stream().anyMatch(a -> a.street().equals("street2") && a.addressType().equals("type2")));
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
        assertEquals(2, account.id());
        assertTrue(account.addresses().isEmpty(), "Lista adresów powinna być pusta.");
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
}