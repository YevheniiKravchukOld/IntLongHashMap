package main.java;

import main.java.util.Util;

public class EntryStorage {
    //it takes 96 bits to store key-value pair
    //32 bits for int key
    //64 bits for long value
    private final int[] entries;

    public EntryStorage() {
        entries = new int[128*3];
        initAllEntryValuesWithDefault(0xffffffffffffffffL);
    }

    public EntryStorage(long specialValue) {
        entries = new int[128*3];
        initAllEntryValuesWithDefault(specialValue);
    }

    public EntryStorage(int numberOfEntries) {
        entries = new int[numberOfEntries * 3];
        initAllEntryValuesWithDefault(0xffffffffffffffffL);
    }

    public EntryStorage(int numberOfEntries, long specialValue) {
        entries = new int[numberOfEntries * 3];
        initAllEntryValuesWithDefault(specialValue);
    }

    public long getEntryValue(int entryIndex) {
        return Util.concatIntBits(entries[entryIndex * 3 + 1], entries[entryIndex * 3 + 2]);
    }

    public int getEntryKey(int entryKeyIndex) {
        return entries[entryKeyIndex * 3];
    }

    public void putEntry(int entryIndex, int key, long value) {
        entries[entryIndex * 3] = key;
        entries[entryIndex * 3 + 1] = Util.getLongHigher32bit(value);
        entries[entryIndex * 3 + 2] = Util.getLongLower32bit(value);
    }

    private void initAllEntryValuesWithDefault(long defaultValue) {
        for(int i =0; i < entries.length; i += 3) {
            entries[i + 1] = Util.getLongHigher32bit(defaultValue);
            entries[i + 2] = Util.getLongLower32bit(defaultValue);
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

    public static void main(String[] args) {
        EntryStorage es = new EntryStorage(3, -1L);

        /*es.putEntry(0, 1, Integer.MAX_VALUE);*/
        /*es.putEntry(1, 9, (long) 12);
        es.putEntry(2, 8, (long) 12);*/

        System.out.println(es.toString());
        System.out.println(es.getEntryKey(0));
        System.out.println(es.getEntryValue(0));
        System.out.println(es.getEntryKey(1));
        System.out.println(es.getEntryValue(1));
        System.out.println(es.getEntryKey(2));
        System.out.println(es.getEntryValue(2));
    }
}