package com.test.modusbox.service;

import static com.test.modusbox.TestDTOBuilder.buildTransactionDTO;
import static com.test.modusbox.TestEntityBuilder.buildTransaction;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.test.modusbox.model.dto.TransactionDTO;
import com.test.modusbox.model.entities.Transaction;
import com.test.modusbox.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final String TOKEN = "Token";

    @Mock
    private TransactionRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SupplierService supplierService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    TokenService tokenService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void processTransaction_whenServiceDoesNotWorksFine_shouldThrowException() {
        Transaction transaction = buildTransaction().build();
        TransactionDTO transactionDTO = buildTransactionDTO().build();

        when(modelMapper.map(transactionDTO, Transaction.class)).thenReturn(transaction);
        when(repository.save(transaction)).thenReturn(transaction);

        when(tokenService.validToken(TOKEN, transaction.getTransactionId())).thenReturn(true);

        assertThatThrownBy(() -> transactionService.processTransaction(transactionDTO, TOKEN))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("500 INTERNAL_SERVER_ERROR");

    }
}
