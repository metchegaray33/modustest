package com.test.modusbox.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.model.entities.Transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityBuilder {

    public static final LocalDate TRANSACTION_FECHA = LocalDate.parse("2020-06-01");
    public static final LocalTime TRANSACTION_HORA = LocalTime.parse("09:00:00");
    public static final String TRANSACTION_CONTENT = "Transaction content up 64 kb missing letters";

    public static final String SUPPLIER_NAME = "Juan";
    public static final String SUPPLIER_ADDRESS = "Juan B . Justo Street";
    public static final int SUPPLIER_CONTACT = 1;

    public static Transaction buildTransaction() {
        return Transaction.builder()
                .id(1L)
                .transactionId(UUID.randomUUID().toString())
                .content(TRANSACTION_CONTENT)
                .fecha(TRANSACTION_FECHA)
                .hora(TRANSACTION_HORA)
                .supplier(buildSupplier()).build();

    }

    public static Supplier buildSupplier() {
        return Supplier.builder()
                .id(1L)
                .name(SUPPLIER_NAME)
                .address(SUPPLIER_ADDRESS)
                .contact(SUPPLIER_CONTACT).build();
    }

}
