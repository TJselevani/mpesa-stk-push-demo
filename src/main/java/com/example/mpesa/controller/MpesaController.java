package com.example.mpesa.controller;

import com.example.mpesa.dto.*;
import com.example.mpesa.service.MpesaService;
import com.example.mpesa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody StkPushRequest request) {
        mpesaService.initiateStkPushAsync(request);
        return ResponseEntity.accepted().body("STK push initiated. Check your phone.");
    }

    @PostMapping("/callback")
    public void callback(@RequestBody StkCallback callback) {
        transactionService.handleCallback(callback);
        System.out.println("📞 Received Callback: " + callback);
    }
}
