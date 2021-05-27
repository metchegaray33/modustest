package com.test.modusbox.controller;

import static com.test.modusbox.constants.Paths.TOKEN_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.modusbox.model.dto.TransactionDTO;
import com.test.modusbox.service.TokenService;

@RestController
@RequestMapping(TOKEN_PATH)
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token")
    public String generateToken(@RequestBody TransactionDTO transactionDTO) {
        return tokenService.generateToken(transactionDTO);
    }

    @PostMapping("/test")
    public String testStore(@RequestHeader(value = "token") String token, @RequestBody TransactionDTO transactionDTO) {
        //According to the "Token" and user related information, go to "Redis" to verify whether there is corresponding information
        boolean result = tokenService.validToken(token, transactionDTO.getTransactionId());
        //Respond to different information according to the verification results
        return result ? "Normal call" : "Repeat call";
    }

}