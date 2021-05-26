package com.test.modusbox.controller;

import static com.test.modusbox.constants.Paths.CREATE_SUPPLIER_PATH;
import static com.test.modusbox.constants.Paths.DELETE_SUPPLIER_PATH;
import static com.test.modusbox.constants.Paths.FIND_ALL_SUPPLIERS_PATH;
import static com.test.modusbox.constants.Paths.FIND_SUPPLIER_BY_ID;
import static com.test.modusbox.constants.Paths.SUPPLIERS_PATH;
import static com.test.modusbox.constants.Paths.UPDATE_SUPPLIER_PATH;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.modusbox.model.dto.SupplierDTO;
import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.service.SupplierService;

@RestController
@RequestMapping(SUPPLIERS_PATH)
public class SupplierController {

    private final Logger logger = LoggerFactory.getLogger(SupplierController.class);
    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PostMapping(CREATE_SUPPLIER_PATH)
    public ResponseEntity<Supplier> addSupplier(@RequestBody SupplierDTO supplierDTO) {
        logger.debug("POST request addFeriado: {}", supplierDTO);
        return new ResponseEntity<>(service.addSupplier(supplierDTO), HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE_SUPPLIER_PATH + "/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable long supplierId) {
        logger.debug("DELETE request deleteFeriado: {}", supplierId);
        service.deleteSupplier(supplierId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(FIND_ALL_SUPPLIERS_PATH)
    public ResponseEntity<List<Supplier>> findAllSuppliers() {
        logger.debug("GET request findAllSuppliers");
        return new ResponseEntity<>(service.findAllSuppliers(), HttpStatus.OK);
    }

    @GetMapping(FIND_SUPPLIER_BY_ID)
    public ResponseEntity<Supplier> getSupplierById(@RequestParam long supplierId) {
        logger.debug("GET request getSupplierById: {}", supplierId);
        return new ResponseEntity<>(service.findSupplierById(supplierId), HttpStatus.OK);
    }

    @PutMapping(UPDATE_SUPPLIER_PATH)
    public ResponseEntity<Void> updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        logger.debug("PUT request updateSupplier: {}", supplierDTO);
        service.updateSupplier(supplierDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
