package com.example.mpesa.dto;

import lombok.Data;
import java.util.Map;

@Data
public class StkCallback {
    private Body body;

    @Data
    public static class Body {
        private StkCallbackItem stkCallback;
    }

    @Data
    public static class StkCallbackItem {
        private String MerchantRequestID;
        private String CheckoutRequestID;
        private String ResultCode;
        private String ResultDesc;
        private CallbackMetadata CallbackMetadata;
    }
}
