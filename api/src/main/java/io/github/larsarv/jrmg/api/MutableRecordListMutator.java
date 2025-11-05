package io.github.larsarv.jrmg.api;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mutator interface for mutable lists of records, allowing for fluent, chainable operations
 * to modify the contents of a list.
 * <p>
 * This interface extends {@link SimpleListMutator} and adds methods to manipulate records
 * using {@link RecordMutator} instances, enabling more complex transformations that
 * involve mutating the internal state of records.
 * <p>
 * The mutator operates on a list of type {@code T}, where each element can be mutated
 * using a {@code RecordMutator<T>}.
 * <p>
 * Methods such as {@link #add(RecordMutator)}, {@link #set(int, RecordMutator)}, and {@link #mutate(int, Function)}
 * allow for modifying the list's contents through record mutators, while {@link #mutateAll(IndexedFunction)}
 * allows for mutating all records in the list based on their index.
 * <p>
 * The {@link #build()} method finalizes the mutations and returns an immutable list of the modified records.
 *
 * @param <T> the type the list element. Should be a record
 * @param <M> the type of record mutator used to modify the record
 */
public interface MutableRecordListMutator<T, M extends RecordMutator<T>> extends SimpleListMutator<T> {
    @Override
    T get(int index);
    @Override
    MutableRecordListMutator<T, M> set(int index, T record);
    @Override
    MutableRecordListMutator<T, M> add(T record);
    @Override
    MutableRecordListMutator<T, M> remove(int index);
    @Override
    MutableRecordListMutator<T, M> filter(Predicate<T> filterFunction);
    @Override
    MutableRecordListMutator<T, M> updateAll(IndexedFunction<T> mutateFunction);

    /**
     * Adds a new record to the end of the list using the provided record mutator.
     * The mutator is used to construct the record before appending it to the list.
     *
     * @param recordMutator the mutator used to create the record to be added
     * @return a new instance of this mutator with the record added
     */
    MutableRecordListMutator<T, M> add(M recordMutator);

    /**
     * Sets the record at the specified index using the provided record mutator.
     * The mutator is used to construct the new record value, which replaces the existing record at the given index.
     *
     * @param index the index of the record to be replaced
     * @param recordMutator the mutator used to create the new record value
     * @return a new instance of this mutator with the record at the specified index replaced
     */
    MutableRecordListMutator<T, M> set(int index, M recordMutator);

    /**
     * Mutates the record at the specified index using the provided function.
     *
     * @param index the index of the record to be mutated
     * @param mutateFunction the function that transforms the object
     * @return a mutator used to build the record at the specified index
     */
    MutableRecordListMutator<T, M> mutate(int index, Function<M, M> mutateFunction);

    /**
     * Mutates all records in the list using the provided indexed function.
     * The function is applied to each record in the list in order, with the index
     * indicating the position of the record within the list.
     *
     * @param mutateFunction the function to apply to each record, taking its index and the record itself
     * @return a new mutator instance with all records mutated according to the provided function
     */
    MutableRecordListMutator<T, M> mutateAll(IndexedFunction<M> mutateFunction);

    /**
     * Finalizes the mutable list and returns an immutable copy.
     * <p>
     * This method creates an immutable list from the current state of the mutator.
     * Any further modifications will not affect the returned list.
     *
     * @return a list containing the final state of all records after applying all mutations
     */
    List<T> build();
}
