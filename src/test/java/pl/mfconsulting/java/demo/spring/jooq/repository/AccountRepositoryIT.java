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
        Optional<AccountDT> result = accountRepository.findById(1);

        // Then
        assertTrue(result.isPresent(), "Account should be found.");
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
        assertTrue(result.isPresent(), "Account should be found.");
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
        assertEquals(2, accounts.size(), "2 Accounts should be found.");
        assertTrue(accounts.stream().anyMatch(a -> a.login().equals("login1")));
        assertTrue(accounts.stream().anyMatch(a -> a.login().equals("login2")));
    }

    @Test
    @Transactional
    void whenCreateAccountThenInsertNewAccountAndReturnGeneratedId() {
        // Given
        AccountDT newAccount = new AccountDT(3, "new.user", "New11", "User11", "new@test.pl", List.of());

        // When
        Optional<Integer> createdId = accountRepository.create(newAccount);

        // Then
        assertTrue(createdId.isPresent(), "New Account should have id generated.");
        assertTrue(createdId.get() > 0, "ID should be greater than 0");

        // When
        Optional<AccountDT> savedInDb = accountRepository.findByLogin("new.user");

        //Then
        assertTrue(savedInDb.isPresent(), "Account should be saved in db");
        assertEquals("new.user", savedInDb.get().login());
        assertEquals("New11", savedInDb.get().firstName());
        assertEquals("User11", savedInDb.get().lastName());
        assertEquals("new@test.pl", savedInDb.get().email());
    }

    @Test
    @Transactional
    void whenFindByLoginWithAddressesThenReturnAccountWithTwoAddresses() {
        // Given

        // When
        Optional<AccountDT> result = accountRepository.findByLoginWithAddress("login1");

        // Then
        assertTrue(result.isPresent(), "Account should be found.");
        AccountDT account = result.get();
        assertEquals(1, account.id());
        assertEquals(2, account.addresses().size(), "Only 2 addresses should be found");

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
        assertTrue(result.isPresent(), "Account should be found.");
        AccountDT account = result.get();
        assertEquals(2, account.id());
        assertTrue(account.addresses().isEmpty(), "List of addresses should be empty");
    }
}