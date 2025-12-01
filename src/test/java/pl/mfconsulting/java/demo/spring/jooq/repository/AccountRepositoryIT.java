package pl.mfconsulting.java.demo.spring.jooq.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.mfconsulting.java.demo.spring.jooq.SpringJooqApplication;
import pl.mfconsulting.java.demo.spring.jooq.configuration.ConfigurationOfIntegration;
import pl.mfconsulting.java.demo.spring.jooq.model.AccountDT;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringJooqApplication.class)
@ActiveProfiles("test")
@Slf4j
class AccountRepositoryIT extends ConfigurationOfIntegration {
    @Autowired
    private AccountRepository accountRepository;

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
}