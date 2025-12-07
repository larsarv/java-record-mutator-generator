package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;

/**
 * Interface for a simple list constructor.
 * <p>
 * Provides methods to construct a list in a fluent manner.
 *
 * @param <ELEMENT> the type of elements in the list
 */
public interface SimpleListCtor<ELEMENT> extends Builder<List<ELEMENT>> {
    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    int size();

    /**
     * Returns the element at the specified position in the list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position
     */
    ELEMENT get(int index);

    /**
     * Appends the specified element to the end of the list.
     *
     * @param item the element to be appended
     * @return this constructor
     */
    SimpleListCtor<ELEMENT> add(ELEMENT item);

    /**
     * Sorts the list according to the order induced by the specified comparator.
     *
     * @param comparator the comparator to determine the order of the list
     * @return this constructor
     */
    SimpleListCtor<ELEMENT> sort(Comparator<? super ELEMENT> comparator);

    /**
     * Builds a copy of the list.
     *
     * @return a new list containing the elements of the constructed list
     */
    List<ELEMENT> buildCopy();

}
