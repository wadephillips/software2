package calendar.helpers;

/**
 * A helper class for building an object that contains a numeric id as the key and a String as the value
 */
public class KeyValuePair {

    /**
     * The object's id
     */
    private final long key;

    /**
     * The object's String Value
     */
    private final String value;

    /**
     * The constructor
     * @param key the id
     * @param value a string
     */
    public KeyValuePair(long key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the object's id
     *
     * @return the id
     */
    public long getKey() {
        return key;
    }

    /**
     * Gets the String value of the object
     */
    @Override
    public String toString() {
        return value;
    }

}
