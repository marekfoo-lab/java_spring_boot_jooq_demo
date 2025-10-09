package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.model;

public record AddressDT(
        Integer id,
        String street,
        String zipCode,
        String city,
        String addressType
) {
}