package com.example.mpesa.service;

import com.example.mpesa.config.MpesaConfig;
import com.example.mpesa.dto.StkPushRequest;
import com.example.mpesa.dto.StkPushResponse;
import com.example.mpesa.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class MpesaService {
    @Autowired
    private MpesaConfig config;

    @Autowired
    private TokenService tokenService;

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

            String response = HttpUtil.post(config.getBaseUrl() + "/mpesa/stkpush/v1/processrequest",
                    "Authorization", "Bearer " + tokenService.getAccessToken(), body.toString());

            JSONObject json = new JSONObject(response);
            return new StkPushResponse(
                    json.optString("MerchantRequestID"),
                    json.optString("CheckoutRequestID"),
                    json.optString("ResponseDescription"),
                    json.optString("ResponseCode")
            );
        } catch (Exception e) {
            throw new RuntimeException("STK Push failed: " + e.getMessage(), e);
        }
    }
}
