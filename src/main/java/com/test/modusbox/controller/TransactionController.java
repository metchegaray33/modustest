package com.test.modusbox.controller;

import static com.test.modusbox.constants.Paths.CREATE_TRANSACTION_PATH;
import static com.test.modusbox.constants.Paths.TRANSACTION_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.modusbox.model.dto.TransactionDTO;
import com.test.modusbox.service.TransactionService;

@RestController
@RequestMapping(TRANSACTION_PATH)
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping(CREATE_TRANSACTION_PATH)
    public ResponseEntity<String> addTransaction(@RequestHeader(value = "token") String token, @RequestBody TransactionDTO transactionDTO) {
        logger.debug("POST request addTransaction: {}", transactionDTO);
        return new ResponseEntity<>(service.processTransaction(transactionDTO, token), HttpStatus.ACCEPTED);

    }
}
