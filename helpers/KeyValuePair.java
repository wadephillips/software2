package calendar.helpers;

public class KeyValuePair {
    private final long key;

    private final String value;

    public KeyValuePair(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return value;
    }

}
