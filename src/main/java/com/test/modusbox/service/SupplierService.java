package com.test.modusbox.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.test.modusbox.model.dto.SupplierDTO;
import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.repository.SupplierRepository;

@Service
public class SupplierService {

    private final Logger logger = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository repository;
    private final ModelMapper modelMapper;
    private static final String ERR_MSG = "ERROR: deleteSupplier -> Supplier con id";
    private static final String NOT_FOUND = " no encontrado.";

    public SupplierService(SupplierRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Supplier addSupplier(SupplierDTO supplierDTO) {
        return repository.save(modelMapper.map(supplierDTO, Supplier.class));
    }

    public void deleteSupplier(long supplierId) {
        Optional<Supplier> supplier = repository.findById(supplierId);
        if (supplier.isPresent()) {
            repository.delete(supplier.get());
        } else {
            logger.error(ERR_MSG, supplierId, NOT_FOUND);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier with id " + supplierId + NOT_FOUND);
        }
    }

    public List<Supplier> findAllSuppliers() {
        return repository.findAll();
    }

    public Supplier findSupplierById(long supplierId) {
        return repository.findById(supplierId).orElseThrow(() -> {
            logger.error("ERROR: findSupplierById -> Supplier with id {} no found", supplierId);
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Supplier with id " + supplierId + NOT_FOUND);
        });
    }

    public Supplier updateSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = repository.findById(supplierDTO.getId()).orElseThrow(() -> {
            logger.error("ERROR: updateSupplier -> id not found {}", supplierDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier con id " + supplierDTO.getId() + NOT_FOUND);
        });

        supplier.setName(supplierDTO.getName());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setContact(supplierDTO.getContact());
        return repository.save(supplier);
    }

}
