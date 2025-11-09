package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A mutator interface for mutable maps with nested operations that involve mutators for both keys and values
 * and basic map operations.
 * <p>
 * This interface extends {@link SimpleMapMutator}, {@link MapKeyMutator}, and {@link MapValueMutator},
 * providing methods for basic map mutations, complex key mutations using mutators, and complex value mutations using mutators.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable map of the modified records.
 *
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 * @param <MK> the type of record mutator used to modify the keys
 * @param <MV> the type of record mutator used to modify the values
 */
public interface NestedKeyValueMapMutator<K, V, MK extends Mutator<K>, MV extends Mutator<V>> extends
        SimpleMapMutator<K, V>, MapKeyMutator<K, V, MK>, MapValueMutator<K, V, MV> {
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> put(K key, V value);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> remove(K key);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> filter(BiFunction<K, V, Boolean> filterFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> update(K key, Function<V, V> mutateFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> updateAll(BiFunction<K, V, V> mutateFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> putAll(Map<? extends K, ? extends V> map);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> clear();

    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> put(Function<MK, MK> mutateFunction, V value);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> mutateKey(K key, Function<MK, MK> mutateFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> mutateAllKeys(Function<MK, MK> mutateFunction);

    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> put(K key, Function<MV, MV> mutateFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> mutateValue(K key, Function<MV, MV> mutateFunction);
    @Override
    NestedKeyValueMapMutator<K, V, MK, MV> mutateAllValues(BiFunction<K, MV, MV> mutateFunction);

    /**
     * Associates a new key with a value.
     *
     * @param mutateKeyFunction a function that takes a mutator for the key and returns a mutated version
     * @param mutateFunction a function that takes a mutator for the value and returns a mutated version
     * @return this mutator instance for method chaining
     */
    NestedKeyValueMapMutator<K, V, MK, MV> put(Function<MK, MK> mutateKeyFunction, Function<MV, MV> mutateFunction);

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