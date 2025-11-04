package com.example.mpesa.service;

import com.example.mpesa.config.MpesaConfig;
import com.example.mpesa.dto.StkPushRequest;
import com.example.mpesa.dto.StkPushResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpesaServiceV2 {

    private final MpesaConfig config;
    private final TokenService tokenService;
    private final WebClient webClient;

    /**
     * Synchronous (blocking) STK Push
     */
    public StkPushResponse initiateStkPush(StkPushRequest req) {
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

            return webClient.post()
                    .uri("/mpesa/stkpush/v1/processrequest")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(body.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> {
                        JSONObject json = new JSONObject(response);
                        return new StkPushResponse(
                                json.optString("MerchantRequestID"),
                                json.optString("CheckoutRequestID"),
                                json.optString("ResponseDescription"),
                                json.optString("ResponseCode")
                        );
                    })
                    .block(); // blocking for now (for API simplicity)
        } catch (Exception e) {
            log.error("❌ STK Push failed: {}", e.getMessage(), e);
            throw new RuntimeException("STK Push failed: " + e.getMessage(), e);
        }
    }

    /**
     * Asynchronous (non-blocking) STK Push for perceived speed
     */
    @Async
    public void initiateStkPushAsync(StkPushRequest req) {
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

            webClient.post()
                    .uri("/mpesa/stkpush/v1/processrequest")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(body.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> log.info("✅ STK Push initiated: {}", response))
                    .doOnError(err -> log.error("❌ Error sending STK Push: {}", err.getMessage()))
                    .subscribe(); // non-blocking send
        } catch (Exception e) {
            log.error("❌ Async STK Push failed: {}", e.getMessage(), e);
        }
    }
}
