package com.evaluation.mastercardPayments.controller;

import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.exception.CustomException;

import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.service.TransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("transaction")
public class TransactionController {
    //  AddAmount, transferAmount

    private static final Logger LOG = LogManager.getLogger(AccountController.class);
    @Autowired
    TransactionService transactionService;


    @PostMapping("/addAmount")
    public ResponseEntity<HttpStatus> addAmount(@Valid @RequestBody AddAmountDto addAmountRequest) throws CustomException {
        LOG.info("Adding amount to the account Id  {}", addAmountRequest.getAccountId());
        transactionService.addAmount(addAmountRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<HttpStatus> transferMoney(@RequestBody @Valid TransferRequestDto paymentTransferRequest) throws CustomException {
        transactionService.transferAmount(paymentTransferRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
