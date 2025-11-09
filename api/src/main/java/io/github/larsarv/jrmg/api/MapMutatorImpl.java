package io.github.larsarv.jrmg.api;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A concrete implementation of map mutator interfaces that provides methods for
 * both basic map operations and complex mutations for keys and values using mutators.
 * <p>
 * This class implements {@link NestedKeyValueMapMutator}, {@link NestedKeyMapMutator} and
 * {@link NestedValueMapMutator}, which extends multiple interfaces
 * including {@link SimpleMapMutator}, {@link MapKeyMutator}, and {@link MapValueMutator}.
 *
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 * @param <MK> the type of record mutator used to modify the keys
 * @param <MV> the type of record mutator used to modify the values
 */
public class MapMutatorImpl<K, V, MK extends Mutator<K>, MV extends Mutator<V>> 
    implements NestedKeyValueMapMutator<K, V, MK, MV>, NestedKeyMapMutator<K, V, MK>, NestedValueMapMutator<K, V, MV> {
    
    private Map<K, V> map;
    private final Function<K, MK> keyMutatorFactory;
    private final Function<V, MV> valueMutatorFactory;

    private boolean locked = false;

    /**
     * Constructs a new {@code MapMutatorImpl} instance with the provided map and mutator factories.
     *
     * @param map the initial map to be mutated; if null, a new empty {@code HashMap} is created
     * @param keyMutatorFactory a function that returns a mutator for the given key
     * @param valueMutatorFactory a function that returns a mutator for the given value
     */
    public MapMutatorImpl(Map<K, V> map, Function<K, MK> keyMutatorFactory, Function<V, MV> valueMutatorFactory) {
        this.map = map == null ? new HashMap<>() : new HashMap<>(map);
        this.keyMutatorFactory = keyMutatorFactory;
        this.valueMutatorFactory = valueMutatorFactory;
    }

    /**
     * Creates a new {@code MapMutatorImpl} instance with the provided map and mutator factories.
     * <p>
     * This method is used to initialize a map mutator that can apply transformations to keys and values
     * using provided mutator factories.
     *
     * @param map the initial map to be mutated; if null, a new empty {@code HashMap} is created
     * @param keyMutatorFactory a function that returns a mutator for the given key
     * @param valueMutatorFactory a function that returns a mutator for the given value
     * @return a new {@code MapMutatorImpl} instance configured with the provided parameters
     *
     * @param <K> the type of keys in the map.
     * @param <V> the type of values in the map.
     * @param <MK> the type of record mutator used to modify the keys
     * @param <MV> the type of record mutator used to modify the values
     */
    public static <K, V, MK extends Mutator<K>, MV extends Mutator<V>> MapMutatorImpl<K, V, MK, MV> mutator(
            Map<K, V> map,
            Function<K, MK> keyMutatorFactory,
            Function<V, MV> valueMutatorFactory
    )  {
        return new MapMutatorImpl<>(map, keyMutatorFactory, valueMutatorFactory);
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
    public MapMutatorImpl<K, V, MK, MV> put(K key, V value) {
        checkLocked();
        map.put(key, value);
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> remove(K key) {
        checkLocked();
        map.remove(key);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MK, MV> filter(BiFunction<K, V, Boolean> filterFunction) {
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
    public MapMutatorImpl<K, V, MK, MV> update(K key, Function<V, V> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            V currentValue = map.get(key);
            V newValue = mutateFunction.apply(currentValue);
            map.put(key, newValue);
        }
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> updateAll(BiFunction<K, V, V> mutateFunction) {
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
    public MapMutatorImpl<K, V, MK, MV> putAll(Map<? extends K, ? extends V> m) {
        checkLocked();
        map.putAll(m);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MK, MV> clear() {
        checkLocked();
        map.clear();
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MK, MV> put(Function<MK, MK> mutateFunction, V value) {
        checkLocked();
        map.put(mutateFunction.apply(keyMutatorFactory.apply(null)).build(), value);
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> mutateKey(K key, Function<MK, MK> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            V value = map.remove(key);
            K newKey = mutateFunction.apply(keyMutatorFactory.apply(key)).build();
            map.put(newKey, value);
        }
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> mutateAllKeys(Function<MK, MK> mutateFunction) {
        checkLocked();
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K newKey = mutateFunction.apply(keyMutatorFactory.apply(entry.getKey())).build();
            newMap.put(newKey, entry.getValue());
        }
        this.map = newMap;
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> put(K key, Function<MV, MV> mutateFunction) {
        checkLocked();
        V value = mutateFunction.apply(valueMutatorFactory.apply(null)).build();
        map.put(key, value);
        return this;
    }

    @Override
    public MapMutatorImpl<K, V, MK, MV> mutateValue(K key, Function<MV, MV> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            V currentValue = map.get(key);
            V newValue = mutateFunction.apply(valueMutatorFactory.apply(currentValue)).build();
            map.put(key, newValue);
        }
        return this;
    }
    
    @Override
    public MapMutatorImpl<K, V, MK, MV> mutateAllValues(BiFunction<K, MV, MV> mutateFunction) {
        checkLocked();
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V newValue = mutateFunction.apply(entry.getKey(), valueMutatorFactory.apply(entry.getValue())).build();
            newMap.put(entry.getKey(), newValue);
        }
        this.map = newMap;
        return this;
    }

    @Override
    public NestedKeyValueMapMutator<K, V, MK, MV> put(Function<MK, MK> mutateKeyFunction, Function<MV, MV> mutateValueFunction) {
        checkLocked();
        K key = mutateKeyFunction.apply(keyMutatorFactory.apply(null)).build();
        V value = mutateValueFunction.apply(valueMutatorFactory.apply(null)).build();
        map.put(key, value);
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