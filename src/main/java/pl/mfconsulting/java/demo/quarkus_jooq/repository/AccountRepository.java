package pl.mfconsulting.java.demo.quarkus_jooq.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record5;
import pl.mfconsulting.java.demo.quarkus_jooq.configuration.CustomDLS;
import pl.mfconsulting.java.demo.quarkus_jooq.model.AccountDT;
import pl.mfconsulting.java.demo.quarkus_jooq.model.AddressDT;

import java.util.List;
import java.util.Optional;

import static pl.mfconsulting.java.demo.quarkus_jooq.generated.Tables.ACCOUNT;
import static pl.mfconsulting.java.demo.quarkus_jooq.generated.Tables.ADDRESS;

@ApplicationScoped
public class AccountRepository {

    @CustomDLS
    @Inject
    private DSLContext context;

    public Optional<AccountDT> findById(Integer id) {
        return context.select(ACCOUNT.ID, ACCOUNT.LOGIN, ACCOUNT.FIRST_NAME, ACCOUNT.LAST_NAME, ACCOUNT.EMAIL)
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOptional()
                .map(this::fillAccount);
    }

    public Optional<AccountDT> findByLogin(String login) {
        return context.select(ACCOUNT.ID, ACCOUNT.LOGIN, ACCOUNT.FIRST_NAME, ACCOUNT.LAST_NAME, ACCOUNT.EMAIL)
                .from(ACCOUNT)
                .where(ACCOUNT.LOGIN.eq(login))
                .fetchOptional()
                .map(this::fillAccount);
    }

    public List<AccountDT> findAll() {
        return context.select(ACCOUNT.ID, ACCOUNT.LOGIN, ACCOUNT.FIRST_NAME, ACCOUNT.LAST_NAME, ACCOUNT.EMAIL)
                .from(ACCOUNT)
                .fetchInto(AccountDT.class);
    }

    @Transactional
    public Optional<Integer> create(AccountDT newAccount) {
        // Use database-agnostic approach that works with both H2 and PostgreSQL
        // First insert the record
        int affectedRows = context.insertInto(ACCOUNT)
                .set(ACCOUNT.LOGIN, newAccount.login())
                .set(ACCOUNT.FIRST_NAME, newAccount.firstName())
                .set(ACCOUNT.LAST_NAME, newAccount.lastName())
                .set(ACCOUNT.EMAIL, newAccount.email())
                .set(ACCOUNT.PASSWORD, "default_password") // Set a default password since it's required
                .execute();

        if (affectedRows > 0) {
            var user = findByLogin(newAccount.login());
            return user.map(AccountDT::id);
        }

        return Optional.empty();
    }

    public Optional<AccountDT> findByLoginWithAddress(String login) {
        return context.select()
                .from(ACCOUNT)
                .leftJoin(ADDRESS).on(ADDRESS.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.LOGIN.eq(login))
                .fetchGroups(ACCOUNT.ID)
                .values()
                .stream()
                .findFirst()
                .map(records -> {
                    //get Account
                    var accountRecord = records.get(0).into(ACCOUNT);

                    List<AddressDT> addresses = records.stream()
                            //Filter only existing addresses for given account
                            .filter(r -> r.get(ADDRESS.ID) != null)
                            //Map first on Address Record and then on POJO Address
                            .map(r -> r.into(ADDRESS).into(AddressDT.class))
                            .toList();
                    return fillAccountWithAddress(accountRecord, addresses);
                });
    }

    private AccountDT fillAccountWithAddress(Record record, List<AddressDT> addresses) {
        return new AccountDT(record.get(ACCOUNT.ID),
                record.get(ACCOUNT.LOGIN),
                record.get(ACCOUNT.FIRST_NAME),
                record.get(ACCOUNT.LAST_NAME),
                record.get(ACCOUNT.EMAIL),
                addresses);
    }

    private AccountDT fillAccount(Record record) {
        return new AccountDT(record.get(ACCOUNT.ID),
                record.get(ACCOUNT.LOGIN),
                record.get(ACCOUNT.FIRST_NAME),
                record.get(ACCOUNT.LAST_NAME),
                record.get(ACCOUNT.EMAIL),
                List.of()
        );
    }

    private AccountDT fillAccount(Record5<Integer, String, String, String, String> record) {
        return new AccountDT(record.get(ACCOUNT.ID),
                record.get(ACCOUNT.LOGIN),
                record.get(ACCOUNT.FIRST_NAME),
                record.get(ACCOUNT.LAST_NAME),
                record.get(ACCOUNT.EMAIL),
                List.of());
    }
}
