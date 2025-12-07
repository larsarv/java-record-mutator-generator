package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for a simple list mutator.
 * <p>
 * Provides methods to modify a list in a fluent manner.
 *
 * @param <ELEMENT> the type of elements in the list
 */
public interface SimpleListMtor<ELEMENT> extends Builder<List<ELEMENT>> {

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
     * Replaces the element at the specified position in the list with the specified element.
     *
     * @param index  the index of the element to replace
     * @param record the element to be stored at the specified position
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> set(int index, ELEMENT record);

    /**
     * Appends the specified element to the end of the list.
     *
     * @param item the element to be appended
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> add(ELEMENT item);

    /**
     * Removes the element at the specified position in the list.
     *
     * @param index the index of the element to be removed
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> remove(int index);

    /**
     * Removes all elements of the list that satisfy the given predicate.
     *
     * @param filterFunction a predicate which returns {@code true} for elements to be removed
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> filter(Predicate<ELEMENT> filterFunction);

    /**
     * Replaces each element of the list with the result of applying the operator to that element.
     *
     * @param indexedMutateFunction the operator to apply to each element
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> updateAll(IndexedFunction<ELEMENT, ELEMENT> indexedMutateFunction);

    /**
     * Sorts the list according to the order induced by the specified comparator.
     *
     * @param comparator the comparator to determine the order of the list
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> sort(Comparator<? super ELEMENT> comparator);

    /**
     * Moves an element from one position to another in the list.
     *
     * @param fromIndex the index of the element to move
     * @param toIndex   the index to move the element to
     * @return this mutator
     */
    SimpleListMtor<ELEMENT> move(int fromIndex, int toIndex);

    /**
     * Builds a copy of the list.
     *
     * @return a new list containing the elements of the mutated list
     */
    List<ELEMENT> buildCopy();

}
