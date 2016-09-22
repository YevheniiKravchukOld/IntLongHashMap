package main.java.storage;

import static main.java.util.BitManipulationsUtil.*;

public class LongArrayEntryStorage implements Storage {

    private final long[] entries;

    private final long specialValue;

    public LongArrayEntryStorage() {
        specialValue = 0xffffffffffffffffL;
        entries = new long[128];
    }

    public LongArrayEntryStorage(long specialValue) {
        this.specialValue = specialValue;
        entries = new long[128];
    }

    public LongArrayEntryStorage(int numberOfEntries) {
        specialValue = 0xffffffffffffffffL;
        entries = new long[(int) Math.ceil(numberOfEntries * 3 / 2.0)];
    }

    public LongArrayEntryStorage(int numberOfEntries, long specialValue) {
        this.specialValue = specialValue;
        entries = new long[(int) Math.ceil(numberOfEntries * 3 / 2.0) + 1];
    }

    public long getEntryValue(int entryIndex) {
        final double index = entryIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);

        if (index == i) {
            int hi = getLongLower32bit(entries[i]);
            int lo = getLongHigher32bit(entries[i + 1]);

            return concatIntBits(hi, lo);
        } else {
            return entries[i + 1];
        }
    }

    public int getEntryKey(int entryKeyIndex) {
        final double index = entryKeyIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);
        if(index == i) {
            return  getLongHigher32bit(entries[i]);
        } else {
            //the case, when the key is stored at lower bits of long
            return getLongLower32bit(entries[i]);
        }
    }

    public void putEntry(int entryIndex, int key, long value) {
        final double index = entryIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);

        System.out.println("Long id: " + index + "Long id: " + i);

        if (index == i) {
            final int hi = getLongHigher32bit(value);
            final int lo= getLongLower32bit(value);

            entries[i] = setLongHigher32bit(entries[i], key);
            entries[i] = setLongLower32bit(entries[i], hi);
            entries[i + 1] = setLongHigher32bit(entries[i + 1], lo);
        } else {
            entries[i] = setLongLower32bit(entries[i], key);
            entries[i+1] = value;
        }
    }

    @Override
    public int size() {
        return (int) (entries.length / 3.0 * 2);
    }

    private void initAllEntryValuesWithDefault(long defaultValue) {
        for(int i = 0; i < entries.length; i += 3) {
            entries[i + 1] = getLongHigher32bit(defaultValue);
            entries[i + 2] = getLongLower32bit(defaultValue);
        }
    }

    private void initAllEntryKeysWithDefault(int defaultValue) {
        for (int i = 0; i < entries.length; i += 3) {
            entries[i] = defaultValue;
        }
    }
}