package com.example.financialanalyzer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {

    private TransactionAnalyzer() {
    }

    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static int countTransactionsByMonth(
            List<Transaction> transactions, String monthYear) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM-yyyy");

        YearMonth target = YearMonth.parse(monthYear, formatter);

        return (int) transactions.stream()
                .filter(t -> YearMonth.from(
                        LocalDate.parse(t.getDate())).equals(target))
                .count();
    }

    public static List<Transaction> findTopExpenses(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    public static List<Transaction> findExpensesByPeriod(
            List<Transaction> transactions,
            LocalDate start,
            LocalDate end) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Початкова дата не може бути пізніше кінцевої");
        }

        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate());
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .toList();
    }

    public static Transaction findLargestExpense(
            List<Transaction> transactions,
            LocalDate start, LocalDate end) {

        return findExpensesByPeriod(transactions, start, end)
                .stream()
                .min(Comparator.comparingDouble(Transaction::getAmount))
                .orElse(null);
    }

    public static Transaction findSmallestExpense(
            List<Transaction> transactions,
            LocalDate start, LocalDate end) {

        return findExpensesByPeriod(transactions, start, end)
                .stream()
                .max(Comparator.comparingDouble(Transaction::getAmount))
                .orElse(null);
    }

    public static Map<String, Double> calculateExpensesByCategory(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(
                                t -> Math.abs(t.getAmount()))));
    }

    public static Map<YearMonth, Double> calculateExpensesByMonth(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(LocalDate.parse(t.getDate())),
                        Collectors.summingDouble(
                                t -> Math.abs(t.getAmount()))));
    }
}
