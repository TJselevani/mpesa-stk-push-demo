package com.example.mpesa.controller;

import com.example.mpesa.dto.*;
import com.example.mpesa.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    @Autowired
    private MpesaService mpesaService;

    @Autowired
    private MongoTransactionService transactionService;

    @Autowired
    private PostgresTransactionService transactionService2;

    @Autowired
    private MongoB2CTransactionService b2cTransactionService;

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
        log.info("\uD83D\uDCDE Received Callback: {}", callback);
    }

    @PostMapping("/b2c-callback")
    public ResponseEntity<String> handleB2CCallback(@RequestBody B2CCallback callback) {
        log.info("📩 Received B2C callback: {}", callback);
        b2cTransactionService.handleB2CCallback(callback);
        return ResponseEntity.ok("B2C callback received successfully");
    }

    @PostMapping("/timeout")
    public ResponseEntity<String> handleB2CTimeout(@RequestBody B2CCallback callback) {
        log.warn("⏰ Received B2C Timeout Callback: {}", callback);
        b2cTransactionService.handleB2CTimeout(callback);
        return ResponseEntity.ok("B2C timeout callback processed successfully");
    }
}
