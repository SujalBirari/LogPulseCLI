package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public class LogReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDateTime reportGeneratedAt;
    private final Map<String, Long> errorCountsPerService;
    private final String mostErroneousService;

    public LogReport(Map<String, Long> errorCountsPerService, String mostErroneousService) {
        this.reportGeneratedAt = LocalDateTime.now();
        this.errorCountsPerService = errorCountsPerService;
        this.mostErroneousService = mostErroneousService;
    }

    public LocalDateTime getReportGeneratedAt() {
        return reportGeneratedAt;
    }

    public Map<String, Long> getErrorCountsPerService() {
        return errorCountsPerService;
    }

    public String getMostErroneousService() {
        return mostErroneousService;
    }

    @Override
    public String toString() {
        return "LogReport{" +
                "reportGeneratedAt=" + reportGeneratedAt +
                ", errorCountsPerService=" + errorCountsPerService +
                ", mostErroneousService='" + mostErroneousService + '\'' +
                '}';
    }
}
