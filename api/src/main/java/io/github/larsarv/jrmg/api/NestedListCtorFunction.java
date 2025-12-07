package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested list constructor.
 *
 * @param <ELEMENT> the type of elements in the list
 * @param <BUILDER> the type of the builder for the elements
 */
@FunctionalInterface
public interface NestedListCtorFunction<ELEMENT, BUILDER> {
    /**
     * Function to implement to interact with a ctor to build a list.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the list
     */
    NestedListCtor<ELEMENT, BUILDER> apply(NestedListCtor<ELEMENT, BUILDER> ctor);
}