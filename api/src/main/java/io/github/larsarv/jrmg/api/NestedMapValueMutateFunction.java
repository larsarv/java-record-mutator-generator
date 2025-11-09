package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.Map}.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * The interface is {@link FunctionalInterface} and can be used with lambda expressions
 * or method references for concise mutation composition.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 * @param <M> the type of mutator used to modify the values
 */
@FunctionalInterface
public interface NestedMapValueMutateFunction<K, V, M extends Mutator<V>> {
    /**
     * Applies a mutation operation with the provided {@link NestedValueMapMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedValueMapMutator<K,V,M> mutate(NestedValueMapMutator<K,V,M> mutator);
}