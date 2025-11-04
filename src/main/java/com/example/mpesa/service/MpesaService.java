package com.example.mpesa.service;

import com.example.mpesa.config.MpesaConfig;
import com.example.mpesa.dto.StkPushRequest;
import com.example.mpesa.dto.StkPushResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Service
public class MpesaService {

    @Autowired
    private MpesaConfig config;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String STK_PUSH_ENDPOINT = "/mpesa/stkpush/v1/processrequest";

    public StkPushResponse initiateStkPush(StkPushRequest req) {
        long start = System.currentTimeMillis();
        try {
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
            String password = Base64.getEncoder().encodeToString(
                    (config.shortCode + config.passKey + timestamp).getBytes()
            );

            JSONObject body = new JSONObject();
            body.put("BusinessShortCode", config.shortCode);
            body.put("Password", password);
            body.put("Timestamp", timestamp);
            body.put("TransactionType", "CustomerPayBillOnline");
            body.put("Amount", req.getAmount());
            body.put("PartyA", req.getPhoneNumber());
            body.put("PartyB", config.shortCode);
            body.put("PhoneNumber", req.getPhoneNumber());
            body.put("CallBackURL", config.callbackUrl);
            body.put("AccountReference", req.getAccountReference());
            body.put("TransactionDesc", req.getDescription());

            String accessToken = tokenService.getAccessToken();

            var headers = new org.springframework.http.HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-Type", "application/json");

            var entity = new org.springframework.http.HttpEntity<>(body.toString(), headers);

            String url = config.getBaseUrl() + STK_PUSH_ENDPOINT;
            String response = restTemplate.postForObject(url, entity, String.class);
            JSONObject json = new JSONObject(response);

            long elapsed = System.currentTimeMillis() - start;
            log.info("⚡ STK Push initiated in {} ms for phone {}", elapsed, req.getPhoneNumber());

            return new StkPushResponse(
                    json.optString("MerchantRequestID"),
                    json.optString("CheckoutRequestID"),
                    json.optString("ResponseDescription"),
                    json.optString("ResponseCode")
            );

        } catch (Exception e) {
            log.error("❌ STK Push failed: {}", e.getMessage(), e);
            throw new RuntimeException("STK Push failed: " + e.getMessage(), e);
        }
    }

    @Async
    public void initiateStkPushAsync(StkPushRequest req) {
        log.info("🚀 Initiating STK Push async for {}", req.getPhoneNumber());
        initiateStkPush(req);
    }
}
