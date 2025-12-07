package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple map mutator.
 *
 * @param <KEY>   the type of keys in the map
 * @param <VALUE> the type of values in the map
 */
@FunctionalInterface
public interface SimpleMapMtorFunction<KEY, VALUE> {
    /**
     * Function to implement to interact with a mtor to mutate a map.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the map
     */
    SimpleMapMtor<KEY, VALUE> apply(SimpleMapMtor<KEY, VALUE> mtor);
}