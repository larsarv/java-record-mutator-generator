package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A mutator interface for mutable maps with nested operations that involve mutators for values
 * and basic map operations.
 * <p>
 * This interface extends both {@link SimpleMapMutator} and {@link MapValueMutator},
 * providing methods for both basic map mutations and complex value mutations using mutators.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable map of the modified records.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map.
 * @param <M> the type of record mutator used to modify the values
 */
public interface NestedValueMapMutator<K, V, M extends Mutator<V>> extends SimpleMapMutator<K, V>, MapValueMutator<K, V, M> {
    @Override
    NestedValueMapMutator<K, V, M> put(K key, V value);
    @Override
    NestedValueMapMutator<K, V, M> remove(K key);
    @Override
    NestedValueMapMutator<K, V, M> filter(BiFunction<K, V, Boolean> filterFunction);
    @Override
    NestedValueMapMutator<K, V, M> update(K key, Function<V, V> mutateFunction);
    @Override
    NestedValueMapMutator<K, V, M> updateAll(BiFunction<K, V, V> mutateFunction);
    @Override
    NestedValueMapMutator<K, V, M> putAll(Map<? extends K, ? extends V> map);
    @Override
    NestedValueMapMutator<K, V, M> clear();

    @Override
    NestedValueMapMutator<K, V, M> put(K key, Function<M, M> mutateFunction);
    @Override
    NestedValueMapMutator<K, V, M> mutateValue(K key, Function<M, M> mutateFunction);
    @Override
    NestedValueMapMutator<K, V, M> mutateAllValues(BiFunction<K, M, M> mutateFunction);

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