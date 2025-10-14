package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model;

import lombok.Getter;

import java.util.List;

@Getter
public class AccountDT {
    private Integer id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressDT> addresses;

    public AccountDT(Integer id,
                     String login,
                     String firstName,
                     String lastName,
                     String email,
                     List<AddressDT> addresses) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addresses = addresses;
    }

    public AccountDT(Integer id,
                     String login,
                     String firstName,
                     String lastName,
                     String email) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addresses = List.of();
    }
}
