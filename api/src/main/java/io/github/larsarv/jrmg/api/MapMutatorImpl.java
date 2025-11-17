package io.github.larsarv.jrmg.api;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A mutable map implementation that allows transformation of values using mutators.
 * This class provides a fluent API for mutating map entries while preserving immutability upon build.
 *
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 *
 * @see NestedMapMutator
 */
public class MapMutatorImpl<K, V, MFP, MFR extends Builder<V>> implements NestedMapMutator<K, V, MFP, MFR> {
    private Map<K, V> map;
    private final Function<V, MFP> mutatorFactory;

    private boolean locked = false;

    /**
     * Constructs a new {@code MapMutatorImpl} instance with the provided map and mutator factories.
     *
     * @param map the initial map to be mutated; if null, a new empty {@code HashMap} is created
     * @param valueMutatorFactory a function that returns a mutator for the given value
     */
    public MapMutatorImpl(Map<K, V> map, Function<V, MFP> valueMutatorFactory) {
        this.map = map == null ? new HashMap<>() : new HashMap<>(map);
        this.mutatorFactory = valueMutatorFactory;
    }

    /**
     * Creates a new {@code MapMutatorImpl} instance with the provided map and mutator factories.
     * <p>
     * This method is used to initialize a map mutator that can apply transformations to keys and values
     * using provided mutator factories.
     *
     * @param map the initial map to be mutated; if null, a new empty {@code HashMap} is created
     * @param valueMutatorFactory a function that returns a mutator for the given value
     * @return a new {@code MapMutatorImpl} instance configured with the provided parameters
     *
     * @param <K> the type of keys in the map.
     * @param <V> the type of values in the map.
     * @param <MFP> the type of the mutate function parameter.
     * @param <MFR> the return type mutate function.
     */
    public static <K, V, MFP, MFR extends Builder<V>> MapMutatorImpl<K, V, MFP, MFR> mutator(
            Map<K, V> map,
            Function<V, MFP> valueMutatorFactory
    )  {
        return new MapMutatorImpl<>(map, valueMutatorFactory);
    }

    private void checkLocked() {
        if (locked) {
            throw new IllegalStateException("Map is locked and can not be modified");
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }
    
    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }
    
    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(map.values());
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> put(K key, V value) {
        checkLocked();
        map.put(key, value);
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> remove(K key) {
        checkLocked();
        map.remove(key);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MFP, MFR> filter(BiFunction<K, V, Boolean> filterFunction) {
        checkLocked();
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (filterFunction.apply(entry.getKey(), entry.getValue())) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        this.map = newMap;
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> update(K key, Function<V, V> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            V currentValue = map.get(key);
            V newValue = mutateFunction.apply(currentValue);
            map.put(key, newValue);
        }
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> updateAll(BiFunction<K, V, V> mutateFunction) {
        checkLocked();
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V newValue = mutateFunction.apply(entry.getKey(), entry.getValue());
            newMap.put(entry.getKey(), newValue);
        }
        this.map = newMap;
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> putAll(Map<? extends K, ? extends V> m) {
        checkLocked();
        map.putAll(m);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MFP, MFR> clear() {
        checkLocked();
        map.clear();
        return this;
    }


    @Override
    public MapMutatorImpl<K, V, MFP, MFR> put(K key, Function<MFP, MFR> mutateFunction) {
        checkLocked();
        V value = mutateFunction.apply(mutatorFactory.apply(null)).build();
        map.put(key, value);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MFP, MFR> mutateValue(K key, Function<MFP, MFR> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            V currentValue = map.get(key);
            V newValue = mutateFunction.apply(mutatorFactory.apply(currentValue)).build();
            map.put(key, newValue);
        }
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MFP, MFR> mutateAllValues(BiFunction<K, MFP, MFR> mutateFunction) {
        checkLocked();
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V newValue = mutateFunction.apply(entry.getKey(), mutatorFactory.apply(entry.getValue())).build();
            newMap.put(entry.getKey(), newValue);
        }
        this.map = newMap;
        return this;
    }

    @Override
    public Map<K, V> build() {
        locked = true;
        return Collections.unmodifiableMap(map);
    }
    
    @Override
    public Map<K, V> buildCopy() {
        return Map.copyOf(map);
    }
}