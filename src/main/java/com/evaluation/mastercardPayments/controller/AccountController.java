package com.evaluation.mastercardPayments.controller;



import java.util.List;

import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createAccount(@Valid @RequestBody AccountRequestDto accountRequest) throws CustomException {
        accountService.createAccount(accountRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping(value = "/account/{id}")
    public ResponseEntity<Object> getAccountDetails(@Valid @PathVariable("id") String accountId) throws CustomException {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAccounts() throws CustomException {
        return new ResponseEntity<>(accountService.getAllAccountDetails(), HttpStatus.OK);
    }


    @GetMapping("/{accountId}/statements/mini")
    public ResponseEntity<Object> getMiniStatement(@Valid @PathVariable String accountId) throws CustomException {
        List<MiniStatement> miniStatement = accountService.getMiniStatement(accountId);
        if(miniStatement.isEmpty()) {
            return new ResponseEntity<>("No fund transfer for this account", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(miniStatement, HttpStatus.OK);
    }
}
