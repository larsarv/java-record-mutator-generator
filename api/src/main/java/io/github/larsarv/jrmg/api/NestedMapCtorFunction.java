package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested map constructor.
 *
 * @param <KEY>     the type of keys in the map
 * @param <VALUE>   the type of values in the map
 * @param <BUILDER> the type of the builder for the values
 */
@FunctionalInterface
public interface NestedMapCtorFunction<KEY, VALUE, BUILDER> {
    /**
     * Function to implement to interact with a ctor to build a map.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the map
     */
    NestedMapCtor<KEY, VALUE, BUILDER> apply(NestedMapCtor<KEY, VALUE, BUILDER> ctor);
}