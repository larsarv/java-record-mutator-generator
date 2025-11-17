package io.github.larsarv.jrmg.api;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mutator interface for mutable sets of records, allowing for fluent, chainable operations
 * to modify the contents of a set.
 * <p>
 * This interface extends {@link SimpleSetMutator} and adds methods to manipulate records
 * using {@link Builder} instances, enabling more complex transformations that
 * involve mutating the internal state of records.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable set of the modified records.
 *
 * @param <E> the type of elements stored in the set.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
public interface NestedSetMutator<E, MFP, MFR extends Builder<E>> extends SimpleSetMutator<E> {
    @Override
    NestedSetMutator<E, MFP, MFR> add(E record);
    @Override
    NestedSetMutator<E, MFP, MFR> remove(E record);
    @Override
    NestedSetMutator<E, MFP, MFR> filter(Predicate<E> filterFunction);
    @Override
    NestedSetMutator<E, MFP, MFR> update(E item, SimpleFunction<E> mutateFunction);
    @Override
    NestedSetMutator<E, MFP, MFR> updateAll(SimpleFunction<E> mutateFunction);

    /**
     * Adds a new element to the end of the set using the provided mutator function.
     * The mutator is used to construct the element before appending it to the set.
     *
     * @param mutateFunction the mutator function used to create the element to be added
     * @return this mutator instance for method chaining
     */
    NestedSetMutator<E, MFP, MFR> add(Function<MFP, MFR> mutateFunction);

    /**
     * Mutates a specific item in the set using the provided mutator function.
     * The function receives a mutator instance for the item and returns a new mutator
     * instance with the desired mutations applied. This allows for fluent, chainable
     * modifications to the record's state.
     * <p>
     * The old item will be removed and the new
     *
     * @param item the item to be mutated
     * @param mutateFunction the function that takes a mutator for the item and returns a mutated version
     * @return a new mutator instance with the item mutated according to the provided function
     */
    NestedSetMutator<E, MFP, MFR> mutate(E item, Function<MFP, MFR> mutateFunction);

    /**
     * Mutates all records in the set using the provided function.
     * The function is applied to each record in the set in order.
     *
     * @param mutateFunction the function to apply to each record
     * @return a new mutator instance with all records mutated according to the provided function
     */
    NestedSetMutator<E, MFP, MFR> mutateAll(Function<MFP, MFR> mutateFunction);

    /**
     * Finalizes the mutable set and returns an immutable copy.
     * <p>
     * This method creates an immutable set from the current state of the mutator.
     * Any further modifications will not affect the returned set.
     *
     * @return a set containing the final state of all records after applying all mutations
     */
    Set<E> build();
}