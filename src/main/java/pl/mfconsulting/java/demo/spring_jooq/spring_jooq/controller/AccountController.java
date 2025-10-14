package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model.AccountDT;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.repository.AccountRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<AccountDT> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDT> getAccountByLogin(@PathVariable String login) {
        return accountRepository.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountDT> createAccount(@RequestBody AccountDT newAccount) {
        var createdId = accountRepository.create(newAccount);
        return ResponseEntity.created(URI.create("/api/accounts/" + createdId)).build();
    }
}
