# 🚀 LogPulse CLI

**A High-Performance, Memory-Efficient Log Analysis Tool built with Java Core.**

LogPulse is a command-line interface (CLI) tool designed to ingest, parse, and analyze massive server log files. It uses a **Stream-based architecture** to process files larger than available RAM (e.g., analyzing a 10GB log file on a machine with 512MB RAM) without triggering `OutOfMemoryError`.

---

## ⚡ Key Features

* **Zero-Memory Ingestion:** Uses `java.nio.file.Files.lines()` to process logs lazily (line-by-line) rather than loading the entire file into memory.
* **Robust Parsing:** Regex-based parser with `Optional` handling to gracefully skip corrupt log lines without crashing.
* **Statistical Analysis:** Uses Java 8 Stream API (`filter`, `map`, `groupingBy`) to aggregate error counts and identify unstable services.
* **State Persistence:** Serializes analysis results into binary `.ser` files using `ObjectOutputStream` for efficient storage and retrieval.
* **CLI flexibility:** Supports command-line arguments to filter by specific Log Levels (`INFO`, `WARN`, `ERROR`) or target specific files.

---

## 🛠️ Technical Highlights (The "Why")

This project was built to demonstrate mastery of **Java Core** and **JVM Memory Management**.

| Challenge | Solution |
| :--- | :--- |
| **Memory Constraints** | Instead of `readAllLines()` (which creates a huge `List`), I used a `Stream<String>` pipeline. This allows the JVM to Garbage Collect processed lines immediately. |
| **Data Integrity** | Custom `LogParseException` and `Optional<LogEntry>` ensure the pipeline is fault-tolerant against bad data. |
| **Performance** | O(N) complexity for a single pass through the file using Functional Programming concepts. |

---

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher.

### 1. Generate Dummy Data
Navigate to the `src` directory
Create a server.log file (default is 2 million lines, ~120MB) to test the performance
```bash
java service/LogGenerator.java
```

### 2. Run the Analyzer 
```bash
java Main.java
```

### Run with Custom Arguments
```
# Analyze WARN logs
java Main.java --level WARN

# Analyze a specific file
java Main.java logs/backup.log --level INFO
```

### The "Stress Test" (Proof of Concept)
To prove the memory efficiency, run the application with a restricted Heap size of 64MB against a 100MB+ log file.
```
java -Xmx64m Main.java
```

## 👤 Author
**Sujal Birari**
*Core Java Project focusing on High-Performance I/O and Functional Programming.*
