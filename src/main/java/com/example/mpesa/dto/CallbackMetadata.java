package com.example.mpesa.dto;

import lombok.Data;
import java.util.List;

@Data
public class CallbackMetadata {
    private List<Item> Item;

    @Data
    public static class Item {
        private String Name;
        private Object Value; // Can be String, Double, Long, etc.
    }
}
