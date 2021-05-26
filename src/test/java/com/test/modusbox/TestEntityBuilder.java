package com.test.modusbox;

import com.test.modusbox.model.entities.Supplier;

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

}
