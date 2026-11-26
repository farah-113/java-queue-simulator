package logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationLogger {
    private final List<String> logLines;
    private final String filePath;

    public SimulationLogger(String filePath) {
        this.logLines = new ArrayList<>();
        this.filePath = filePath;
    }

    public synchronized void log(String line) {
        logLines.add(line);
    }

    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : logLines) {
                writer.write(line);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    }
}