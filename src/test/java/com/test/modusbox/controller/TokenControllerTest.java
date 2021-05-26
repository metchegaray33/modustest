package com.test.modusbox.controller;

import static com.test.modusbox.constants.Paths.TOKEN_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.test.modusbox.model.DTOBuilder;
import com.test.modusbox.model.dto.TransactionDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TokenControllerTest {

    private static final String TOKEN_PATH_URL = TOKEN_PATH + "/token";
    private final Logger logger = LoggerFactory.getLogger(TokenControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void interfaceIdempotenceTest() throws Exception {
        TransactionDTO transactionDTO = DTOBuilder.buildTransactionDTO();

        ResponseEntity<String> resultToken =
                restTemplate.postForEntity(TOKEN_PATH_URL, transactionDTO, String.class);
        logger.info("Acquired Token String:{}\"", resultToken.getBody());

        //POSTING TRANSACTION USING THE TOKEN OBTAINED
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("token", resultToken.getBody());

        HttpEntity<Object> entityTransaction = new HttpEntity<>(transactionDTO, headers);

        for (int i = 1; i <= 5; i++) {
            //CALLING SERVICE FOR POSTING TRANSACTION
            ResponseEntity<String> result =
                    restTemplate.postForEntity(TOKEN_PATH + "/test", entityTransaction, String.class);
            logger.info("POSTING transaction: {}", transactionDTO.getTransactionId() + " -" + result.getBody());

            //  The result asserts
            if (i == 1) {
                assertThat(result.getBody()).isEqualTo("Normal call");
            } else {
                assertThat(result.getBody()).isEqualTo("Repeat call");
            }

        }

    }

}
