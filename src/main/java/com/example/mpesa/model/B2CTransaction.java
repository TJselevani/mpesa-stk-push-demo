package com.example.mpesa.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "b2c_transactions")
public class B2CTransaction {

    @Id
    private String id;

    private String transactionId;
    private String conversationId;
    private String originatorConversationId;
    private Double transactionAmount;
    private String transactionReceipt;
    private String receiverPartyPublicName;
    private String transactionCompletedDateTime;
    private Double workingAccountBalance;
    private Double utilityAccountBalance;
    private Double chargesPaidAccountBalance;
    private int resultCode;
    private String resultDesc;

    private LocalDateTime createdAt;
    private String status;
}
