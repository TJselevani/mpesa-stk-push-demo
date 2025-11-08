package com.example.mpesa.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String merchantRequestId;
    private String checkoutRequestId;
    private String mpesaReceiptNumber;
    private String phoneNumber;
    private Double amount;
    private Long transactionDate;
    private String resultDesc;
    private int resultCode;

    private LocalDateTime createdAt;
}
