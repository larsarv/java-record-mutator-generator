package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple map constructor.
 *
 * @param <KEY>   the type of keys in the map
 * @param <VALUE> the type of values in the map
 */
@FunctionalInterface
public interface SimpleMapCtorFunction<KEY, VALUE> {
    /**
     * Function to implement to interact with a ctor to build a map.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the map
     */
    SimpleMapCtor<KEY, VALUE> apply(SimpleMapCtor<KEY, VALUE> ctor);
}