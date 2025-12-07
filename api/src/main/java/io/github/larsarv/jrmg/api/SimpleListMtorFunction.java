package io.github.larsarv.jrmg.api;

/**
 * Functional interface for interacting with a simple list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 */
/**
 * Functional interface for interacting with a simple list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 */
/**
 * Functional interface for interacting with a simple list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 */
/**
 * Functional interface for interacting with a simple list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 */

/**
 * Functional interface for interacting with a simple list mutator.
 *
 * @param <ELEMENT> the type of elements in the list
 */
@FunctionalInterface
public interface SimpleListMtorFunction<ELEMENT> {
    /**
     * Function to implement to interact with a mtor to mutate a list.
     *
     * @param mtor the mutator instance to be used for mutation
     * @return a modified version of the mutator used to mutate in the list
     */
    SimpleListMtor<ELEMENT> apply(SimpleListMtor<ELEMENT> mtor);
}
