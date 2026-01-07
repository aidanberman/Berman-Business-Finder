import java.util.Map;

public class FrequencyTable implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    static final class Node {
        final String key;
        int count;
        Node next;

        Node(String k, Node n) {
            key = k;
            count = 1;
            next = n;
        }
    }

    private Node[] table;
    private int size;

    public FrequencyTable() {
        table = new Node[16]; // Increased initial capacity
        size = 0;
    }

    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int h = key.hashCode();
        int i = h & (table.length - 1);

        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)) {
                e.count++;
                return;
            }
        }

        table[i] = new Node(key, table[i]);
        ++size;
        if ((float) size / table.length >= 0.75f) {
            resize();
        }
    }

    private void resize() {
        Node[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        Node[] newTable = new Node[newCapacity];

        for (Node node : oldTable) {
            for (Node e = node; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, newTable[j]);
                newTable[j].count = e.count; // Preserve count
            }
        }
        table = newTable;
    }

    public Map<String, Integer> getFrequencyTable() {
        Map<String, Integer> frequencyMap = new java.util.HashMap<>();
        for (Node node : table) {
            for (Node e = node; e != null; e = e.next) {
                frequencyMap.put(e.key, e.count);
            }
        }
        return frequencyMap;
    }

    public int getSize() {
        return size;
    }

    public int getCount(String key) {
        if (key == null) return 0;

        int h = key.hashCode();
        int i = h & (table.length - 1);

        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)) {
                return e.count;
            }
        }
        return 0;
    }
}