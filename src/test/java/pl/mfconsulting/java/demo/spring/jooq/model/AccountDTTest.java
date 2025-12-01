package pl.mfconsulting.java.demo.spring.jooq.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccountDTTest {
    @Test
    void whenEqualsThenOK() {
        var account1 = new AccountDT(1, "login1", "first1", "last1", "email1", List.of());
        var account2 = new AccountDT(1, "login1", "first1", "last1", "email1", List.of());

        assertEquals(account1, account2);
    }

    @Test
    void whenNoEqualsThenOK() {
        var account1 = new AccountDT(1, "login1", "first1", "last1", "email1", List.of());
        var account2 = new AccountDT(2, "login1", "first1", "last1", "email1", List.of());

        assertNotEquals(account1, account2);
    }
}
