package io.github.larsarv.jrmg.api;

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