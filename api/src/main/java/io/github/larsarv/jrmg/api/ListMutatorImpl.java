package io.github.larsarv.jrmg.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link NestedListMutator} that provides a fluent, chainable API
 * for mutating a list of records. It allows for adding, removing, filtering, and updating
 * records using {@link Mutator} instances, which can be used to create or modify
 * records in place.
 * <p>
 * This implementation maintains a mutable list internally and provides methods to
 * modify its contents. The mutator is built around a factory function that creates
 * {@link Mutator} instances for each record, enabling transformations that
 * affect the internal state of records.
 * <p>
 * The {@link #build()} method returns an immutable list
 * of the modified records. All mutations are performed in-place on the internal list,
 * and the mutator returns itself for method chaining.
 *
 * @param <T> the type of elements stored in the list.
 * @param <E> the type of {@link Mutator} used to mutate the elements of type {@code T}
 */
public class ListMutatorImpl<T, E extends Mutator<T>> implements NestedListMutator<T, E> {
    private final List<T> list;
    private final Function<T, E> elementMutatorFactory;
    private boolean locked = false;

    /**
     * Constructs a new instance of MutableRecordListMutatorImpl for the specified list and element mutator factory.
     *
     * @param list the initial list to be wrapped; if null, an empty list is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the list
     */
    public ListMutatorImpl(List<T> list, Function<T, E> elementMutatorFactory) {
        this.list = list == null ? new ArrayList<>(): new ArrayList<>(list);
        this.elementMutatorFactory = elementMutatorFactory;
    }

    /**
     * Creates a new list mutator for the specified list, using the provided element mutator factory.
     * <p>
     * Each element in the list can be individually mutated using the factory-provided mutator.
     *
     * @param <T> the type of elements stored in the list.
     * @param <E> the type of {@link Mutator} used to mutate the elements of type {@code T}
     * @param list the initial list to be wrapped; if null, an empty list is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the list,
     *                              null if the element data type is simple
     * @return a new list mutator instance that can be used to modify the list
     */
    public static <T, E extends Mutator<T>> NestedListMutator<T, E> mutator(List<T> list, Function<T, E> elementMutatorFactory) {
        return new ListMutatorImpl<>(list, elementMutatorFactory);
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
    public NestedListMutator<T, E> set(int index, T record) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, record);
        return this;
    }

    @Override
    public NestedListMutator<T, E> add(T item) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(item);
        return this;
    }

    @Override
    public NestedListMutator<T, E> remove(int index) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.remove(index);
        return this;
    }

    @Override
    public NestedListMutator<T, E> filter(Predicate<T> filterFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public NestedListMutator<T, E> updateAll(IndexedFunction<T> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
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
    public NestedListMutator<T, E> sort(Comparator<? super T> comparator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.sort(comparator);
        return this;
    }

    @Override
    public NestedListMutator<T, E> move(int fromIndex, int toIndex) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        if (fromIndex < 0 || fromIndex >= list.size() || toIndex < 0 || toIndex >= list.size()) {
            throw new IndexOutOfBoundsException("Index: " + fromIndex + ", Size: " + list.size());
        }
        T item = list.remove(fromIndex);
        list.add(toIndex, item);
        return this;
    }

    @Override
    public NestedListMutator<T, E> set(int index, E recordMutator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, recordMutator.build());
        return this;
    }

    @Override
    public NestedListMutator<T, E> add(Function<E, E> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(mutateFunction.apply(elementMutatorFactory.apply(null)).build());
        return this;
    }

    @Override
    public NestedListMutator<T, E> mutate(int index, Function<E, E> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        T orgValue = list.get(index);
        T newValue = modifierFunction.apply(elementMutatorFactory.apply(orgValue)).build();
        list.set(index, newValue);
        return this;
    }

    @Override
    public NestedListMutator<T, E> mutateAll(IndexedFunction<E> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        for (int index = 0; index < list.size(); index++) {
            T orgValue = list.get(index);
            T newValue = modifierFunction.apply(index, elementMutatorFactory.apply(orgValue)).build();
            list.set(index, newValue);
        }
        return this;
    }

    @Override
    public List<T> build() {
        this.locked = true;
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<T> buildCopy() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
