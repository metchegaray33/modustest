package com.test.modusbox;

import static com.test.modusbox.TestEntityBuilder.SUPPLIER_ADDRESS;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_CONTACT;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_NAME;
import static com.test.modusbox.model.EntityBuilder.TRANSACTION_CONTENT;
import static com.test.modusbox.model.EntityBuilder.TRANSACTION_FECHA;
import static com.test.modusbox.model.EntityBuilder.TRANSACTION_HORA;
import static com.test.modusbox.model.EntityBuilder.buildSupplier;

import java.util.UUID;

import com.test.modusbox.model.dto.SupplierDTO;
import com.test.modusbox.model.dto.TransactionDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDTOBuilder {

    public static TransactionDTO.TransactionDTOBuilder buildTransactionDTO() {
        return TransactionDTO.builder()
                .transactionId(UUID.randomUUID().toString())
                .content(TRANSACTION_CONTENT)
                .fecha(TRANSACTION_FECHA)
                .hora(TRANSACTION_HORA)
                .supplier(buildSupplier());
    }

    public static SupplierDTO.SupplierDTOBuilder buildSupplierDTO() {
        return SupplierDTO.builder()
                .id(1L)
                .name(SUPPLIER_NAME)
                .address(SUPPLIER_ADDRESS)
                .contact(SUPPLIER_CONTACT);
    }

}
