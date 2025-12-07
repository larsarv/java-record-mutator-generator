package io.github.larsarv.jrmg.api;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of {@link NestedMapMtor} and {@link NestedMapCtor}.
 * <p>
 * This class provides the logic for mutating and constructing maps, including support for nested builders.
 *
 * @param <KEY>     the type of keys in the map
 * @param <VALUE>   the type of values in the map
 * @param <BUILDER> the type of the builder for the values
 */
public class MapBuilderImpl<KEY, VALUE, BUILDER> implements NestedMapMtor<KEY, VALUE, BUILDER>, NestedMapCtor<KEY, VALUE, BUILDER> {
    private final Function<VALUE, BUILDER> valueBuilderFactory;
    private Map<KEY, VALUE> map;
    private boolean locked = false;

    /**
     * Constructs a new MapBuilderImpl.
     *
     * @param map                 the initial map (can be null)
     * @param valueBuilderFactory a factory function to create builders for values
     */
    public MapBuilderImpl(Map<KEY, VALUE> map, Function<VALUE, BUILDER> valueBuilderFactory) {
        this.map = map == null ? new HashMap<>() : new HashMap<>(map);
        this.valueBuilderFactory = valueBuilderFactory;
    }

    /**
     * Creates a new builder instance.
     *
     * @param map                 the initial map
     * @param valueMutatorFactory a factory function to create builders for values
     * @param <K>                 the type of keys
     * @param <V>                 the type of values
     * @param <B>                 the type of the builder
     * @return a new MapBuilderImpl instance
     */
    public static <K, V, B> MapBuilderImpl<K, V, B> builder(
            Map<K, V> map,
            Function<V, B> valueMutatorFactory
    ) {
        return new MapBuilderImpl<>(map, valueMutatorFactory);
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
    public VALUE get(KEY key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(KEY key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(VALUE value) {
        return map.containsValue(value);
    }

    @Override
    public Set<KEY> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Collection<VALUE> values() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> put(KEY key, VALUE value) {
        checkLocked();
        map.put(key, value);
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> remove(KEY key) {
        checkLocked();
        map.remove(key);
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> filter(BiFunction<KEY, VALUE, Boolean> filterFunction) {
        checkLocked();
        Map<KEY, VALUE> newMap = new HashMap<>();
        for (Map.Entry<KEY, VALUE> entry : map.entrySet()) {
            if (filterFunction.apply(entry.getKey(), entry.getValue())) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        this.map = newMap;
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> update(KEY key, Function<VALUE, VALUE> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            VALUE currentValue = map.get(key);
            VALUE newValue = mutateFunction.apply(currentValue);
            map.put(key, newValue);
        }
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> updateAll(BiFunction<KEY, VALUE, VALUE> mutateFunction) {
        checkLocked();
        Map<KEY, VALUE> newMap = new HashMap<>();
        for (Map.Entry<KEY, VALUE> entry : map.entrySet()) {
            VALUE newValue = mutateFunction.apply(entry.getKey(), entry.getValue());
            newMap.put(entry.getKey(), newValue);
        }
        this.map = newMap;
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> putAll(Map<? extends KEY, ? extends VALUE> m) {
        checkLocked();
        map.putAll(m);
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> clear() {
        checkLocked();
        map.clear();
        return this;
    }


    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> put(KEY key, Function<BUILDER, Builder<VALUE>> mutateFunction) {
        checkLocked();
        VALUE value = mutateFunction.apply(valueBuilderFactory.apply(null)).build();
        map.put(key, value);
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> mutateValue(KEY key, Function<BUILDER, Builder<VALUE>> mutateFunction) {
        checkLocked();
        if (map.containsKey(key)) {
            VALUE currentValue = map.get(key);
            VALUE newValue = mutateFunction.apply(valueBuilderFactory.apply(currentValue)).build();
            map.put(key, newValue);
        }
        return this;
    }

    @Override
    public MapBuilderImpl<KEY, VALUE, BUILDER> mutateAllValues(BiFunction<KEY, BUILDER, Builder<VALUE>> mutateFunction) {
        checkLocked();
        Map<KEY, VALUE> newMap = new HashMap<>();
        for (Map.Entry<KEY, VALUE> entry : map.entrySet()) {
            VALUE newValue = mutateFunction.apply(entry.getKey(), valueBuilderFactory.apply(entry.getValue())).build();
            newMap.put(entry.getKey(), newValue);
        }
        this.map = newMap;
        return this;
    }

    @Override
    public Map<KEY, VALUE> build() {
        locked = true;
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Map<KEY, VALUE> buildCopy() {
        return Map.copyOf(map);
    }
}