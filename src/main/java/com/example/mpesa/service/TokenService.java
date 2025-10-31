package com.example.mpesa.service;

import com.example.mpesa.config.MpesaConfig;
import com.example.mpesa.util.HttpUtil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;

import java.util.Base64;

@Service
public class TokenService {
    @Autowired
    private MpesaConfig config;

    public String getAccessToken() {
        try {
            String credentials = config.consumerKey + ":" + config.consumerSecret;
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

            String response = HttpUtil.get(config.getBaseUrl() + "/oauth/v1/generate?grant_type=client_credentials",
                    "Authorization", "Basic " + encoded);

            JSONObject json = new JSONObject(response);
            return json.getString("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token: " + e.getMessage(), e);
        }
    }
}
