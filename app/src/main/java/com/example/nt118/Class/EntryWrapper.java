package com.example.nt118.Class;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

import java.util.Map;
import java.util.Objects;

public class EntryWrapper<K, V> {
    private K key;
    private V value;

    public EntryWrapper(Map.Entry<K, V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    // Getter và setter
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
