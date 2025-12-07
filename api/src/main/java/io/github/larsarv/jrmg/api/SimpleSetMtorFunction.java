package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple set mutator.
 *
 * @param <ELEMENT> the type of elements in the set
 */
@FunctionalInterface
public interface SimpleSetMtorFunction<ELEMENT> {
    /**
     * Function to implement to interact with a mtor to mutate a set.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the set
     */
    SimpleSetMtor<ELEMENT> apply(SimpleSetMtor<ELEMENT> mtor);
}
