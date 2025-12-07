package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested set constructor.
 *
 * @param <ELEMENT> the type of elements in the set
 * @param <BUILDER> the type of the builder for the elements
 */
@FunctionalInterface
public interface NestedSetCtorFunction<ELEMENT, BUILDER> {
    /**
     * Function to implement to interact with a ctor to build a set.
     *
     * @param ctor the constructor instance to be used for construction
     * @return a modified version of the constructor used to create in the set
     */
    NestedSetCtor<ELEMENT, BUILDER> apply(NestedSetCtor<ELEMENT, BUILDER> ctor);
}