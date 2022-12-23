package com.evaluation.mastercardPayments.controller;

import com.evaluation.mastercardPayments.dto.AddAmountDto;
import com.evaluation.mastercardPayments.entity.AccountEntity;
import com.evaluation.mastercardPayments.exception.CustomErrors;
import com.evaluation.mastercardPayments.exception.CustomException;
import com.evaluation.mastercardPayments.model.AccountStatus;
import com.evaluation.mastercardPayments.model.CurrencyType;
import com.evaluation.mastercardPayments.model.TransferRequestDto;

import com.evaluation.mastercardPayments.service.TransactionService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;


    @Test
    void transferAmountSuccessTest() throws Exception {
        Mockito.when(transactionService.transferAmount(Mockito.any(TransferRequestDto.class))).thenReturn(null);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/transaction/transfer")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .content("{\n" +
                        " \"debtorAccount\": 111,\n" +
                        " \"creditorAccount\":222,\n" +
                        " \"amount\": 20,\n" +
                        " \"currency\":\"GBP\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
        ;
    }


    @Test
    void addAmountSuccessTest() throws Exception {
        doNothing().when(transactionService).addAmount(Mockito.any(AddAmountDto.class));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/transaction/addAmount")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .content("{\n" +
                        " \"accountId\": 111,\n" +
                        " \"amount\": 20\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isAccepted())
        ;
    }

    @Test
    void addAmountForInvalidAccountTest() throws Exception {
        doThrow(new CustomException(CustomErrors.INVALID_ACCOUNT_NUMBER)).when(transactionService).addAmount(Mockito.any(AddAmountDto.class));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/transaction/addAmount")
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .content("{\n" +
                        " \"accountId\": \"abc\",\n" +
                        " \"amount\": 20\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid Account Number")))
        ;
    }
}
