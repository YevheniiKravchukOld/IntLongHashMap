package main.java.probing;

import static main.java.util.AuxiliaryHashFunctionUtil.*;

// A good approach to make hash-table cacheable
public class LinearProbing implements ProbingStrategy {
    @Override
    public int hash(int key, int i, int powerOfTwoMod) {
        return (fastHash(key) + i) & (powerOfTwoMod - 1);
    }
}
