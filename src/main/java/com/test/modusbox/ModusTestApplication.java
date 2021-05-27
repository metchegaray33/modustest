package com.test.modusbox;

import static com.test.modusbox.constants.Paths.BASE_URL;
import static com.test.modusbox.constants.Paths.CREATE_TRANSACTION_PATH;
import static com.test.modusbox.constants.Paths.TOKEN_PATH;
import static com.test.modusbox.constants.Paths.TRANSACTION_PATH;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.test.modusbox.model.DTOBuilder;
import com.test.modusbox.model.dto.TransactionDTO;

@SpringBootApplication
@EnableRetry
public class ModusTestApplication {

    private static final String TOKEN_PATH_URL = BASE_URL + TOKEN_PATH + "/token";
    private static final String TRANSACTION_PATH_URL = BASE_URL + TRANSACTION_PATH + CREATE_TRANSACTION_PATH;

    private final Logger logger = LoggerFactory.getLogger(ModusTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ModusTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner run(RestTemplate restTemplate) {
        return (args) -> {
            List<TransactionDTO> transactionDTOList = buildTransactionListDTO(12);
            ListUtils.emptyIfNull(transactionDTOList).forEach(transactionDTO -> {
                processTransaction(transactionDTO, restTemplate);
            });

        };
    }

    private void processTransaction(TransactionDTO transactionDTO, RestTemplate restTemplate) {
        //CALLING SERVICE FOR GETTING TRANSACTION TOKEN
        try {
            ResponseEntity<String> resultToken =
                    restTemplate.postForEntity(TOKEN_PATH_URL, transactionDTO, String.class);
            logger.info("POSTING token: {}", resultToken.getBody());

            //POSTING TRANSACTION USING THE TOKEN OBTAINED
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");
            headers.add("token", resultToken.getBody());
            HttpEntity<Object> entityTransaction = new HttpEntity<>(transactionDTO, headers);

            //CALLING SERVICE FOR POSTING TRANSACTION
            ResponseEntity<String> result =
                    restTemplate.postForEntity(TRANSACTION_PATH_URL, entityTransaction, String.class);

            logger.info("POSTING transaction: {}", transactionDTO.getTransactionId() + " -" + result.getBody());

        } catch (Exception ex) {
            logger.info("ERROR POSTING transaction: {}", transactionDTO.getTransactionId());
        }

    }

    private List<TransactionDTO> buildTransactionListDTO(int maxSize) {
        List<TransactionDTO> transactionDTOList = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            transactionDTOList.add(DTOBuilder.buildTransactionDTO());
        }
        return transactionDTOList;
    }
}
