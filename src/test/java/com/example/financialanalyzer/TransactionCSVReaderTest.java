package com.example.financialanalyzer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {

    @Test
    void testReadCSVLines() {
        List<String> lines = List.of(
                "date,amount,description,category",
                "2024-01-01,1000,Salary,Дохід",
                "2024-01-02,-250,Grocery,Продукти"
        );

        List<Transaction> transactions =
                TransactionCSVReader.parseLines(lines);

        assertEquals(2, transactions.size());
        assertEquals("2024-01-01", transactions.get(0).getDate());
        assertEquals(1000.0, transactions.get(0).getAmount(), 0.001);
        assertEquals("Продукти", transactions.get(1).getCategory());
    }

    @Test
    void testInvalidCSVRowIsSkipped() {
        List<String> lines = List.of(
                "date,amount,description,category",
                "2024-01-01,100,Salary,Дохід",
                "incorrect-date,not-a-number,Invalid,Інше"
        );

        List<Transaction> transactions =
                TransactionCSVReader.parseLines(lines);

        assertEquals(1, transactions.size());
    }
}
