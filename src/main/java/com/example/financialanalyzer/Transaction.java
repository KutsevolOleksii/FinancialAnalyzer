package com.example.financialanalyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String date;
    private double amount;
    private String description;
    private String category;

    public Transaction(String date, double amount, String description) {
        this(date, amount, description, "Інше");
    }
}
