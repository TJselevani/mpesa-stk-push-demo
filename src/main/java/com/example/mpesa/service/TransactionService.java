package com.example.mpesa.service;

import com.example.mpesa.dto.StkCallback;
import com.example.mpesa.model.Transaction;
import com.example.mpesa.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void handleCallback(StkCallback callback) {
        var stk = callback.getBody().getStkCallback();

        log.info("Received callback: {}", stk.getResultDesc());

        if (stk.getResultCode() == 0 && stk.getCallbackMetadata() != null) {
            var metadata = stk.getCallbackMetadata().getItem();

            String receipt = null;
            Double amount = null;
            Long transactionDate = null;
            String phoneNumber = null;

            for (var item : metadata) {
                switch (item.getName()) {
                    case "Amount" -> amount = Double.parseDouble(item.getValue().toString());
                    case "MpesaReceiptNumber" -> receipt = item.getValue().toString();
                    case "TransactionDate" -> transactionDate = Long.parseLong(item.getValue().toString());
                    case "PhoneNumber" -> phoneNumber = item.getValue().toString();
                }
            }

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
                    .build();

            transactionRepository.save(tx);
            log.info("✅ Transaction saved successfully for phone: {}", phoneNumber);
        } else {
            log.warn("⚠️ Failed transaction: {}", stk.getResultDesc());
        }
    }
}
