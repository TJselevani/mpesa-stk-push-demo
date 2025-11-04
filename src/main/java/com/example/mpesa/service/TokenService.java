// service/TokenService.java
package com.example.mpesa.service;

import com.example.mpesa.config.MpesaConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private MpesaConfig config;

    @Autowired
    private RestTemplate restTemplate;

    private String cachedToken;
    private Instant tokenExpiry;

    public synchronized String getAccessToken() {
        // ✅ Make sure your TokenService caches tokens instead of fetching every time.
        // ✅ This avoids unnecessary token requests and reduces request latency.
        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }

        log.info("🔐 Fetching new M-Pesa access token...");

        String auth = config.consumerKey + ":" + config.consumerSecret;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());

        var headers = new org.springframework.http.HttpHeaders();
        headers.add("Authorization", "Basic " + encoded);

        var entity = new org.springframework.http.HttpEntity<>(headers);
        String url = config.getBaseUrl() + "/oauth/v1/generate?grant_type=client_credentials";

        String response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class).getBody();
        JSONObject json = new JSONObject(response);
        cachedToken = json.getString("access_token");
        tokenExpiry = Instant.now().plusSeconds(3500); // token valid for ~1 hour

        log.info("✅ Token cached successfully");
        return cachedToken;
    }
}
