package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple list constructor.
 *
 * @param <ELEMENT> the type of elements in the list
 */
@FunctionalInterface
public interface SimpleListCtorFunction<ELEMENT> {
    /**
     * Function to implement to interact with a ctor to build a list.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the list
     */
    SimpleListCtor<ELEMENT> apply(SimpleListCtor<ELEMENT> ctor);
}
