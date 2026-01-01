import model.LogEntry;
import model.LogReport;
import service.LogAnalytics;
import service.LogsParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String DEFAULT_FILE = "logs/server.log";
    private static final String DEFAULT_LEVEL = "ERROR";

    public static void main(String[] args) {
        String targetFile = DEFAULT_FILE;
        String targetLevel = DEFAULT_LEVEL;

        // Usage: java Main [filepath] [--level LEVEL]
        for (int i = 0; i < args.length; i++) {
            if ("--level".equals(args[i]) && i + 1 < args.length) {
                targetLevel = args[i + 1].toUpperCase(); // Handle "warn" -> "WARN"
                i++; // Skip the next arg since we consumed it
            } else if (!args[i].startsWith("--")) {
                // If it's not a flag, assume it's the file path
                targetFile = args[i];
            }
        }
        Path filePath = Paths.get(targetFile);
        System.out.println("--------------------------------------------------");
        System.out.println("🚀 LogPulse CLI Started");
        System.out.println("📂 Target File:  " + filePath.toAbsolutePath());
        System.out.println("🔍 Filter Level: " + targetLevel);
        System.out.println("--------------------------------------------------");

        if (!Files.exists(filePath)) {
            System.err.println("❌ Error: File not found at " + filePath.toAbsolutePath());
            System.out.println("Tip: Make sure you ran LogGenerator first!");
            return;
        }

        LogsParser parser = new LogsParser();
        Map<String, Long> logStats;
        final String levelFilter = targetLevel;

        // this is a zero-memory stream for however GB of files
        try (Stream<String> lines = Files.lines(filePath)) {
            logStats = lines
                    .map(parser::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(logEntry -> levelFilter.equals(logEntry.getLogLevel()))
                    .collect(Collectors.groupingBy(
                            LogEntry::getServiceName,
                            Collectors.counting()
                    ));
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
            return;
        }

        if (logStats.isEmpty()) {
            System.out.println("⚠️ No logs found for level: " + targetLevel);
            return;
        }

        String mostUnstableService = LogAnalytics.getMostUnstableService(logStats);
        System.out.println("✅ Analysis Result: The service with most " + targetLevel + " logs is: " + mostUnstableService);

        LogReport logsReport = new LogReport(logStats, mostUnstableService);
        String outputFileName = targetLevel + "_analysis_report.ser";
        saveReport(logsReport, outputFileName);

        // Verify immediately
        readReportInternal(outputFileName);
    }

    // .ser file is a binary file - stores the states of the objects as it is - so you can just read it
// doesn't need to follow cycle object -> binary -> text (solves the mappings and values)(for writing) -> binary (again map the dependencies, hashmaps, etc.) -> object (for reading)
// just object -> binary (mappings stored as it is) -> object
    private static void saveReport(LogReport report, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(report);
            System.out.println("💾 Report saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }

    public static void readReportInternal(String fileName) {
        System.out.println("\n--- VERIFYING OUTPUT (" + fileName + ") ---");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            LogReport loadedReport = (LogReport) in.readObject();
            System.out.println(loadedReport);
        } catch (Exception e) {
            System.err.println("Error reading report: " + e.getMessage());
        }
    }
}
