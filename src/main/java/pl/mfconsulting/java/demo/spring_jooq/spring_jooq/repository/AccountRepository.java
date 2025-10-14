package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.repository;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model.AccountDT;
import pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model.AddressDT;

import java.util.List;
import java.util.Optional;

import static pl.mfconsulting.java.demo.spring_jooq.spring_jooq.generated.Tables.ACCOUNT;
import static pl.mfconsulting.java.demo.spring_jooq.spring_jooq.generated.Tables.ADDRESS;

@Repository
public class AccountRepository {
    private final DSLContext context;

    public AccountRepository(DSLContext context) {
        this.context = context;
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
    public AccountDT create(AccountDT newAccount) {
        Record1<Long> sequenceResult = context.select(context.nextval(context.nextval("account_id_seq")))
                .fetchOne();
        return context.insertInto(ACCOUNT)
                .set(ACCOUNT.ID, seq)
                .set(ACCOUNT.LOGIN, newAccount.getLogin())
                .set(ACCOUNT.FIRST_NAME, newAccount.getFirstName())
                .set(ACCOUNT.LAST_NAME, newAccount.getLastName())
                .set(ACCOUNT.EMAIL, newAccount.getEmail())
                .returning(ACCOUNT.ID, ACCOUNT.LOGIN, ACCOUNT.FIRST_NAME, ACCOUNT.LAST_NAME, ACCOUNT.EMAIL)
                .fetchOne()
                .map(this::fillAccount);
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
                    var accountRecord = records.getFirst().into(ACCOUNT);

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
