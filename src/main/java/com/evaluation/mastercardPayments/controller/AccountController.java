package com.evaluation.mastercardPayments.controller;



import java.util.List;

import com.evaluation.mastercardPayments.dto.AccountRequestDto;
import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.MiniStatement;
import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.service.AccountService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("accounts")
public class AccountController {

    private static final Logger LOG = LogManager.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createAccount(@Valid @RequestBody AccountRequestDto accountRequest) throws CustomException {
        LOG.info("Inside the Account Controller for creating account with accountId {}",accountRequest.getAccountId() );
        accountService.createAccount(accountRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping(value = "/account/{id}")
    public ResponseEntity<Object> getAccountDetails(@Valid @PathVariable("id") String accountId) throws CustomException {
        LOG.info("Inside Account Controller getting account details including real time balance for the account {}", accountId);
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAccounts() throws CustomException {
        LOG.info("Getting all the accounts");
        return new ResponseEntity<>(accountService.getAllAccountDetails(), HttpStatus.OK);
    }


    @GetMapping("/{accountId}/statements/mini")
    public ResponseEntity<Object> getMiniStatement(@Valid @PathVariable String accountId) throws CustomException {
        LOG.info("Getting mini statement for the given account {}",accountId);
        List<MiniStatement> miniStatement = accountService.getMiniStatement(accountId);
        if(miniStatement.isEmpty()) {
            return new ResponseEntity<>(CustomErrors.NO_TRANSACTIONS_FOUND, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(miniStatement, HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseEntity<Object> deleteAccount(@RequestBody AccountRequestDto account) throws CustomException {
        LOG.info("Soft deleting the account {}", account.getAccountId());
        return accountService.deleteAccount(account);
    }
}
