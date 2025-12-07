package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 * @param <BUILDER> the type of the builder for the elements
 */
@FunctionalInterface
public interface NestedListMtorFunction<ELEMENT, BUILDER> {
    /**
     * Function to implement to interact with a mtor to mutate a list.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the list
     */
    NestedListMtor<ELEMENT, BUILDER> apply(NestedListMtor<ELEMENT, BUILDER> mtor);
}