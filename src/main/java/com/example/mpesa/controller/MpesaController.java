package com.example.mpesa.controller;

import com.example.mpesa.dto.*;
import com.example.mpesa.service.MpesaService;
import com.example.mpesa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    @Autowired
    private MpesaService mpesaService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/stkpush")
    public StkPushResponse stkPush(@RequestBody StkPushRequest request) {
        return mpesaService.initiateStkPush(request);
    }

    @PostMapping("/callback")
    public void callback(@RequestBody StkCallback callback) {
        System.out.println("📞 Received Callback: " + callback);
        transactionService.handleCallback(callback);

    }
}
