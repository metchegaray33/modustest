package com.test.modusbox;

import static com.test.modusbox.TestEntityBuilder.SUPPLIER_ADDRESS;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_CONTACT;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_NAME;

import com.test.modusbox.model.dto.SupplierDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDTOBuilder {

    public static SupplierDTO.SupplierDTOBuilder buildSupplierDTO() {
        return SupplierDTO.builder()
                .id(1L)
                .name(SUPPLIER_NAME)
                .address(SUPPLIER_ADDRESS)
                .contact(SUPPLIER_CONTACT);
    }

}
