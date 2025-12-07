package io.github.larsarv.jrmg.api;

import java.util.Set;

/**
 * Interface for a simple set constructor.
 * <p>
 * Provides methods to construct a set in a fluent manner.
 *
 * @param <ELEMENT> the type of elements in the set
 */
public interface SimpleSetCtor<ELEMENT> extends Builder<Set<ELEMENT>> {
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
     * @return this constructor
     */
    SimpleSetCtor<ELEMENT> add(ELEMENT record);

    /**
     * Builds a copy of the set.
     *
     * @return a new set containing the elements of the constructed set
     */
    Set<ELEMENT> buildCopy();
}