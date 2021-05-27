package com.test.modusbox;

import static com.test.modusbox.model.EntityBuilder.TRANSACTION_CONTENT;
import static com.test.modusbox.model.EntityBuilder.TRANSACTION_FECHA;
import static com.test.modusbox.model.EntityBuilder.TRANSACTION_HORA;

import java.util.UUID;

import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.model.entities.Transaction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestEntityBuilder {

    public static final String SUPPLIER_NAME = "NETFLIX";
    public static final String SUPPLIER_ADDRESS = "LIMA AVENUE 1234";
    public static final int SUPPLIER_CONTACT = 454545;

    public static Supplier.SupplierBuilder buildSupplier() {
        return Supplier.builder()
                .id(1L)
                .name(SUPPLIER_NAME)
                .address(SUPPLIER_ADDRESS)
                .contact(SUPPLIER_CONTACT);
    }

    public static Transaction.TransactionBuilder buildTransaction() {
        return Transaction.builder()
                .id(1L)
                .transactionId(UUID.randomUUID().toString())
                .content(TRANSACTION_CONTENT)
                .fecha(TRANSACTION_FECHA)
                .hora(TRANSACTION_HORA)
                .supplier(buildSupplier().build());

    }

}
