package com.example.financialanalyzer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/resources/transactions.csv";

        try {
            List<Transaction> transactions =
                    TransactionCSVReader.readTransactions(filePath);

            if (transactions.isEmpty()) {
                System.out.println("Транзакції не знайдено.");
                return;
            }

            System.out.println("=== ФІНАНСОВИЙ АНАЛІЗАТОР ===");
            System.out.println("Завантажено транзакцій: "
                    + transactions.size());

            double balance =
                    TransactionAnalyzer.calculateTotalBalance(transactions);

            TransactionReportGenerator.printBalanceReport(balance);

            String monthYear = "01-2024";
            int count = TransactionAnalyzer.countTransactionsByMonth(
                    transactions, monthYear);

            TransactionReportGenerator.printTransactionsCountByMonth(
                    monthYear, count);

            List<Transaction> topExpenses =
                    TransactionAnalyzer.findTopExpenses(transactions);

            TransactionReportGenerator.printTopExpensesReport(topExpenses);

            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);

            List<Transaction> periodExpenses =
                    TransactionAnalyzer.findExpensesByPeriod(
                            transactions, start, end);

            TransactionReportGenerator.printPeriodExpensesReport(
                    periodExpenses);

            Transaction largest =
                    TransactionAnalyzer.findLargestExpense(
                            transactions, start, end);

            Transaction smallest =
                    TransactionAnalyzer.findSmallestExpense(
                            transactions, start, end);

            TransactionReportGenerator.printLargestAndSmallestExpenses(
                    largest, smallest);

            Map<String, Double> categoryExpenses =
                    TransactionAnalyzer.calculateExpensesByCategory(
                            transactions);

            TransactionReportGenerator.printCategoryReport(categoryExpenses);

            Map<YearMonth, Double> monthlyExpenses =
                    TransactionAnalyzer.calculateExpensesByMonth(
                            transactions);

            TransactionReportGenerator.printMonthlyReport(monthlyExpenses);

        } catch (IOException e) {
            System.err.println("Помилка читання CSV: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Помилка обробки даних: " + e.getMessage());
        }
    }
}
