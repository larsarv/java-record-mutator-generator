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
 * records using {@link Builder} instances, which can be used to create or modify
 * records in place.
 * <p>
 * This implementation maintains a mutable list internally and provides methods to
 * modify its contents. The mutator is built around a factory function that creates
 * {@link Builder} instances for each record, enabling transformations that
 * affect the internal state of records.
 * <p>
 * The {@link #build()} method returns an immutable list
 * of the modified records. All mutations are performed in-place on the internal list,
 * and the mutator returns itself for method chaining.
 *
 * @param <E> the type of elements stored in the list.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
public class ListMutatorImpl<E, MFP, MFR extends Builder<E>> implements NestedListMutator<E, MFP, MFR> {
    private final List<E> list;
    private final Function<E, MFP> elementMutatorFactory;
    private boolean locked = false;

    /**
     * Constructs a new instance of MutableRecordListMutatorImpl for the specified list and element mutator factory.
     *
     * @param list the initial list to be wrapped; if null, an empty list is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the list
     */
    public ListMutatorImpl(List<E> list, Function<E, MFP> elementMutatorFactory) {
        this.list = list == null ? new ArrayList<>(): new ArrayList<>(list);
        this.elementMutatorFactory = elementMutatorFactory;
    }

    /**
     * Creates a new list mutator for the specified list, using the provided element mutator factory.
     * <p>
     * Each element in the list can be individually mutated using the factory-provided mutator.
     *
     * @param <E> the type of elements stored in the list.
     * @param <MFP> the type of the mutate function parameter.
     * @param <MFR> the return type mutate function.
     * @param list the initial list to be wrapped; if null, an empty list is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the list,
     *                              null if the element data type is simple
     * @return a new list mutator instance that can be used to modify the list
     */
    public static <E, MFP, MFR extends Builder<E>> NestedListMutator<E, MFP, MFR> mutator(
            List<E> list,
            Function<E, MFP> elementMutatorFactory
    ) {
        return new ListMutatorImpl<>(list, elementMutatorFactory);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public NestedListMutator<E, MFP, MFR> set(int index, E record) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, record);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> add(E item) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(item);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> remove(int index) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.remove(index);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> filter(Predicate<E> filterFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> updateAll(IndexedFunction<E, E> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        for (int index = 0; index != list.size(); ++index) {
            E orgItem = list.get(index);
            E newItem = mutateFunction.apply(index, orgItem);
            if (newItem != orgItem) {
                list.set(index, newItem);
            }
        }
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> sort(Comparator<? super E> comparator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.sort(comparator);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> move(int fromIndex, int toIndex) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        if (fromIndex < 0 || fromIndex >= list.size() || toIndex < 0 || toIndex >= list.size()) {
            throw new IndexOutOfBoundsException("Index: " + fromIndex + ", Size: " + list.size());
        }
        E item = list.remove(fromIndex);
        list.add(toIndex, item);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> set(int index, MFR recordMutator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, recordMutator.build());
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> add(Function<MFP, MFR> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(mutateFunction.apply(elementMutatorFactory.apply(null)).build());
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> mutate(int index, Function<MFP, MFR> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        E orgValue = list.get(index);
        E newValue = modifierFunction.apply(elementMutatorFactory.apply(orgValue)).build();
        list.set(index, newValue);
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> mutateAll(IndexedFunction<MFP, MFR> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        for (int index = 0; index < list.size(); index++) {
            E orgValue = list.get(index);
            E newValue = modifierFunction.apply(index, elementMutatorFactory.apply(orgValue)).build();
            list.set(index, newValue);
        }
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> findFirstAndMutate(Predicate<E> predicate, Function<MFP, MFR> mutatorFunction) {
        for (int index = 0; index < list.size(); index++) {
            E orgValue = list.get(index);
            if (predicate.test(orgValue)) {
                list.set(index, mutatorFunction.apply(elementMutatorFactory.apply(orgValue)).build());
                return  this;
            }
        }
        return this;
    }

    @Override
    public NestedListMutator<E, MFP, MFR> findAllAndMutate(Predicate<E> predicate, Function<MFP, MFR> mutatorFunction) {
        for (int index = 0; index < list.size(); index++) {
            E orgValue = list.get(index);
            if (predicate.test(orgValue)) {
                list.set(index, mutatorFunction.apply(elementMutatorFactory.apply(orgValue)).build());
            }
        }
        return this;
    }

    @Override
    public List<E> build() {
        this.locked = true;
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<E> buildCopy() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
