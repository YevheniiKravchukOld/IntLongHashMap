package main.java;

import main.java.probing.DoubleHashing;
import main.java.probing.ProbingStrategy;
import main.java.storage.IntArrayEntryStorage;
import main.java.storage.Storage;

import static main.java.util.Util.*;

public class PrimitiveIntLongHashMap implements IPrimitiveIntLongHashMap {

    private static final int SPECIAL_KEY = Integer.MIN_VALUE;

    private static final long SPECIAL_VALUE = Long.MIN_VALUE;

    private static final int DEFAULT_SIZE = 128;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //A value to indicate free entry's key
    private final int specialKey;

    //A value to indicate NIL-value
    private final long specialValue;

    private Storage entries;

    private final ProbingStrategy probingStrategy;

    private final float loadFactor;

    //threshold
    private int capacity;

    //Number of key-value pairs
    private int size;

    public PrimitiveIntLongHashMap() {
        this(
                DEFAULT_LOAD_FACTOR,

                new IntArrayEntryStorage(nextPowerOfTwo((int) (DEFAULT_SIZE / DEFAULT_LOAD_FACTOR + 1)),
                        SPECIAL_KEY,
                        SPECIAL_VALUE),

                new DoubleHashing()
        );
    }

    public PrimitiveIntLongHashMap(
            float loadFactor,
            Storage es,
            ProbingStrategy ps) {

        this(loadFactor, es, ps, SPECIAL_KEY, SPECIAL_VALUE);
    }

    public PrimitiveIntLongHashMap(
            float loadFactor,
            Storage es,
            ProbingStrategy ps,
            int specialKey,
            long specialValue) {

        if (loadFactor <= 0 || loadFactor >= 1)
            throw new IllegalArgumentException("Wrong fill factor");
        final int capacity = nextPowerOfTwo((int) (es.size() * loadFactor + 1));
        if (capacity > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Unable to create such a big map");
        entries = es;
        probingStrategy = ps;
        this.loadFactor = loadFactor;
        this.specialKey = specialKey;
        this.specialValue = specialValue;
        this.capacity = capacity;
    }

    @Override
    public long get(int key) {
        int i = 0;
        int hash = probingStrategy.hash(key, i, capacity);
        while (entries.getEntryKey(hash) != specialKey) {
            if (entries.getEntryKey(hash) == key) return entries.getEntryValue(hash);
            ++i;
            hash = probingStrategy.hash(key, i, capacity);
        }
        return specialValue;
    }

    @Override
    public void put(int key, long value) {
        if (key == specialKey)
            throw new IllegalArgumentException("Key must not be equal to " + specialKey);
        if (value == specialValue)
            throw new IllegalArgumentException("Value must not be equal to " + specialValue);
        int i = 0;
        int hash = probingStrategy.hash(key, i, capacity);

        while (entries.getEntryKey(hash) != specialKey) {
            //insertion in non-free bucket (overriding)
            if (entries.getEntryKey(hash) == key) {
                entries.putEntry(hash, key, value);
                return;
            }
            ++i;
            hash = probingStrategy.hash(key, i, capacity);
        }

        if (size >= capacity) {
            resize();
            put(key, value);
            return;
        }
        ++size;
        entries.putEntry(hash, key, value);
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        final int newCapacity = nextPowerOfTwo(capacity * 2);
        capacity = newCapacity;
        Storage oldStorage = entries;
        try {
            Class[] c = {int.class, int.class, long.class};
            entries = entries.getClass().getConstructor(c).newInstance(newCapacity, specialKey, specialValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int oldStorageSize = oldStorage.size();
        size = 0;
        for (int i = 0; i < oldStorageSize; i++) {
            if (oldStorage.getEntryKey(i) != specialKey) {
                this.put(oldStorage.getEntryKey(i), oldStorage.getEntryValue(i));
            }
        }
    }
}