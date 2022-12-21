package com.evaluation.mastercardPayments.controller;

import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.exception.CustomException;

import com.evaluation.mastercardPayments.model.TransferRequestDto;
import com.evaluation.mastercardPayments.service.TransactionService;

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
    @Autowired
    TransactionService transactionService;

    @PostMapping("/addAmount")
    public ResponseEntity<HttpStatus> addAmount(@Valid @RequestBody AddAmountDto addAmountRequest) throws CustomException {
        transactionService.addAmount(addAmountRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<HttpStatus> transferMoney(@Valid @RequestBody TransferRequestDto paymentTransferRequest) throws CustomException {
        transactionService.transferMoney(paymentTransferRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
