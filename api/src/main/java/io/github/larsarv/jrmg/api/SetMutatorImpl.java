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
 * @param <E> the type of elements stored in the set.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
public class SetMutatorImpl<E, MFP, MFR extends Builder<E>> implements NestedSetMutator<E, MFP, MFR> {
    private Set<E> set;
    private final Function<E, MFP> elementMutatorFactory;
    private boolean locked = false;

    /**
     * Constructs a new instance of MutableRecordSetMutatorImpl for the specified set and element mutator factory.
     *
     * @param set the set to be copied into the internal mutable set; may be {@code null}
     * @param elementMutatorFactory a function that generates a mutator for each element in the set
     */
    public SetMutatorImpl(Set<E> set, Function<E, MFP> elementMutatorFactory) {
        this.set = set == null ? new HashSet<>() : new HashSet<>(set);
        this.elementMutatorFactory = elementMutatorFactory;
    }

    /**
     * Creates a new set mutator for the specified set, using the provided element mutator factory.
     * <p>
     * Each element in the set can be individually mutated using the factory-provided mutator.
     *
     * @param <E> the type of elements stored in the set.
     * @param <MFP> the type of the mutate function parameter.
     * @param <MFR> the return type mutate function.
     * @param set the initial set to be wrapped; if null, an empty set is created
     * @param elementMutatorFactory a function that generates a mutator for each element in the set,
     *                              null if the element data type is simple
     * @return a new set mutator instance that can be used to modify the set
     */
    public static <E, MFP, MFR extends Builder<E>> NestedSetMutator<E, MFP, MFR> mutator(Set<E> set, Function<E, MFP> elementMutatorFactory) {
        return new SetMutatorImpl<>(set, elementMutatorFactory);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(E element) {
        return set.contains(element);
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> add(E record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(record);
        return this;
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> remove(E record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.remove(record);
        return this;
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> filter(Predicate<E> filterFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.removeIf(e -> !filterFunction.test(e));
        return this;
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> update(E record, SimpleFunction<E> mutateFunction) {
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
    public NestedSetMutator<E, MFP, MFR> updateAll(SimpleFunction<E> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<E> newSet = new HashSet<>();
        for (E item : set) {
            E newItem = mutateFunction.apply(item);
            newSet.add(newItem);
        }
        set = newSet;
        return this;
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> add(Function<MFP, MFR> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(mutateFunction.apply(elementMutatorFactory.apply(null)).build());
        return this;
    }

    @Override
    public NestedSetMutator<E, MFP, MFR> mutate(E item, Function<MFP, MFR> mutateFunction) {
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
    public NestedSetMutator<E, MFP, MFR> mutateAll(Function<MFP, MFR> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<E> newSet = new HashSet<>();
        for (E item : set) {
            E newValue = mutateFunction.apply(elementMutatorFactory.apply(item)).build();
            newSet.add(newValue);
        }
        set = newSet;
        return this;
    }

    @Override
    public Set<E> build() {
        this.locked = true;
        return Collections.unmodifiableSet(set);
    }

    @Override
    public Set<E> buildCopy() {
        return Collections.unmodifiableSet(new HashSet<>(set));
    }
}