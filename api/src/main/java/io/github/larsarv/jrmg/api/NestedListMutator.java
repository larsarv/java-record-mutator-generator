package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mutator interface for mutable lists of records, allowing for fluent, chainable operations
 * to modify the contents of a list.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable list of the modified records.
 *
 * @param <T> the type the list element.
 * @param <M> the type of record mutator used to modify the record
 */
public interface NestedListMutator<T, M extends Mutator<T>> extends SimpleListMutator<T> {
    @Override
    T get(int index);
    @Override
    NestedListMutator<T, M> set(int index, T record);
    @Override
    NestedListMutator<T, M> add(T item);
    @Override
    NestedListMutator<T, M> remove(int index);
    @Override
    NestedListMutator<T, M> filter(Predicate<T> filterFunction);
    @Override
    NestedListMutator<T, M> updateAll(IndexedFunction<T> mutateFunction);
    @Override
    NestedListMutator<T, M> sort(Comparator<? super T> comparator);
    @Override
    NestedListMutator<T, M> move(int fromIndex, int toIndex);

    /**
     * Adds a new element to the end of the list using the provided mutator function.
     * The mutator is used to construct the element before appending it to the list.
     *
     * @param mutateFunction the mutator function used to create the element to be added
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> add(Function<M, M> mutateFunction);

    /**
     * Sets the element at the specified index using the provided mutator.
     * The mutator is used to construct the new element value, which replaces the existing element at the given index.
     *
     * @param index the index of the record to be replaced
     * @param mutator the mutator used to create the new record value
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> set(int index, M mutator);

    /**
     * Mutates the element at the specified index using the provided function.
     *
     * @param index the index of the element to be mutated
     * @param mutateFunction the function that transforms the element
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> mutate(int index, Function<M, M> mutateFunction);

    /**
     * Mutates all elements in the list using the provided indexed function.
     * The function is applied to each element in the list in order, with the index
     * indicating the position of the element within the list.
     *
     * @param mutateFunction the function to apply to each element, taking its index and the element itself
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> mutateAll(IndexedFunction<M> mutateFunction);

    /**
     * Finds the first element matching the given predicate and apply a mutation on it.
     * <p>
     * This method allows for targeted mutation of a single element in the list based on a condition.
     * The returned mutator instance can be used to apply transformations to the found element.
     *
     * @param predicate the predicate used to locate the first matching element to mutate
     * @param mutateFunction the function used to mutate the found element
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> findFirstAndMutate(Predicate<T> predicate, Function<M, M> mutateFunction);

    /**
     * Finds all elements matching the given predicate and applies a mutation on it.
     * <p>
     * This method allows for mutating multiple elements in the list based on a condition.
     * The mutator function is applied to each matching element in the list.
     * <p>
     * The returned function can be invoked to apply the mutations.
     *
     * @param predicate the predicate used to locate all matching elements to mutate
     * @param mutateFunction the function used to mutate each matching element
     * @return this mutator instance for method chaining
     */
    NestedListMutator<T, M> findAllAndMutate(Predicate<T> predicate, Function<M, M> mutateFunction);

    /**
     * Finalizes the mutable list and returns an immutable copy.
     * <p>
     * This method creates an immutable list from the current state of the mutator.
     * Any further modifications will not affect the returned list.
     *
     * @return a list containing the final state of all elements after applying all mutations
     */
    List<T> build();
}