package com.example.api.DTO;

import lombok.Data;

@Data
public class Payment {
    private String phone;
    double amount;
    String username;
}
