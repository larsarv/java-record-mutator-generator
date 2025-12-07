package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a nested set mutator.
 *
 * @param <ELEMENT> the type of elements in the set
 * @param <BUILDER> the type of the builder for the elements
 */
@FunctionalInterface
public interface NestedSetMtorFunction<ELEMENT, BUILDER> {
    /**
     * Function to implement to interact with a mtor to mutate a set.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the set
     */
    NestedSetMtor<ELEMENT, BUILDER> apply(NestedSetMtor<ELEMENT, BUILDER> mtor);
}
