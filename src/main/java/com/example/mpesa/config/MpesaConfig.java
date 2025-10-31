package com.example.mpesa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MpesaConfig {
    @Value("${mpesa.consumerKey}")
    public String consumerKey;

    @Value("${mpesa.consumerSecret}")
    public String consumerSecret;

    @Value("${mpesa.shortCode}")
    public String shortCode;

    @Value("${mpesa.passKey}")
    public String passKey;

    @Value("${mpesa.callbackUrl}")
    public String callbackUrl;

    @Value("${mpesa.environment}")
    public String environment;

    public String getBaseUrl() {
        return environment.equalsIgnoreCase("sandbox")
                ? "https://sandbox.safaricom.co.ke"
                : "https://api.safaricom.co.ke";
    }
}
