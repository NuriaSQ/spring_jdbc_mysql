package com.ra2.Logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class CustomLogging {

    private static final String LOG_FILE = "application.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //Creació i directori per guardar l'arxiu amb els logs a la carpeta logs
    private Path getLogFilePath() {
        try {
            Path logDir = Paths.get("logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            String fileName = "aplicacio-" + dayFormatter.format(LocalDate.now()) + ".log";
            return logDir.resolve(fileName);
        } catch (IOException e) {
            System.err.println("ERROR creant fitxer de log: " + e.getMessage());
            return Paths.get(LOG_FILE);
        }
    }

    //Funcio que s'encarrega de gestionar o crear els logs relacionats amb errors
    public void logError(String className, String methodName, String errorMsg, Exception exception) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[ERROR] %s - Class: %s - Method: %s - Message: %s",
                timestamp, className, methodName, errorMsg);
        if (exception != null) logEntry += " - Exception: " + exception.getMessage();
        writeToFile(logEntry);
        System.out.println(logEntry);
    }

    //Funció que s'encarrega de gestionar o crear els logs relacionats amb informació
    public void logInfo(String className, String methodName, String infoMsg) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[INFO] %s - Class: %s - Method: %s - Message: %s",
                timestamp, className, methodName, infoMsg);
        writeToFile(logEntry);
        System.out.println(logEntry);
    }

    //Funcio que escriu la informació al log prèviament creat
    private void writeToFile(String message) {
        Path logPath = getLogFilePath();
        try {
            Files.createDirectories(logPath.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(
                    logPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                bw.write(message);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("ERROR escrivint al fitxer de log: " + e.getMessage());
        }
    }
}

