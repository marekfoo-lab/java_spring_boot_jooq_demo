package pl.mfconsulting.java.demo.quarkus_jooq.model;

public record AddressDT(
        Integer id,
        String street,
        String zipCode,
        String city,
        String addressType,
        Integer accountId
) {
}