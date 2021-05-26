package com.test.modusbox.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.modusbox.model.dto.TransactionDTO;
import com.test.modusbox.model.entities.Supplier;
import com.test.modusbox.model.entities.Transaction;
import com.test.modusbox.repository.TransactionRepository;

@Service
public class TransactionService {

    private static final String TRANSACTION_SERVICE_URL = "http://tracker.local/supplier-transactions";

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository repository;
    private final ModelMapper modelMapper;
    private final SupplierService supplierService;

    @Autowired RestTemplateBuilder restTemplateBuilder;
    @Autowired RestTemplate restTemplate;
    @Autowired TokenUtilService tokenService;

    public TransactionService(TransactionRepository repository, ModelMapper modelMapper, SupplierService supplierService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.supplierService = supplierService;
    }

    @Retryable(value = { ResponseStatusException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String processTransaction(TransactionDTO transactionDTO, String token) throws ResponseStatusException {

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        try{
            supplierService.findSupplierById(transaction.getSupplier().getId()); // this throw an exception if supplier does not exist or it is null
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }


        boolean result = tokenService.validToken(token, transaction.getTransactionId());
        if (result) {//create a new one
            logger.info("Normal call transaction: {}", transaction.getTransactionId());
            repository.save(transaction);
            invokeTransactionService(transaction, true);
            return "Normal call"; // THIS IS ONLY FOR TEST TO BE EXPLICIT THE NORMAL O REPEAT CALL. Out of test I should return a void in method
        } else {//update existing one
            logger.info("Retrying call transaction: {}", transaction.getTransactionId());
            Optional<Transaction> transaction1 = repository.findTopByTransactionId(transaction.getTransactionId());
            if (transaction1.isPresent()) {
                repository.save(transaction1.get());
               invokeTransactionService(transaction1.get(), false);
            }
            return "Retry call"; // THIS IS ONLY FOR TEST TO BE EXPLICIT THE NORMAL O RETRY CALL. Out of test I should return a void in method

        }

    }


    public String invokeTransactionService(Transaction transaction, boolean create) throws ResponseStatusException {
        ObjectMapper mapper = new ObjectMapper();
        String statusCode = HttpStatus.ACCEPTED.getReasonPhrase();
        try {
            HttpEntity<Transaction> request = new HttpEntity<>(transaction);
            logger.debug("Request body for transaction service: {}", mapper.writeValueAsString(request));
            ResponseEntity<Transaction> response;
            if (create) {
                response = restTemplate.exchange(TRANSACTION_SERVICE_URL, HttpMethod.POST, request, Transaction.class);

            } else {
                response = restTemplate.exchange(TRANSACTION_SERVICE_URL, HttpMethod.PUT, request, Transaction.class);

            }

            logger.debug("Response from transaction service: {}", mapper.writeValueAsString(response));

            if (!response.getStatusCode().getReasonPhrase().equals(HttpStatus.ACCEPTED.getReasonPhrase())) {
                return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            }

        } catch (HttpStatusCodeException e1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e1.getLocalizedMessage());
        } catch (JsonProcessingException e2) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e2.getLocalizedMessage());
        } catch (Exception e3) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e3.getLocalizedMessage());
        }
        return statusCode;
    }
}
