package main.java;

import main.java.util.Util;

public class EntryStorage {
    //it takes 96 bits to store key-value pair
    //32 bits for int key
    //64 bits for long value
    private final long[] entries;

    private final long specialValue;

    public EntryStorage() {
        specialValue = 0xffffffffffffffffL;
        entries = new long[128];
    }

    public EntryStorage(long specialValue) {
        this.specialValue = specialValue;
        entries = new long[128];
    }

    public EntryStorage(int numberOfEntries) {
         specialValue = 0xffffffffffffffffL;
         entries = new long[(int) Math.ceil(numberOfEntries * 3 / 2.0)];
    }

    public EntryStorage(int numberOfEntries, long specialValue) {
        this.specialValue = specialValue;
        entries = new long[(int) Math.ceil(numberOfEntries * 3 / 2.0) + 1];
    }

    public long getEntryValue(int entryIndex) {
        final double index = entryIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);

        if (index == i) {
            int hi = Util.getLongLower32bit(entries[i]);
            int lo = Util.getLongHigher32bit(entries[i + 1]);

            return Util.concatIntBits(hi, lo);
        } else {
            return entries[i + 1];
        }
    }

    public int getEntryKey(int entryKeyIndex) {
        final double index = entryKeyIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);
        if(index == i) {
            return  Util.getLongHigher32bit(entries[i]);
        } else {
            //the case, when the key is stored at lower bits of long
            return Util.getLongLower32bit(entries[i]);
        }
    }

    public void putEntry(int entryIndex, int key, long value) {
        final double index = entryIndex * 3 / 2.0;
        final int i = (int) Math.floor(index);

        System.out.println("Long id: " + index + "Long id: " + i);

        if (index == i) {
            final int hi = Util.getLongHigher32bit(value);
            final int lo= Util.getLongLower32bit(value);

            entries[i] = Util.setLongHigher32bit(entries[i], key);
            entries[i] = Util.setLongLower32bit(entries[i], hi);
            entries[i + 1] = Util.setLongHigher32bit(entries[i + 1], lo);
        } else {
            entries[i] = Util.setLongLower32bit(entries[i], key);
            entries[i+1] = value;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(long l: entries) {
            sb.append(String.format("%64s", Long.toBinaryString(l)).replace(' ', '0') + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        EntryStorage es = new EntryStorage(15);
        System.out.println(String.format("%33s", "||"));
        es.putEntry(0, 1, Integer.MAX_VALUE);
        es.putEntry(1, 2, 20);
       es.putEntry(2, 3, 30);
        es.putEntry(3, 4, 40);
        es.putEntry(4, 5, 50);
        es.putEntry(5, 6, Long.MIN_VALUE);

      /*  es.putEntry(1, 9, (long) 2);
        es.putEntry(2, 8, (long) 2);
        es.putEntry(3, 7, (long) 2);
        es.putEntry(4, -6, (long) 2);*/
        /*for(int i = 0; i < 20; i++) {
            System.out.println(Util.fastMod3(i));
        }*/
        System.out.println(es.toString());
        /*System.out.println(es.getEntryKey(0));
        System.out.println(es.getEntryKey(1));
        System.out.println(es.getEntryKey(2));
        System.out.println(es.getEntryKey(3));
        System.out.println(es.getEntryKey(4));
        System.out.println(es.getEntryKey(5));*/
        EntryStorage es2 = new EntryStorage(3);

        System.out.println(String.format("%33s", "||"));
        System.out.println(es2.toString());
        System.out.println(es2.entries.length);
    }
}