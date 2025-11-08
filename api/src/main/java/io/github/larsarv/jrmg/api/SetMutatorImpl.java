package io.github.larsarv.jrmg.api;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of {@link MutableRecordSetMutator} that provides a fluent API for mutating a set of records.
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
 * @param <T> the type of elements in the set
 * @param <M> the type of record mutator used to modify the record
 */
public class SetMutatorImpl<T, M extends RecordMutator<T>> implements MutableRecordSetMutator<T, M> {
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

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(T element) {
        return set.contains(element);
    }

    @Override
    public MutableRecordSetMutator<T, M> add(T record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(record);
        return this;
    }

    @Override
    public MutableRecordSetMutator<T, M> remove(T record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.remove(record);
        return this;
    }

    @Override
    public MutableRecordSetMutator<T, M> filter(Predicate<T> filterFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public MutableRecordSetMutator<T, M> update(T record, SimpleFunction<T> mutateFunction) {
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
    public MutableRecordSetMutator<T, M> updateAll(SimpleFunction<T> mutateFunction) {
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
    public MutableRecordSetMutator<T, M> add(M recordMutator) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(recordMutator.build());
        return this;
    }

    @Override
    public MutableRecordSetMutator<T, M> mutate(T item, Function<M, M> mutateFunction) {
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
    public MutableRecordSetMutator<T, M> mutateAll(Function<M, M> mutateFunction) {
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