import model.LogEntry;
import model.LogReport;
import service.LogAnalytics;
import service.LogsParser;

private static final Path FILE_PATH = Paths.get("logs", "server.log");

void main() {
    LogsParser parser = new LogsParser();
    Map<String, Long> errorStats;

    System.out.println("Starting Analysis on " + FILE_PATH + "...");

    // this is a zero-memory stream for however GB of files
    try (Stream<String> lines = Files.lines(FILE_PATH)) {
        errorStats = lines
                .map(parser::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(logEntry -> "ERROR".equals(logEntry.getLogLevel()))
                .collect(Collectors.groupingBy(
                        LogEntry::getServiceName,
                        Collectors.counting()
                ));
    } catch (IOException e) {
        System.err.println("File error: " + e.getMessage());
        return;
    }

    String mostUnstableService = LogAnalytics.getMostUnstableService(errorStats);
    System.out.println("Analysis Result: The most unstable service is " + mostUnstableService);

    LogReport logReport = new LogReport(errorStats, mostUnstableService);
    saveReport(logReport);
}

// .ser file is a binary file - stores the states of the objects as it is - so you can just read it
// doesn't need to follow cycle object -> binary -> text (solves the mappings and values)(for writing) -> binary (again map the dependencies, hashmaps, etc.) -> object (for reading)
// just object -> binary (mappings stored as it is) -> object
private static void saveReport(LogReport report) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("error_analysis_report.ser"))) {
        out.writeObject(report);
        System.out.println("Report saved to error_analysis_report.ser");
    } catch (IOException e) {
        System.err.println("Error saving report to error_analysis_report.ser: " + e.getMessage());
    }
}

public static void readReportInternal() {
    System.out.println("\n--- READING BINARY FILE ---");

    // 1. Open the binary file
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("error_analysis_report.ser"))) {

        // 2. Read the raw bytes and convert them back to a Java Object
        LogReport loadedReport = (LogReport) in.readObject();

        // 3. Print the object (This uses your toString() method)
        System.out.println(loadedReport);

    } catch (Exception e) {
        System.err.println("Error reading analysis_report.ser: " + e.getMessage());
    }
}
