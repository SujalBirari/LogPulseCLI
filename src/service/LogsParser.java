package service;

import exception.LogParseException;
import model.LogEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogsParser {
    private static final Path FILE_PATH = Paths.get("logs", "server.log");
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

    void main() {
        if (Files.exists(FILE_PATH)) {
            List<LogEntry> logsEntryList = parseLogFiles(FILE_PATH.toString());

            IO.println("Successfully parsed " + logsEntryList.size() + " lines.");
            logsEntryList.stream().limit(5).forEach(System.out::println);
        } else {
            System.err.println("Logs file does not exist!!!" + FILE_PATH.toAbsolutePath());
        }
    }

    public List<LogEntry> parseLogFiles(String path) {
        List<LogEntry> logEntries = new ArrayList<>();
        Path filePath = Paths.get(path);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    LogEntry logEntry = getLogEntry(line, lineNumber);
                    logEntries.add(logEntry);
                } catch (LogParseException e) {
                    System.err.println("Skipping bad line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return logEntries;
    }

    private LogEntry getLogEntry(String line, int lineNumber) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new LogParseException("Invalid format for line " + lineNumber + ": " + line);
        }

        String timestamp = matcher.group("timestamp");
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, FORMATTER);
        String logLevel = matcher.group("level");
        String logService = matcher.group("service");
        String message = matcher.group("message");

        return new LogEntry(localDateTime, logLevel, logService, message);
    }
}
