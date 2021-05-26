package com.test.modusbox.service;

import static com.test.modusbox.TestDTOBuilder.buildSupplierDTO;
import static com.test.modusbox.TestEntityBuilder.buildSupplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import com.test.modusbox.model.dto.SupplierDTO;
import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    private static final String ERR_MSG = "Supplier with id ";
    private static final String NOT_FOUND = " no encontrado.";

    @Mock
    private SupplierRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupplierService service;

    private Supplier supplier;

    @Test
    public void addSupplier_shouldSaveSupplier() {
        supplier = buildSupplier().build();
        SupplierDTO supplierDTO = buildSupplierDTO().build();
        when(modelMapper.map(supplierDTO, Supplier.class)).thenReturn(supplier);
        when(repository.save(supplier)).thenReturn(supplier);

        service.addSupplier(supplierDTO);

        verify(repository).save(supplier);
    }

    @Test
    public void deleteSupplier_whenSupplierExists_shouldDeleteSupplier() {
        supplier = buildSupplier().build();
        when(repository.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        service.deleteSupplier(supplier.getId());

        verify(repository).findById(supplier.getId());
        verify(repository).delete(supplier);
    }

    @Test
    public void deleteSupplier_whenSupplierDoesNotExists_shouldThrowException() {
        long idSupplier = 1L;
        when(repository.findById(idSupplier)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteSupplier(idSupplier))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(ERR_MSG + idSupplier + NOT_FOUND);
    }

    @Test
    public void findAllSuppliers_shouldReturnSuppliersList() {
        supplier = buildSupplier().build();
        when(repository.findAll()).thenReturn(Collections.singletonList(supplier));

        List<Supplier> suppliers = service.findAllSuppliers();

        assertThat(suppliers).isNotNull();
        assertThat(suppliers.size()).isEqualTo(1);
        assertThat(suppliers).contains(supplier);
    }

    @Test
    public void findSupplierById_whenIdExists_shouldReturnSupplier() {
        supplier = buildSupplier().build();
        when(repository.findById(supplier.getId())).thenReturn(Optional.of(supplier));

        service.findSupplierById(supplier.getId());

        verify(repository).findById(supplier.getId());
    }

    @Test
    public void findSupplierById_whenIdDoesNotExists_shouldThrowResponseStatusException() {
        supplier = buildSupplier().build();
        when(repository.findById(supplier.getId())).thenReturn(Optional.empty());

        long idSupplier=supplier.getId();

        assertThatThrownBy(() -> service.findSupplierById(idSupplier))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(ERR_MSG + idSupplier + NOT_FOUND);

    }

    @Test
    public void updateSupplier_whenSupplierExist_shouldUpdateSupplier() {
        supplier = buildSupplier().build();
        SupplierDTO supplierDTO = buildSupplierDTO().build();
        when(repository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        when(repository.save(supplier)).thenReturn(supplier);

        service.updateSupplier(supplierDTO);

        verify(repository).findById(supplier.getId());
        verify(repository).save(supplier);

    }

    @Test
    public void updateSupplier_whenSupplierDoesNotExist_shouldThrowException() {
        supplier = buildSupplier().build();
        SupplierDTO supplierDTO = buildSupplierDTO().build();
        when(repository.findById(supplier.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateSupplier(supplierDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404 NOT_FOUND", ERR_MSG + supplierDTO.getId() + NOT_FOUND);


        verify(repository).findById(supplier.getId());

    }


}
