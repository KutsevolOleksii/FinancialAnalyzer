package com.example.financialanalyzer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = List.of(
                new Transaction("2024-01-01", 100.0, "Дохід"),
                new Transaction("2024-01-02", -50.0, "Витрата"),
                new Transaction("2024-01-03", 150.0, "Дохід")
        );

        assertEquals(200.0,
                TransactionAnalyzer.calculateTotalBalance(transactions),
                0.001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = List.of(
                new Transaction("2024-02-01", 50.0, "Дохід"),
                new Transaction("2024-02-15", -20.0, "Витрата"),
                new Transaction("2024-03-05", 100.0, "Дохід")
        );

        assertEquals(2,
                TransactionAnalyzer.countTransactionsByMonth(
                        transactions, "02-2024"));

        assertEquals(1,
                TransactionAnalyzer.countTransactionsByMonth(
                        transactions, "03-2024"));
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> transactions = java.util.stream.IntStream
                .rangeClosed(1, 12)
                .mapToObj(i -> new Transaction(
                        "2024-01-01", -i * 100.0, "Витрата " + i))
                .toList();

        List<Transaction> result =
                TransactionAnalyzer.findTopExpenses(transactions);

        assertEquals(10, result.size());
        assertEquals(-1200.0, result.get(0).getAmount(), 0.001);
        assertEquals(-300.0, result.get(9).getAmount(), 0.001);
    }

    @Test
    void testFindLargestAndSmallestExpensesByPeriod() {
        List<Transaction> transactions = List.of(
                new Transaction("2024-01-01", -100.0, "Мала"),
                new Transaction("2024-01-10", -900.0, "Велика"),
                new Transaction("2024-03-01", -500.0, "Поза періодом")
        );

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        assertEquals(-900.0,
                TransactionAnalyzer.findLargestExpense(
                        transactions, start, end).getAmount(), 0.001);

        assertEquals(-100.0,
                TransactionAnalyzer.findSmallestExpense(
                        transactions, start, end).getAmount(), 0.001);
    }

    @Test
    void testCalculateExpensesByCategory() {
        List<Transaction> transactions = List.of(
                new Transaction("2024-01-01", -100.0, "Хліб", "Продукти"),
                new Transaction("2024-01-02", -200.0, "Молоко", "Продукти"),
                new Transaction("2024-01-03", 500.0, "Зарплата", "Дохід")
        );

        var result =
                TransactionAnalyzer.calculateExpensesByCategory(transactions);

        assertEquals(300.0, result.get("Продукти"), 0.001);
        assertFalse(result.containsKey("Дохід"));
    }
}
