package io.github.larsarv.jrmg.api;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Interface for a simple set mutator.
 * <p>
 * Provides methods to modify a set in a fluent manner.
 *
 * @param <ELEMENT> the type of elements in the set
 */
public interface SimpleSetMtor<ELEMENT> extends Builder<Set<ELEMENT>> {
    /**
     * Returns the number of elements in the set.
     *
     * @return the size of the set
     */
    int size();

    /**
     * Returns {@code true} if this set contains the specified element.
     *
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    boolean contains(ELEMENT element);

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param record element to be added to this set
     * @return this mutator
     */
    SimpleSetMtor<ELEMENT> add(ELEMENT record);

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param record object to be removed from this set, if present
     * @return this mutator
     */
    SimpleSetMtor<ELEMENT> remove(ELEMENT record);

    /**
     * Removes all elements of the set that satisfy the given predicate.
     *
     * @param filterFunction a predicate which returns {@code true} for elements to be removed
     * @return this mutator
     */
    SimpleSetMtor<ELEMENT> filter(Predicate<ELEMENT> filterFunction);


    /**
     * Updates an element in the set using the provided function.
     *
     * @param item           the element to update
     * @param mutateFunction the function to apply to the element
     * @return this mutator
     */
    SimpleSetMtor<ELEMENT> update(ELEMENT item, SimpleFunction<ELEMENT> mutateFunction);

    /**
     * Updates all elements in the set using the provided function.
     *
     * @param mutateFunction the function to apply to each element
     * @return this mutator
     */
    SimpleSetMtor<ELEMENT> updateAll(SimpleFunction<ELEMENT> mutateFunction);

    /**
     * Builds the final immutable set.
     *
     * @return the immutable set
     */
    @Override
    Set<ELEMENT> build();

    /**
     * Builds a copy of the set.
     *
     * @return a new set containing the elements of the mutated set
     */
    Set<ELEMENT> buildCopy();
}