package io.github.larsarv.jrmg.api;

import java.util.Map;

/**
 * A functional interface that defines a mutation operation on a {@link Map}.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * The interface is {@link FunctionalInterface} and can be used with lambda expressions
 * or method references for concise mutation composition.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 * @param <MK> the type of mutator used to modify the keys
 * @param <MV> the type of mutator used to modify the values
 */
@FunctionalInterface
public interface NestedMapKeyValueMutateFunction<K, V, MK extends Mutator<K>, MV extends Mutator<V>> {
    /**
     * Applies a mutation operation with the provided {@link NestedKeyValueMapMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedKeyValueMapMutator<K,V,MK,MV> mutate(NestedKeyValueMapMutator<K,V,MK,MV> mutator);

}