package main.java.storage;

import static main.java.util.BitManipulationsUtil.*;

public class IntArrayEntryStorage implements Storage{

    private static final int DEFAULT_ARRAY_SIZE = 384;

    private final int[] entries;

    public IntArrayEntryStorage() {
        entries = new int[DEFAULT_ARRAY_SIZE];
        initAllEntryKeysWithDefault(SPECIAL_KEY);
        initAllEntryValuesWithDefault(SPECIAL_VALUE);
    }

    public IntArrayEntryStorage(int numberOfEntries) {
        entries = new int[numberOfEntries * 3];
        initAllEntryKeysWithDefault(SPECIAL_KEY);
        initAllEntryValuesWithDefault(SPECIAL_VALUE);
    }

    public IntArrayEntryStorage(int numberOfEntries, int specialKey, long specialValue) {
        entries = new int[numberOfEntries * 3];
        initAllEntryKeysWithDefault(specialKey);
        initAllEntryValuesWithDefault(specialValue);
    }

    public long getEntryValue(int entryIndex) {
        return concatIntBits(entries[entryIndex * 3 + 1], entries[entryIndex * 3 + 2]);
    }

    public int getEntryKey(int entryIndex) {
        return entries[entryIndex * 3];
    }

    public void putEntry(int entryIndex, int key, long value) {
        entries[entryIndex * 3] = key;
        entries[entryIndex * 3 + 1] = getLongHigher32bit(value);
        entries[entryIndex * 3 + 2] = getLongLower32bit(value);
    }

    public int size() {
        return entries.length / 3;
    }

    private void initAllEntryValuesWithDefault(long defaultValue) {
        for(int i = 0; i < entries.length; i += 3) {
            entries[i + 1] = getLongHigher32bit(defaultValue);
            entries[i + 2] = getLongLower32bit(defaultValue);
        }
    }

    private void initAllEntryKeysWithDefault(int defaultValue) {
        for(int i =0; i < entries.length; i += 3) {
            entries[i] = defaultValue;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int l: entries) {
            sb.append(String.format("%32s", Integer.toBinaryString(l)).replace(' ', '0') + "\n");
        }
        return sb.toString();
    }

    public String toStringOfIntegerKeyValuePairs(){
        StringBuilder sb = new StringBuilder();
        for(int l = 0; l < entries.length; l += 3) {
            sb.append("Key: " + entries[l] + " Value: " + concatIntBits(entries[l+1], entries[l+2]) + "\n");
        }
        System.out.println(entries.length);
        return sb.toString();
    }
}