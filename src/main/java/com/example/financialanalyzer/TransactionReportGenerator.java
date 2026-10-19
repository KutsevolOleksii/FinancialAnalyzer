package com.example.financialanalyzer;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class TransactionReportGenerator {

    private TransactionReportGenerator() {
    }

    public static void printBalanceReport(double totalBalance) {
        System.out.printf("Загальний баланс: %.2f%n", totalBalance);
    }

    public static void printTransactionsCountByMonth(
            String monthYear, int count) {

        System.out.printf(
                "Кількість транзакцій за %s: %d%n",
                monthYear, count);
    }

    public static void printTopExpensesReport(
            List<Transaction> expenses) {

        System.out.println("\n10 найбільших витрат:");

        if (expenses.isEmpty()) {
            System.out.println("Витрат не знайдено.");
            return;
        }

        for (Transaction expense : expenses) {
            System.out.printf("%s | %s | %.2f%n",
                    expense.getDate(),
                    expense.getDescription(),
                    expense.getAmount());
        }
    }

    public static void printPeriodExpensesReport(
            List<Transaction> expenses) {

        System.out.println("\nВитрати за період:");

        if (expenses.isEmpty()) {
            System.out.println("Витрат за цей період не знайдено.");
            return;
        }

        for (Transaction expense : expenses) {
            System.out.printf("%s | %s | %.2f%n",
                    expense.getDate(),
                    expense.getDescription(),
                    expense.getAmount());
        }
    }

    public static void printCategoryReport(Map<String, Double> expenses) {
        System.out.println("\nВитрати за категоріями:");

        expenses.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue()
                        .reversed())
                .forEach(entry -> {
                    int stars = (int) (entry.getValue() / 1000);
                    System.out.printf("%-15s %10.2f | %s%n",
                            entry.getKey(),
                            entry.getValue(),
                            "*".repeat(stars));
                });
    }

    public static void printMonthlyReport(Map<YearMonth, Double> expenses) {
        System.out.println("\nВитрати за місяцями:");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM-yyyy");

        expenses.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    int stars = (int) (entry.getValue() / 1000);
                    System.out.printf("%s %10.2f | %s%n",
                            entry.getKey().format(formatter),
                            entry.getValue(),
                            "*".repeat(stars));
                });
    }

    public static void printLargestAndSmallestExpenses(
            Transaction largest, Transaction smallest) {

        System.out.println("\nНайбільша витрата:");
        printOneExpense(largest);

        System.out.println("Найменша витрата:");
        printOneExpense(smallest);
    }

    private static void printOneExpense(Transaction transaction) {
        if (transaction == null) {
            System.out.println("Не знайдено.");
            return;
        }

        System.out.printf("%s | %s | %.2f%n",
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount());
    }
}
