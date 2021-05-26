package com.test.modusbox.controller;

import static com.test.modusbox.TestDTOBuilder.buildSupplierDTO;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_ADDRESS;
import static com.test.modusbox.TestEntityBuilder.SUPPLIER_NAME;
import static com.test.modusbox.TestEntityBuilder.buildSupplier;
import static com.test.modusbox.constants.Paths.CREATE_SUPPLIER_PATH;
import static com.test.modusbox.constants.Paths.SUPPLIERS_PATH;
import static com.test.modusbox.constants.Paths.UPDATE_SUPPLIER_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.test.modusbox.constants.Paths;
import com.test.modusbox.model.dto.SupplierDTO;
import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.repository.SupplierRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class SupplierControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SupplierRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void postSupplier_whenValid_shouldReturnCreatedStatusCode() {
        SupplierDTO supplierDTO = buildSupplierDTO().build();
        ResponseEntity<Supplier> response
                = restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplierDTO, Supplier.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void postSupplier_whenValid_shouldReturnSupplierBody() {
        Supplier supplier = buildSupplier().build();
        ResponseEntity<Supplier> response
                = restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplier, Supplier.class);

        Supplier returnedSupplier = response.getBody();

        assertThat(returnedSupplier).isNotNull();
        assertThat(supplier.getAddress()).isEqualTo(returnedSupplier.getAddress());
        assertThat(supplier.getName()).isEqualTo(returnedSupplier.getName());
    }

    @Test
    public void postSupplier_whenValidFecha_shouldSaveToDB() {
        Supplier supplier = buildSupplier().build();
        restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplier, Supplier.class);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    public void deleteSupplier_whenSupplierExists_shouldReturnOk() {
        Supplier supplier = buildSupplier().build();
        ResponseEntity<Supplier> response
                = restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplier, Supplier.class);

        long supplierId = response.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(SUPPLIERS_PATH + Paths.DELETE_SUPPLIER_PATH + "/" + supplierId,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteSupplier_whenSupplierDoesNotExists_shouldReturnNotFound() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(SUPPLIERS_PATH + Paths.DELETE_SUPPLIER_PATH + "/1",
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAllSuppliers_whenValidRequest_shouldReturnOk() {
        ResponseEntity<Supplier[]> response
                = restTemplate.getForEntity(SUPPLIERS_PATH + Paths.FIND_ALL_SUPPLIERS_PATH, Supplier[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAllSuppliers_whenValidRequest_shouldReturnSupplierListBody() {
        Supplier supplier = buildSupplier().build();

        restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplier, Supplier.class);
        ResponseEntity<Supplier[]> response
                = restTemplate.getForEntity(SUPPLIERS_PATH + Paths.FIND_ALL_SUPPLIERS_PATH, Supplier[].class);

        Supplier[] suppliers = response.getBody();
        assertThat(suppliers).isNotNull();
        assertThat(suppliers.length).isEqualTo(1);
        assertThat(suppliers[0].getName()).isEqualTo(SUPPLIER_NAME);
        assertThat(suppliers[0].getAddress()).isEqualTo(SUPPLIER_ADDRESS);
    }

    @Test
    public void putSucursal_whenSucursalExists_shouldUpdateSucursal() {
        Supplier supplier = buildSupplier().build();
        restTemplate.postForEntity(SUPPLIERS_PATH + CREATE_SUPPLIER_PATH, supplier, Object.class);
        Supplier supplierPreChange = repository.findAll().get(0);

        String expectedName = "Nombre Test";
        supplierPreChange.setName(expectedName);

        restTemplate.exchange(SUPPLIERS_PATH + UPDATE_SUPPLIER_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(supplierPreChange),
                Void.class);

        Optional<Supplier> supplierPostChange = repository.findById(supplierPreChange.getId());

        assertThat(expectedName).isEqualTo(supplierPostChange.get().getName());
    }

    @Test
    public void putSupplier_whenSupplieroesNotExists_shouldReturnNotFound() {
        Supplier supplier = buildSupplier().build();
        ResponseEntity<Void> updateResponse = restTemplate.exchange(SUPPLIERS_PATH + UPDATE_SUPPLIER_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(supplier),
                Void.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
