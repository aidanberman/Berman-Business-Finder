public class HT implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    static final class Node {
        final Object key;
        Object value;
        Node next;

        Node(Object k, Object v, Node n) {
            key = k;
            value = v;
            next = n;
        }
    }

    private Node[] table = new Node[16]; // Increased initial size
    private int size = 0;

    public boolean contains(Object key) {
        if (key == null) return false;

        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }

    public void printAll() {
        for (int i = 0; i < table.length; i++) {
            System.out.print("Bucket " + i + ": ");
            for (Node e = table[i]; e != null; e = e.next) {
                System.out.print("[" + e.key + "=" + e.value + "] -> ");
            }
            System.out.println("null");
        }
    }

    public Object get(Object key) {
        if (key == null) return null;

        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e.value;
        }
        return null;
    }

    public void put(Object key, Object value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)) {
                e.value = value; // Update existing
                return;
            }
        }
        table[i] = new Node(key, value, table[i]);
        ++size;
        if ((float) size / table.length >= 0.75f)
            resize();
    }

    private void resize() {
        Node[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        Node[] newTable = new Node[newCapacity];

        for (Node node : oldTable) {
            for (Node e = node; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, e.value, newTable[j]);
            }
        }
        table = newTable;
    }

    public boolean remove(Object key) {
        if (key == null) return false;

        int h = key.hashCode();
        int i = h & (table.length - 1);
        Node e = table[i], p = null;

        while (e != null) {
            if (key.equals(e.key)) {
                if (p == null)
                    table[i] = e.next;
                else
                    p.next = e.next;
                size--;
                return true;
            }
            p = e;
            e = e.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}