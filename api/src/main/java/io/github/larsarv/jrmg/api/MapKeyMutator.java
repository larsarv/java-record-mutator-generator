package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.function.Function;

/**
 * A mutator interface for mutable maps with nested operations that involve mutators for keys.
 * <p>
 * This interface provides methods to manipulate map keys using {@link Mutator} instances,
 * enabling more complex transformations that involve mutating the internal state of keys.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable map of the modified records.
 *
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map
 * @param <M> the type of record mutator used to modify the keys
 */
public interface MapKeyMutator<K, V, M extends Mutator<K>> {
    /**
     * Associates the specified value with a new key generated using the provided mutator function.
     * The mutator is used to construct the key before associating it with the value.
     *
     * @param mutateFunction the mutator function used to create the key to be associated with the value
     * @param value the value to be associated with the generated key
     * @return this mutator instance for method chaining
     */
    MapKeyMutator<K, V, M> put(Function<M, M> mutateFunction, V value);

    /**
     * Mutates the key associated with the specified value using the provided function.
     * The function receives a mutator instance for the key and returns a new mutator
     * instance with the desired mutations applied. This allows for fluent, chainable
     * modifications to the key's state.
     *
     * @param key the key to be mutated
     * @param mutateFunction the function that takes a mutator for the key and returns a mutated version
     * @return a new mutator instance with the key mutated according to the provided function
     */
    MapKeyMutator<K, V, M> mutateKey(K key, Function<M, M> mutateFunction);

    /**
     * Mutates all keys in the map using the provided function.
     * The function is applied to each key in the map in order.
     *
     * @param mutateFunction the function to apply to each key
     * @return a new mutator instance with all keys mutated according to the provided function
     */
    MapKeyMutator<K, V, M> mutateAllKeys(Function<M, M> mutateFunction);

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