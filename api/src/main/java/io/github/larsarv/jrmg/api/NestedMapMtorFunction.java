package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested map mutator.
 *
 * @param <KEY>     the type of keys in the map
 * @param <VALUE>   the type of values in the map
 * @param <BUILDER> the type of the builder for the values
 */
@FunctionalInterface
public interface NestedMapMtorFunction<KEY, VALUE, BUILDER> {
    /**
     * Function to implement to interact with a mtor to mutate a map.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the map
     */
    NestedMapMtor<KEY, VALUE, BUILDER> apply(NestedMapMtor<KEY, VALUE, BUILDER> mtor);
}