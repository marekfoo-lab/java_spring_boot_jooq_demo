package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model;

import java.util.List;

public record AccountDT(Integer id,
                        String login,
                        String firstName,
                        String lastName,
                        String email,
                        List<AddressDT> addresses) {
}
