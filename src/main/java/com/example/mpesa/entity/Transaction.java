package com.example.mpesa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
