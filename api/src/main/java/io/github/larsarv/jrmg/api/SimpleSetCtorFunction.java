package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple set constructor.
 *
 * @param <ELEMENT> the type of elements in the set
 */
@FunctionalInterface
public interface SimpleSetCtorFunction<ELEMENT> {
    /**
     * Function to implement to interact with a ctor to build a set.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the set
     */
    SimpleSetCtor<ELEMENT> apply(SimpleSetCtor<ELEMENT> ctor);
}
