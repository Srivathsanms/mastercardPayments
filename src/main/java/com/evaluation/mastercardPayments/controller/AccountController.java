package com.evaluation.mastercardPayments.controller;

import java.util.List;

import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.services.AccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    //Creating a new account as a fresh start
    @ApiOperation(value = "This API is used to create an Account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(value = "/create/{accountId}", produces ={MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<String> createAccount(@PathVariable(value="accountId") String accountId) throws CustomException {
        return accountService.createAccount(accountId);
    }


    @GetMapping(value = "/account/{accountId}/balance",produces ={MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public AccountEntity getBalance(@PathVariable ("accountId") String accountId) throws CustomException {
       return accountService.getBalance(accountId);
    }

    @GetMapping(value = "/accounts/all",produces ={MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public List<AccountEntity> fetchAllAccountDetails(){
        return accountService.fetchAllAccounts();
    }




    @PostMapping(value = "/delete")
   // @PostMapping(value = "/delete/{accountId}")
    @ResponseBody
    public String deleteAccount(@RequestBody String accountId) throws CustomException {
        return accountService.deleteAccount(accountId);
    }


}
