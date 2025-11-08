package com.example.mpesa.service;

import com.example.mpesa.dto.StkCallback;
import com.example.mpesa.model.Transaction;
import com.example.mpesa.repository.MongoTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoTransactionService {

    private final MongoTransactionRepository transactionRepository;

    public void handleCallback(StkCallback callback) {
        var stk = callback.getBody().getStkCallback();

        String receipt = null;
        Double amount = null;
        Long transactionDate = null;
        String phoneNumber = null;

        // Extract metadata only if available
        if (stk.getCallbackMetadata() != null && stk.getCallbackMetadata().getItem() != null) {
            var metadata = stk.getCallbackMetadata().getItem();

            for (var item : metadata) {
                switch (item.getName()) {
                    case "Amount" -> amount = parseDoubleSafe(item.getValue());
                    case "MpesaReceiptNumber" -> receipt = safeToString(item.getValue());
                    case "TransactionDate" -> transactionDate = parseLongSafe(item.getValue());
                    case "PhoneNumber" -> phoneNumber = safeToString(item.getValue());
                }
            }
        }

        // Build and save the transaction regardless of resultCode
        Transaction tx = Transaction.builder()
                .merchantRequestId(stk.getMerchantRequestID())
                .checkoutRequestId(stk.getCheckoutRequestID())
                .mpesaReceiptNumber(receipt)
                .phoneNumber(phoneNumber)
                .amount(amount)
                .transactionDate(transactionDate)
                .resultDesc(stk.getResultDesc())
                .resultCode(stk.getResultCode())
                .createdAt(LocalDateTime.now())
                .status(stk.getResultCode() == 0 ? "SUCCESS" : "FAILED")
                .build();

        transactionRepository.save(tx);

        if (stk.getResultCode() == 0) {
            log.info("✅ Successful transaction saved for phone: {}", phoneNumber);
        } else {
            log.warn("⚠️ Failed transaction saved for phone: {} | Reason: {}", phoneNumber, stk.getResultDesc());
        }
    }

    // --- Helper methods for safe parsing ---
    private Double parseDoubleSafe(Object value) {
        try {
            return value != null ? Double.parseDouble(value.toString()) : null;
        } catch (NumberFormatException e) {
            log.error("Failed to parse amount value: {}", value, e);
            return null;
        }
    }

    private Long parseLongSafe(Object value) {
        try {
            return value != null ? Long.parseLong(value.toString()) : null;
        } catch (NumberFormatException e) {
            log.error("Failed to parse transaction date value: {}", value, e);
            return null;
        }
    }

    private String safeToString(Object value) {
        return value != null ? value.toString() : null;
    }
}
