package io.github.larsarv.jrmg.api;

/**
 * A functional interface that represents a mutation operation to be applied to a {@link NestedMapMutator}.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
@FunctionalInterface
public interface NestedMapMutateFunction<K, V, MFP, MFR extends Builder<V>> {
    /**
     * Applies a mutation operation with the provided {@link NestedMapMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedMapMutator<K,V,MFP, MFR> mutate(NestedMapMutator<K,V,MFP, MFR> mutator);
}