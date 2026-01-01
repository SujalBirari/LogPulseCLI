package service;

import model.LogEntry;

import java.util.*;
import java.util.stream.Collectors;

public class LogAnalytics {
    private final List<LogEntry> logEntries;

    public LogAnalytics(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    // 1. Storing service logs and their count
//    public Map<String, Integer> getServiceLogsCount() {
//        Map<String, Integer> serviceLogsCount = new HashMap<>();
//        for (LogEntry logEntry : logEntries) {
//            serviceLogsCount.put(logEntry.getServiceName(), serviceLogsCount.getOrDefault(logEntry.getServiceName(), 0) + 1);
//        }
//
//        return serviceLogsCount;
//    }

    public Map<String, Long> getServiceLogsCount() {
        return logEntries.stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getServiceName,
                        Collectors.counting()
                ));
    }

    // 2. Get only error logs
//    public List<LogEntry> getErrorLogEntries() {
//        List<LogEntry> errorLogEntries = new ArrayList<>();
//        for (LogEntry logEntry : logEntries) {
//            if  ("ERROR".equals(logEntry.getLogLevel())) {
//                errorLogEntries.add(logEntry);
//            }
//        }
//
//        return errorLogEntries;
//    }

    public List<LogEntry> getErrorLogEntries() {
        return logEntries.stream()
                .filter(logEntry -> "ERROR".equals(logEntry.getLogLevel()))
                .collect(Collectors.toList());
    }

    // 3. Grouping logs by service name
//    public List<LogEntry> getServiceLogEntries(String serviceName) {
//        ArrayList<LogEntry> serviceLogEntries = new ArrayList<>();
//        for (LogEntry logEntry : logEntries) {
//            if (serviceName.equals(logEntry.getServiceName())) {
//                serviceLogEntries.add(logEntry);
//            }
//        }
//
//        return serviceLogEntries;
//    }

    public List<LogEntry> getServiceLogEntries(String serviceName) {
        return logEntries.stream()
                .filter(logEntry -> serviceName.equals(logEntry.getServiceName()))
                .collect(Collectors.toList());
    }

    // 4. Which service produced the most error logs
//    public String getErroneousService() {
//        Map<String, Integer> serviceErrorLogsCount = new HashMap<>();
//        int maxCount = 0;
//        String mostErroneousService = "";
//
//        for  (LogEntry logEntry : logEntries) {
//            if ("ERROR".equals(logEntry.getLogLevel())) {
//                serviceErrorLogsCount.put(logEntry.getServiceName(), serviceErrorLogsCount.getOrDefault(logEntry.getServiceName(), 0) + 1);
//            }
//        }
//
//        for (String serviceName : serviceErrorLogsCount.keySet()) {
//            if (serviceErrorLogsCount.get(serviceName) > maxCount) {
//                maxCount = serviceErrorLogsCount.get(serviceName);
//                mostErroneousService = serviceName;
//            }
//        }
//
//        if (!Objects.equals(mostErroneousService, ""))  {
//            return mostErroneousService;
//        }
//
//        return "Error!!!";
//    }

    public Optional<String> getErroneousService() {
        return logEntries.stream()                                                           // Stream all logs
                .filter(logEntry -> "ERROR".equals(logEntry.getLogLevel()))         // Filter error logs
                .collect(Collectors.groupingBy(                                             // Group them by service and count them -> gives a map
                        LogEntry::getServiceName,
                        Collectors.counting()))
                .entrySet()                                                                 // Converts map to set
                .stream()                                                                   // Convert set of key-value pairs to stream
                .max(Map.Entry.comparingByValue())                                         // Find service with the highest count
                .map(Map.Entry::getKey);                                                   // Get the service name
    }

    // Map<String, Long> is not streamable
    // So .collect(...).entrySet() gives a Set -> Set<Map<String, Long>>
    // Map.Entry entry -> entry.getKey(), entry.getValue()
}
