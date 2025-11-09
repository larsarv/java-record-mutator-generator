package io.github.larsarv.jrmg.api;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface for a mutable set that allows for fluent, chainable operations to modify its contents.
 * <p>
 * This interface provides methods to add, remove, filter, and update elements in the set.
 * <p>
 * The interface supports type parameter {@code T}, which represents the type of elements in the set.
 * <p>
 * The {@link #build()} method finalizes all mutations and returns an immutable set.
 *
 * @param <T> the type of elements in the set
 */
public interface SimpleSetMutator<T> extends Mutator<Set<T>> {
    /**
     * Returns the number of elements in the set.
     *
     * @return the number of elements in the set
     */
    int size();

    /**
     * Checks whether the set contains the specified element.
     * <p>
     * This method returns true if the set contains the specified element, otherwise false.
     *
     * @param element the element to check for containment
     * @return true if the set contains the element, false otherwise
     */
    boolean contains(T element);

    /**
     * Adds the specified record to the set.
     * <p>
     * This operation modifies the set in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If the element is already present, it is not added again.
     *
     * @param record the element to add to the set
     * @return this mutator instance for method chaining
     */
    SimpleSetMutator<T> add(T record);

    /**
     * Removes the specified record from the set.
     * <p>
     * This operation modifies the set in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If the element is not present, it is not removed.
     *
     * @param record the element to remove from the set
     * @return this mutator instance for method chaining
     */
    SimpleSetMutator<T> remove(T record);

    /**
     * Filters the set by removing elements that do not satisfy the given predicate.
     * <p>
     * This operation modifies the set in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * The predicate is applied to each element in the set. If the predicate returns false
     * for an element, that element is removed from the set.
     *
     * @param filterFunction the predicate used to determine whether an element should be retained
     * @return this mutator instance for method chaining
     */
    SimpleSetMutator<T> filter(Predicate<T> filterFunction);


    /**
     * Updates the set by applying the given mutation function to the specified item.
     * <p>
     * If the item is present in the set, it is replaced with the result of applying the mutation function.
     * If the item is not present, it is not modified.
     * <p>
     * This operation modifies the set in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param item the item to update
     * @param mutateFunction the function to apply to the item, accepting the current item and returning the modified item
     * @return this mutator instance for method chaining
     */
    SimpleSetMutator<T> update(T item, SimpleFunction<T> mutateFunction);

    /**
     * Applies the given mutation function to all elements in the set.
     * The function is invoked for each element.
     * A new set is built with the elements returned from the mutation function.
     * <p>
     * Once the mutation function has been applied on all elements, the new list replaces the old list.
     *
     * @param mutateFunction the function to apply to each element, accepting the current element, returning the modified element
     * @return this mutator instance for method chaining
     */
    SimpleSetMutator<T> updateAll(SimpleFunction<T> mutateFunction);

    /**
     * Finalizes the mutable set and returns an immutable copy.
     * <p>
     * This method creates an immutable set from the current state of the mutator.
     * Any further mutations will trigger a {@link java.lang.IllegalStateException}
     *
     * @return an immutable set containing the current elements
     */
    @Override
    Set<T> build();

    /**
     * Finalizes the mutable list and returns an immutable copy.
     * <p>
     * This method creates an immutable list from the current state of the mutator.
     * Any further mutations will not affect the returned list.
     *
     * @return an immutable list containing the current elements
     */
    Set<T> buildCopy();
}