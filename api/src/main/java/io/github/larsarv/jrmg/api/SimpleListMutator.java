package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An interface for a mutable list that allows for fluent, chainable operations to modify its contents.
 * <p>
 * This interface provides methods to set, add, remove, filter, and update elements in the list.
 * <p>
 * The interface supports type parameter {@code T}, which represents the type of elements in the list.
 * <p>
 * The {@link #build()} method finalizes all mutations and returns an immutable list.
 *
 * @param <T> the type of elements in the list
 */
public interface SimpleListMutator<T> extends Mutator<List<T>> {
    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements in the list
     */
    int size();

    /**
     * Returns the element at the specified index in the list.
     *
     * @param index the index of the element to return
     * @return the element at the specified index
     */
    T get(int index);

    /**
     * Sets the element at the specified index in the list to the given record.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If the index is out of bounds, an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param index the index of the element to replace
     * @param record the new element to set at the specified index
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> set(int index, T record);

    /**
     * Adds the specified item to add the end of the list.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param item the item to add to the list
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> add(T item);

    /**
     * Removes the element at the specified index from the list.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If the index is out of bounds, an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param index the index of the element to remove
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> remove(int index);

    /**
     * Filters the list by removing elements that do not satisfy the given predicate.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * The predicate is applied to each element in the list. If the predicate returns false
     * for an element, that element is removed from the list.
     *
     * @param filterFunction the predicate used to determine whether an element should be retained
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> filter(Predicate<T> filterFunction);

    /**
     * Applies the given mutation function to all elements in the list.
     * The function is invoked for each element with its index and the current element.
     * If the returned element differs from the original, it is updated in place.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param indexedMutateFunction the function to apply to each element, accepting its index and the current element, returning the modified element
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> updateAll(IndexedFunction<T> indexedMutateFunction);

    /**
     * Sorts the elements of the list in place according to the provided comparator.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * The comparator defines the ordering of elements. If two elements are considered
     * equal by the comparator, their relative positions in the list are not guaranteed
     * to be preserved.
     *
     * @param comparator the comparator to determine the order of the list
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> sort(Comparator<? super T> comparator);

    /**
     * Moves an element from one index to another in the list.
     * <p>
     * This operation modifies the list in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If either index is out of bounds, an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param fromIndex the index of the element to move
     * @param toIndex the index to move the element to
     * @return this mutator instance for method chaining
     */
    SimpleListMutator<T> move(int fromIndex, int toIndex);

    /**
     * Finalizes the mutable list and returns an immutable copy.
     * <p>
     * This method creates an immutable list from the current state of the mutator.
     * Any further mutations will trigger a {@link java.lang.IllegalStateException}
     *
     * @return an immutable list containing the current elements
     */
    @Override
    List<T> build();

    /**
     * Finalizes the mutable list and returns an immutable copy.
     * <p>
     * This method creates an immutable list from the current state of the mutator.
     * Any further mutations will not affect the returned list.
     *
     * @return an immutable list containing the current elements
     */
    List<T> buildCopy();

}
