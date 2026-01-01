package service;

import model.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogsParser {
    // named groups in patterns for matchers
    // non-greedy pattern (.* vs .*?)
    // matcher methods - to match, search or extract from pattern
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "\\[(?<timestamp>.*?)]\\s+" +
                    "(?<level>INFO|WARNING|ERROR)\\s+" +
                    "\\[(?<service>.*?)]\\s+" +
                    "(?<message>.*)"
    );
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    public Optional<LogEntry> parseLine(String line) {
        try {
            Matcher matcher = LOG_PATTERN.matcher(line);

            // If regex doesn't match, return Empty (Steam will skip it)
            if (!matcher.matches()) {
                return Optional.empty();
            }

            String timestamp = matcher.group("timestamp");
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp, FORMATTER);
            String logLevel = matcher.group("level");
            String logService = matcher.group("service");
            String message = matcher.group("message");

            return Optional.of(new LogEntry(localDateTime, logLevel, logService, message));
        } catch (Exception e) {
            // If Date parsing fails or anything else goes wrong, return Empty
            return Optional.empty();
        }
    }
}
