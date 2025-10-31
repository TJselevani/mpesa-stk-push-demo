package com.example.mpesa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StkPushResponse {
    private String merchantRequestId;
    private String checkoutRequestId;
    private String responseDescription;
    private String responseCode;
}
