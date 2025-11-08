package io.github.larsarv.jrmg.api;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mutator interface for mutable sets of records, allowing for fluent, chainable operations
 * to modify the contents of a set.
 * <p>
 * This interface extends {@link SimpleSetMutator} and adds methods to manipulate records
 * using {@link RecordMutator} instances, enabling more complex transformations that
 * involve mutating the internal state of records.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable set of the modified records.
 *
 * @param <T> the type the set element. Should be a record annotated with {@link GenerateMutator}.
 * @param <M> the type of record mutator used to modify the record
 */
public interface MutableRecordSetMutator<T, M extends RecordMutator<T>> extends SimpleSetMutator<T> {
    @Override
    MutableRecordSetMutator<T,M> add(T record);
    @Override
    MutableRecordSetMutator<T,M> remove(T record);
    @Override
    MutableRecordSetMutator<T,M> filter(Predicate<T> filterFunction);
    @Override
    MutableRecordSetMutator<T,M> update(T item, SimpleFunction<T> mutateFunction);
    @Override
    MutableRecordSetMutator<T,M> updateAll(SimpleFunction<T> mutateFunction);

    /**
     * Adds a new record to the set using the provided record mutator.
     * The mutator is used to construct the record before adding it to the set.
     *
     * @param recordMutator the mutator used to create the record to be added
     * @return a new instance of this mutator with the record added
     */
    MutableRecordSetMutator<T, M> add(M recordMutator);

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
    MutableRecordSetMutator<T, M> mutate(T item, Function<M, M> mutateFunction);

    /**
     * Mutates all records in the set using the provided function.
     * The function is applied to each record in the set in order.
     *
     * @param mutateFunction the function to apply to each record
     * @return a new mutator instance with all records mutated according to the provided function
     */
    MutableRecordSetMutator<T, M> mutateAll(Function<M, M> mutateFunction);

    /**
     * Finalizes the mutable set and returns an immutable copy.
     * <p>
     * This method creates an immutable set from the current state of the mutator.
     * Any further modifications will not affect the returned set.
     *
     * @return a set containing the final state of all records after applying all mutations
     */
    Set<T> build();
}