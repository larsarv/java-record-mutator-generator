package io.github.larsarv.jrmg.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link MutableRecordListMutator} that provides a fluent, chainable API
 * for mutating a list of records. It allows for adding, removing, filtering, and updating
 * records using {@link RecordMutator} instances, which can be used to create or modify
 * records in place.
 * <p>
 * This implementation maintains a mutable list internally and provides methods to
 * modify its contents. The mutator is built around a factory function that creates
 * {@link RecordMutator} instances for each record, enabling transformations that
 * affect the internal state of records.
 * <p>
 * The {@link #build()} method returns an immutable list
 * of the modified records. All mutations are performed in-place on the internal list,
 * and the mutator returns itself for method chaining.
 *
 * @param <T> the type of records stored in the list. Should be a record type
 * @param <E> the type of {@link RecordMutator} used to mutate records of type {@code T}
 */
public class MutableRecordListMutatorImpl<T, E extends RecordMutator<T>> implements MutableRecordListMutator<T, E> {
    private final List<T> list;
    private final Function<T, E> elementMutatorFactory;

    /**
     * Constructs a new instance of MutableRecordListMutatorImpl for the specified list and element mutator factory.
     *
     * @param list the initial list to be wrapped; if null, an empty list is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the list
     */
    public MutableRecordListMutatorImpl(List<T> list, Function<T, E> elementMutatorFactory) {
        this.list = list == null ? new ArrayList<>(): new ArrayList<>(list);
        this.elementMutatorFactory = elementMutatorFactory;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public MutableRecordListMutator<T, E> set(int index, T record) {
        list.set(index, record);
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> add(T record) {
        list.add(record);
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> remove(int index) {
        list.remove(index);
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> filter(Predicate<T> filterFunction) {
        list.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> updateAll(IndexedFunction<T> mutateFunction) {
        for (int index = 0; index != list.size(); ++index) {
            T orgItem = list.get(index);
            T newItem = mutateFunction.apply(index, orgItem);
            if (newItem != orgItem) {
                list.set(index, newItem);
            }
        }
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> set(int index, E recordMutator) {
        list.set(index, recordMutator.build());
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> add(E recordMutator) {
        list.add(recordMutator.build());
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> mutate(int index, Function<E, E> modifierFunction) {
        T orgValue = list.get(index);
        T newValue = modifierFunction.apply(elementMutatorFactory.apply(orgValue)).build();
        list.set(index, newValue);
        return this;
    }

    @Override
    public MutableRecordListMutator<T, E> mutateAll(IndexedFunction<E> modifierFunction) {
        for (int index = 0; index < list.size(); index++) {
            T orgValue = list.get(index);
            T newValue = modifierFunction.apply(index, elementMutatorFactory.apply(orgValue)).build();
            list.set(index, newValue);
        }
        return this;
    }

    @Override
    public List<T> build() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
