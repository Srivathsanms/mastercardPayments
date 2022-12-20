package com.evaluation.mastercardPayments.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.evaluation.mastercardPayments.dto.FundDto;
import com.evaluation.mastercardPayments.dto.TransferFundDto;
import com.evaluation.mastercardPayments.entity.TransactionEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("transactions")
public class TransactionController {

//TODO : Convert the path variable for POST request to JSON objects in the request Body -Done
    //TODO : Update readme

    @Autowired
    private TransactionService transactionService;
    @PostMapping(value = "/transfer",produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFund
                                               ) throws CustomException {
            transactionService.fundTransfer(transferFund.getDebtorAccountNumber(), transferFund.getCreditorAccountNumber()
                    , transferFund.getAmount());
            return new ResponseEntity<>("Success",OK);
        }

    @GetMapping(value = "/accounts/{accountId}/statements/mini", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<TransactionEntity> getMiniStatement(@PathVariable ("accountId") String accountId) throws CustomException {
        return transactionService.getMiniStatement(accountId);
    }

    @PostMapping(value = "/addFund")
    @ResponseBody
    public String addFundToAccount(@RequestBody FundDto funds) throws CustomException {
        return transactionService.addFundToAccount(funds.getAccountNumber(),funds.getAmount());
    }

}
