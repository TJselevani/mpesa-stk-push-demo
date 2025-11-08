package com.example.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class B2CCallback {

    @JsonProperty("Result")
    private Result result;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("ResultType")
        private int resultType;

        @JsonProperty("ResultCode")
        private int resultCode;

        @JsonProperty("ResultDesc")
        private String resultDesc;

        @JsonProperty("OriginatorConversationID")
        private String originatorConversationId;

        @JsonProperty("ConversationID")
        private String conversationId;

        @JsonProperty("TransactionID")
        private String transactionId;

        @JsonProperty("ResultParameters")
        private ResultParameters resultParameters;

        @JsonProperty("ReferenceData")
        private ReferenceData referenceData; // optional field
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultParameters {

        @JsonProperty("ResultParameter")
        private List<ResultParameter> resultParameter;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultParameter {

        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private Object value;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReferenceData {

        @JsonProperty("ReferenceItem")
        private ReferenceItem referenceItem;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReferenceItem {

        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private String value;
    }
}
