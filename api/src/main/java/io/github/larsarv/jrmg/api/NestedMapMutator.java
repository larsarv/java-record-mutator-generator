package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A mutator interface for nested maps that allows fluent, chainable modifications to key-value pairs.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 */
public interface NestedMapMutator<K, V, MFP, MFR extends Builder<V>> extends SimpleMapMutator<K, V> {
    @Override
    NestedMapMutator<K, V, MFP, MFR> put(K key, V value);
    @Override
    NestedMapMutator<K, V, MFP, MFR> remove(K key);
    @Override
    NestedMapMutator<K, V, MFP, MFR> filter(BiFunction<K, V, Boolean> filterFunction);
    @Override
    NestedMapMutator<K, V, MFP, MFR> update(K key, Function<V, V> mutateFunction);
    @Override
    NestedMapMutator<K, V, MFP, MFR> updateAll(BiFunction<K, V, V> mutateFunction);
    @Override
    NestedMapMutator<K, V, MFP, MFR> putAll(Map<? extends K, ? extends V> map);
    @Override
    NestedMapMutator<K, V, MFP, MFR> clear();

    /**
     * Associates a new value with the specified key using the provided mutator function.
     * The mutator is used to construct the value before associating it with the key.
     *
     * @param key the key with which the value is to be associated
     * @param mutateFunction the mutator function used to create the value to be associated with the key
     * @return this mutator instance for method chaining
     */
    NestedMapMutator<K, V, MFP, MFR> put(K key, Function<MFP, MFR> mutateFunction);

    /**
     * Mutates the value associated with the specified key using the provided function.
     * The function receives a mutator instance for the value and returns a new mutator
     * instance with the desired mutations applied. This allows for fluent, chainable
     * modifications to the record's state.
     *
     * @param key the key of the value to be mutated
     * @param mutateFunction the function that takes a mutator for the value and returns a mutated version
     * @return a new mutator instance with the value mutated according to the provided function
     */
    NestedMapMutator<K, V, MFP, MFR> mutateValue(K key, Function<MFP, MFR> mutateFunction);

    /**
     * Mutates all values in the map using the provided function that takes both the key and a mutator.
     * The function is applied to each key-value pair in the map in order.
     *
     * @param mutateFunction the function to apply to each key-value pair, taking the key and a mutator for the value
     * @return a new mutator instance with all values mutated according to the provided function
     */
    NestedMapMutator<K, V, MFP, MFR> mutateAllValues(BiFunction<K, MFP, MFR> mutateFunction);

    /**
     * Finalizes the mutable map and returns an immutable copy.
     * <p>
     * This method creates an immutable map from the current state of the mutator.
     * Any further modifications will not affect the returned map.
     *
     * @return a map containing the final state of all entries after applying all mutations
     */
    Map<K, V> build();
}