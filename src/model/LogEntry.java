package model;

import java.time.LocalDateTime;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final String logLevel;
    private final String serviceName;
    private final String message;

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMessage() {
        return message;
    }
}
