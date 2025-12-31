package model;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String logLevel;
    private String serviceName;
    private String message;

    public LogEntry(LocalDateTime timestamp, String logLevel, String serviceName, String message) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.serviceName = serviceName;
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", logLevel='" + logLevel + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
