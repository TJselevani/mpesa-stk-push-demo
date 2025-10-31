package com.example.mpesa.dto;

import lombok.Data;

@Data
public class StkPushRequest {
    private String phoneNumber;
    private String accountReference;
    private String description;
    private double amount;
}
