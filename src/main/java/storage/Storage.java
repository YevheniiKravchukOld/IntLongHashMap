package main.java.storage;

public interface Storage {
    int SPECIAL_KEY = Integer.MIN_VALUE;
    long SPECIAL_VALUE = Long.MIN_VALUE;

    long getEntryValue(int entryIndex);
    int getEntryKey(int entryIndex);
    void putEntry(int entryIndex, int key, long value);
    int size();
}
