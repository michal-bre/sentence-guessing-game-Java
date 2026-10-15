package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Reads and writes the minimal number of attempts (the record) to a file.
 */
public class RecordManager {

    /** Returned by readRecord when no record exists yet. */
    public static final int NO_RECORD = Integer.MAX_VALUE;

    private final String fileName;

    public RecordManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Reads the record from the file.
     * Returns NO_RECORD if the file does not exist, is empty or is corrupted.
     */
    public int readRecord() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line == null) {
                return NO_RECORD;
            }
            return Integer.parseInt(line.trim());
        } catch (FileNotFoundException e) {
            // first game - no record yet
            return NO_RECORD;
        } catch (NumberFormatException e) {
            System.out.println("Record file is corrupted, starting a new record.");
            return NO_RECORD;
        } catch (IOException e) {
            System.out.println("Error reading record file: " + e.getMessage());
            return NO_RECORD;
        }
    }

    /**
     * Writes a new record to the file (replacing the old one).
     */
    public void saveRecord(int attempts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(attempts));
        } catch (IOException e) {
            System.out.println("Error saving record: " + e.getMessage());
        }
    }

    /**
     * Saves the attempts if they beat the current record.
     * Returns true if a new record was set.
     */
    public boolean updateIfRecord(int attempts) {
        if (attempts < readRecord()) {
            saveRecord(attempts);
            return true;
        }
        return false;
    }
}
