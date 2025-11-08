package com.example.mpesa.service;

import com.example.mpesa.dto.B2CCallback;
import com.example.mpesa.model.B2CTransaction;
import com.example.mpesa.repository.MongoB2CTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoB2CTransactionService {

    private final MongoB2CTransactionRepository b2cTransactionRepository;

    public void handleB2CCallback(B2CCallback callback) {
        var result = callback.getResult();

        Double transactionAmount = null;
        String transactionReceipt = null;
        String transactionCompletedDateTime = null;
        String receiverPartyPublicName = null;
        Double workingAccountBalance = null;
        Double utilityAccountBalance = null;
        Double chargesPaidAccountBalance = null;

        if (result.getResultParameters() != null && result.getResultParameters().getResultParameter() != null) {
            for (var param : result.getResultParameters().getResultParameter()) {
                switch (param.getKey()) {
                    case "TransactionAmount" -> transactionAmount = parseDoubleSafe(param.getValue());
                    case "TransactionReceipt" -> transactionReceipt = safeToString(param.getValue());
                    case "TransactionCompletedDateTime" -> transactionCompletedDateTime = safeToString(param.getValue());
                    case "ReceiverPartyPublicName" -> receiverPartyPublicName = safeToString(param.getValue());
                    case "B2CWorkingAccountAvailableFunds" -> workingAccountBalance = parseDoubleSafe(param.getValue());
                    case "B2CUtilityAccountAvailableFunds" -> utilityAccountBalance = parseDoubleSafe(param.getValue());
                    case "B2CChargesPaidAccountAvailableFunds" -> chargesPaidAccountBalance = parseDoubleSafe(param.getValue());
                }
            }
        }

        B2CTransaction tx = B2CTransaction.builder()
                .transactionId(result.getTransactionId())
                .conversationId(result.getConversationId())
                .originatorConversationId(result.getOriginatorConversationId())
                .transactionAmount(transactionAmount)
                .transactionReceipt(transactionReceipt)
                .receiverPartyPublicName(receiverPartyPublicName)
                .transactionCompletedDateTime(transactionCompletedDateTime)
                .workingAccountBalance(workingAccountBalance)
                .utilityAccountBalance(utilityAccountBalance)
                .chargesPaidAccountBalance(chargesPaidAccountBalance)
                .resultCode(result.getResultCode())
                .resultDesc(result.getResultDesc())
                .createdAt(LocalDateTime.now())
                .status(result.getResultCode() == 0 ? "SUCCESS" : "FAILED")
                .build();

        b2cTransactionRepository.save(tx);

        if (result.getResultCode() == 0) {
            log.info("✅ B2C Transaction successful: {}", transactionReceipt);
        } else {
            log.warn("⚠️ B2C Transaction failed: {} | Reason: {}", result.getResultCode(), result.getResultDesc());
        }
    }

    // helper methods
    private Double parseDoubleSafe(Object value) {
        try {
            return value != null ? Double.parseDouble(value.toString()) : null;
        } catch (NumberFormatException e) {
            log.error("Failed to parse double: {}", value, e);
            return null;
        }
    }

    private String safeToString(Object value) {
        return value != null ? value.toString() : null;
    }

    public void handleB2CTimeout(B2CCallback callback) {
        var result = callback.getResult();

        B2CTransaction tx = B2CTransaction.builder()
                .transactionId(result.getTransactionId())
                .conversationId(result.getConversationId())
                .originatorConversationId(result.getOriginatorConversationId())
                .transactionAmount(null)
                .transactionReceipt(null)
                .receiverPartyPublicName(null)
                .transactionCompletedDateTime(null)
                .workingAccountBalance(null)
                .utilityAccountBalance(null)
                .chargesPaidAccountBalance(null)
                .resultCode(result.getResultCode())
                .resultDesc(result.getResultDesc())
                .status("TIMEOUT")
                .createdAt(LocalDateTime.now())
                .build();

        b2cTransactionRepository.save(tx);
        log.error("⏳ B2C Timeout: OriginatorConversationID={}, Reason={}",
                result.getOriginatorConversationId(), result.getResultDesc());
    }

}
