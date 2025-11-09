package io.github.larsarv.jrmg.api;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link NestedSetMutator} that provides a fluent API for mutating a set of records.
 * <p>
 * This implementation maintains a mutable set internally and provides methods to
 * modify its contents. It allows for adding, removing, filtering, and updating elements of the set.
 * <p>
 * This implementation maintains a mutable set internally and provides methods to
 * modify its contents.
 * <p>
 * The constructor accepts a set, which is copied to the internal mutable set. If the provided set is null,
 * an empty set is created.
 * <p>
 * This class is designed for use in fluent APIs where operations are chained together before finalizing
 * the result with {@link #build()}.
 *
 * @param <T> the type of elements stored in the set.
 * @param <M> the type of {@link Mutator} used to mutate the elements of type {@code T}
 */
public class SetMutatorImpl<T, M extends Mutator<T>> implements NestedSetMutator<T, M> {
    private Set<T> set;
    private final Function<T, M> elementMutatorFactory;
    private boolean locked = false;

    /**
     * Constructs a new instance of MutableRecordSetMutatorImpl for the specified set and element mutator factory.
     *
     * @param set the set to be copied into the internal mutable set; may be {@code null}
     * @param elementMutatorFactory a function that generates a mutator for each element in the set
     */
    public SetMutatorImpl(Set<T> set, Function<T, M> elementMutatorFactory) {
        this.set = set == null ? new HashSet<>() : new HashSet<>(set);
        this.elementMutatorFactory = elementMutatorFactory;
    }

    /**
     * Creates a new set mutator for the specified set, using the provided element mutator factory.
     * <p>
     * Each element in the set can be individually mutated using the factory-provided mutator.
     *
     * @param <T> the type of elements stored in the set.
     * @param <E> the type of {@link Mutator} used to mutate the elements of type {@code T}
     * @param set the initial set to be wrapped; if null, an empty set is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the set,
     *                              null if the element data type is simple
     * @return a new set mutator instance that can be used to modify the set
     */
    public static <T, E extends Mutator<T>> NestedSetMutator<T, E> mutator(Set<T> set, Function<T, E> elementMutatorFactory) {
        return new SetMutatorImpl<>(set, elementMutatorFactory);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(T element) {
        return set.contains(element);
    }

    @Override
    public NestedSetMutator<T, M> add(T record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(record);
        return this;
    }

    @Override
    public NestedSetMutator<T, M> remove(T record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.remove(record);
        return this;
    }

    @Override
    public NestedSetMutator<T, M> filter(Predicate<T> filterFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public NestedSetMutator<T, M> update(T record, SimpleFunction<T> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        if (set.contains(record)) {
            set.remove(record);
            set.add(mutateFunction.apply(record));
        }
        return this;
    }

    @Override
    public NestedSetMutator<T, M> updateAll(SimpleFunction<T> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<T> newSet = new HashSet<>();
        for (T item : set) {
            T newItem = mutateFunction.apply(item);
            newSet.add(newItem);
        }
        set = newSet;
        return this;
    }

    @Override
    public NestedSetMutator<T, M> add(Function<M, M> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(mutateFunction.apply(elementMutatorFactory.apply(null)).build());
        return this;
    }

    @Override
    public NestedSetMutator<T, M> mutate(T item, Function<M, M> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        if (set.contains(item)) {
            set.remove(item);
            set.add(mutateFunction.apply(elementMutatorFactory.apply(item)).build());
        }
        return this;
    }

    @Override
    public NestedSetMutator<T, M> mutateAll(Function<M, M> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<T> newSet = new HashSet<>();
        for (T item : set) {
            T newValue = mutateFunction.apply(elementMutatorFactory.apply(item)).build();
            newSet.add(newValue);
        }
        set = newSet;
        return this;
    }

    @Override
    public Set<T> build() {
        this.locked = true;
        return Collections.unmodifiableSet(set);
    }

    @Override
    public Set<T> buildCopy() {
        return Collections.unmodifiableSet(new HashSet<>(set));
    }
}