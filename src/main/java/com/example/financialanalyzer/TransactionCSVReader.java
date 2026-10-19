package com.example.financialanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class TransactionCSVReader {

    private TransactionCSVReader() {
    }

    public static List<String> readLines(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            URI.create(source).toURL().openStream(),
                            StandardCharsets.UTF_8))) {
                return reader.lines().toList();
            }
        }

        return Files.readAllLines(Path.of(source), StandardCharsets.UTF_8);
    }

    public static List<Transaction> readTransactions(String source)
            throws IOException {
        return parseLines(readLines(source));
    }

    public static List<Transaction> parseLines(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] values = line.split(",", -1);

            if (values.length < 3) {
                continue;
            }

            // Пропускаем строку заголовков, если она есть.
            if (values[0].trim().equalsIgnoreCase("date")) {
                continue;
            }

            try {
                String date = normalizeDate(values[0].trim());
                double amount = Double.parseDouble(
                        values[1].trim().replace(',', '.'));

                String description = values[2].trim();
                String category = values.length >= 4
                        && !values[3].isBlank()
                        ? values[3].trim()
                        : inferCategory(description);

                transactions.add(new Transaction(
                        date, amount, description, category));
            } catch (NumberFormatException | DateTimeParseException e) {
                System.err.println("Пропущена некоректна транзакція: " + line);
            }
        }

        return transactions;
    }

    private static String normalizeDate(String date) {
        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(date, format).toString();
            } catch (DateTimeParseException ignored) {
                // Пробуем следующий формат.
            }
        }

        throw new DateTimeParseException(
                "Непідтримуваний формат дати", date, 0);
    }

    private static String inferCategory(String description) {
        String text = description.toLowerCase(Locale.ROOT);

        if (text.contains("food") || text.contains("grocery")
                || text.contains("супермаркет") || text.contains("продукт")) {
            return "Продукти";
        }
        if (text.contains("transport") || text.contains("taxi")
                || text.contains("транспорт")) {
            return "Транспорт";
        }
        if (text.contains("rent") || text.contains("комунал")) {
            return "Житло";
        }
        if (text.contains("salary") || text.contains("зарплат")) {
            return "Дохід";
        }
        if (text.contains("health") || text.contains("аптек")) {
            return "Здоров'я";
        }

        return "Інше";
    }
}
