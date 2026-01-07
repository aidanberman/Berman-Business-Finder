import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    private static final JSONParser jsonParser = new JSONParser();

    public static List<JSONObject> readJsonFile(String filePath, int maxRecords) throws IOException, ParseException {
        List<JSONObject> records = new ArrayList<>();

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int recordsRead = 0;

            while ((line = br.readLine()) != null && recordsRead < maxRecords) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                try {
                    Object obj = jsonParser.parse(line);
                    if (obj instanceof JSONObject) {
                        records.add((JSONObject) obj);
                        recordsRead++;

                        if (recordsRead % 1000 == 0) {
                            System.out.println("Loaded " + recordsRead + " records...");
                        }
                    }
                } catch (ParseException e) {
                    System.err.println("Failed to parse line " + (recordsRead + 1) + ": " + e.getMessage());
                    // Continue with next line instead of failing completely
                }
            }
        }

        System.out.println("Successfully loaded " + records.size() + " records from " + filePath);
        return records;
    }

    public static List<JSONObject> readJsonFile(String filePath) throws IOException, ParseException {
        return readJsonFile(filePath, Integer.MAX_VALUE);
    }
}