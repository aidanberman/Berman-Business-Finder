import java.io.*;
import java.util.*;

public class Loader {

    public static class Business implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private String name;
        private List<String> categories;

        public Business(String id, String name, List<String> categories) {
            this.id = Objects.requireNonNull(id, "ID cannot be null");
            this.name = Objects.requireNonNull(name, "Name cannot be null");
            this.categories = categories != null ? categories : new ArrayList<>();
        }

        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getCategories() { return categories; }

        @Override
        public String toString() {
            return "Business{id='" + id + "', name='" + name + "', categories=" + categories + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Business business = (Business) o;
            return Objects.equals(id, business.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public static List<Business> loadSerializedData(String directoryPath) {
        List<Business> businesses = new ArrayList<>();
        File folder = new File(directoryPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Directory does not exist: " + directoryPath);
            return businesses;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) {
            System.err.println("No files found in directory: " + directoryPath);
            return businesses;
        }

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    byte[] bytes = Base64.getDecoder().decode(line.trim());
                    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                         ObjectInput in = new ObjectInputStream(bis)) {
                        Business business = (Business) in.readObject();
                        businesses.add(business);
                    }
                }
            } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
                System.err.println("Error loading file: " + file.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("Loaded " + businesses.size() + " businesses from " + files.length + " files");
        return businesses;
    }

    public static void preCategorizeRecords(List<Business> businesses) {
        // TODO: Implement clustering algorithm
        System.out.println("Pre-categorizing " + businesses.size() + " businesses");
        // Simple category grouping for now
        Map<String, List<Business>> categoryMap = new HashMap<>();
        for (Business business : businesses) {
            for (String category : business.getCategories()) {
                categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(business);
            }
        }

        // Print category statistics
        for (Map.Entry<String, List<Business>> entry : categoryMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Category '" + entry.getKey() + "' has " + entry.getValue().size() + " businesses");
            }
        }
    }

    public static void main(String[] args) {
        String dataDirectoryPath = "data"; // Use relative path

        List<Business> businesses = loadSerializedData(dataDirectoryPath);
        preCategorizeRecords(businesses);
    }
}