package com.test.modusbox.controller;

import static com.test.modusbox.constants.Paths.CREATE_TRANSACTION_PATH;
import static com.test.modusbox.constants.Paths.TOKEN_PATH;
import static com.test.modusbox.constants.Paths.TRANSACTION_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.test.modusbox.model.DTOBuilder;
import com.test.modusbox.model.dto.TransactionDTO;
import com.test.modusbox.repository.SupplierRepository;
import com.test.modusbox.repository.TransactionRepository;
import com.test.modusbox.service.TransactionService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TransactionControllerTest {

    private static final String TOKEN_PATH_URL = TOKEN_PATH + "/token";
    private static final String TRANSACTION_PATH_URL = TRANSACTION_PATH + CREATE_TRANSACTION_PATH;

    private final Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    public void before() {
        transactionRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        transactionRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    @Test
    @Sql({ "/db/h2/populateSuppliersTest.sql" })
    public void sqlScript_whenValid_shouldLoadRequiredTables() {
        assertThat(supplierRepository.count()).isEqualTo(3);
    }

    @Test
    @Sql({ "/db/h2/populateSuppliersTest.sql" })
    public void postTransaction_whenValidSupplierAndInternalError_shouldReturnError() throws Exception {

        TransactionDTO transactionDTO = DTOBuilder.buildTransactionDTO();
        ResponseEntity<String> resultToken =
                restTemplate.postForEntity(TOKEN_PATH_URL, transactionDTO, String.class);
        logger.info("Acquired Token String:{}\"", resultToken.getBody());

        //POSTING TRANSACTION USING THE TOKEN OBTAINED
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("token", resultToken.getBody());

        HttpEntity<Object> entityTransaction = new HttpEntity<>(transactionDTO, headers);

        //CALLING SERVICE FOR POSTING TRANSACTION
        ResponseEntity<String> result =
                restTemplate.postForEntity(TRANSACTION_PATH_URL, entityTransaction, String.class);
        logger.info("POSTING transaction: {}", transactionDTO.getTransactionId() + " -" + result.getBody());

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).contains("Internal Server Error");

    }

    @Test
    @Sql({ "/db/h2/populateSuppliersTest.sql" })
    public void postTransaction_whenNotValidSupplierAndInternalError_shouldReturnError() throws Exception {

        TransactionDTO transactionDTO = DTOBuilder.buildTransactionDTO();
        transactionDTO.getSupplier().setId(999);

        ResponseEntity<String> resultToken =
                restTemplate.postForEntity(TOKEN_PATH_URL, transactionDTO, String.class);
        logger.info("Acquired Token String:{}\"", resultToken.getBody());

        //POSTING TRANSACTION USING THE TOKEN OBTAINED
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("token", resultToken.getBody());
        HttpEntity<Object> entityTransaction = new HttpEntity<>(transactionDTO, headers);

        //CALLING SERVICE FOR POSTING TRANSACTION
        ResponseEntity<String> result =
                restTemplate.postForEntity(TRANSACTION_PATH_URL, entityTransaction, String.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR).isEqualTo(result.getStatusCode());
        assertThat(result.getBody()).contains("Internal Server Error");

    }
}
