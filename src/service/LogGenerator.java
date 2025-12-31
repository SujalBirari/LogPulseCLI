package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogGenerator {
    private static final int LOG_COUNT = 1000;
    private static final String LOG_DIR = "logs";
    private static final String FILE_NAME = "server.log";

    private static final String[] LEVELS = {"INFO", "WARNING", "ERROR"};
    private static final String[] SERVICES = {"UserService", "PaymentService", "AuthService", "OrderService"};
    private static final String[] MESSAGES = {
            "User login successful",
            "Database connection timeout",
            "Payment proceeded",
            "Invalid credentials",
            "Cache miss",
            "Disk space low"
    };

    static void main() {
        Path logsPath = Paths.get(LOG_DIR);
        Path filePath = logsPath.resolve(FILE_NAME);
        Random random = new Random();

        try {
            Files.createDirectories(logsPath);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                System.out.println("Generating " + LOG_COUNT + " logs...");

                for (int i = 0; i < LOG_COUNT; i++) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String timestamp = now.format(formatter);

                    String level = LEVELS[random.nextInt(LEVELS.length)];
                    String service = SERVICES[random.nextInt(SERVICES.length)];
                    String message = MESSAGES[random.nextInt(MESSAGES.length)];

                    String logLine = String.format("[%s] %s [%s] %s", timestamp, level, service, message);

                    writer.write(logLine);
                    writer.newLine();
                }

                System.out.println("Done! Created " + FILE_NAME);
            }
        } catch (IOException e) {
            System.err.println("Failed to write logs: " + e.getMessage());
        }
    }
}
