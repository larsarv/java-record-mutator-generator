package io.github.larsarv.jrmg.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of {@link NestedSetMtor} and {@link NestedSetCtor}.
 * <p>
 * This class provides the logic for mutating and constructing sets, including support for nested builders.
 *
 * @param <ELEMENT> the type of elements in the set
 * @param <BUILDER> the type of the builder for the elements
 */
public class SetBuilderImpl<ELEMENT, BUILDER> implements NestedSetMtor<ELEMENT, BUILDER>, NestedSetCtor<ELEMENT, BUILDER> {
    private final Function<ELEMENT, BUILDER> elementBuilderFactory;
    private Set<ELEMENT> set;
    private boolean locked = false;

    /**
     * Constructs a new SetBuilderImpl.
     *
     * @param set                   the initial set (can be null)
     * @param elementBuilderFactory a factory function to create builders for elements
     */
    public SetBuilderImpl(Set<ELEMENT> set, Function<ELEMENT, BUILDER> elementBuilderFactory) {
        this.set = set == null ? new HashSet<>() : new HashSet<>(set);
        this.elementBuilderFactory = elementBuilderFactory;
    }

    /**
     * Creates a new builder instance.
     *
     * @param set                   the initial set
     * @param elementMutatorFactory a factory function to create builders for elements
     * @param <E>                   the type of elements
     * @param <B>                   the type of the builder
     * @return a new SetBuilderImpl instance
     */
    public static <E, B> SetBuilderImpl<E, B> builder(Set<E> set, Function<E, B> elementMutatorFactory) {
        return new SetBuilderImpl<>(set, elementMutatorFactory);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(ELEMENT element) {
        return set.contains(element);
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> add(ELEMENT record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(record);
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> remove(ELEMENT record) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.remove(record);
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> filter(Predicate<ELEMENT> filterFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.removeIf(e -> !filterFunction.test(e));
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> update(ELEMENT record, SimpleFunction<ELEMENT> mutateFunction) {
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
    public SetBuilderImpl<ELEMENT, BUILDER> updateAll(SimpleFunction<ELEMENT> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<ELEMENT> newSet = new HashSet<>();
        for (ELEMENT item : set) {
            ELEMENT newItem = mutateFunction.apply(item);
            newSet.add(newItem);
        }
        set = newSet;
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> add(Function<BUILDER, Builder<ELEMENT>> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        set.add(mutateFunction.apply(elementBuilderFactory.apply(null)).build());
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> mutate(ELEMENT item, Function<BUILDER, Builder<ELEMENT>> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        if (set.contains(item)) {
            set.remove(item);
            set.add(mutateFunction.apply(elementBuilderFactory.apply(item)).build());
        }
        return this;
    }

    @Override
    public SetBuilderImpl<ELEMENT, BUILDER> mutateAll(Function<BUILDER, Builder<ELEMENT>> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("Set is locked and cannot be modified.");
        }
        Set<ELEMENT> newSet = new HashSet<>();
        for (ELEMENT item : set) {
            ELEMENT newValue = mutateFunction.apply(elementBuilderFactory.apply(item)).build();
            newSet.add(newValue);
        }
        set = newSet;
        return this;
    }

    @Override
    public Set<ELEMENT> build() {
        this.locked = true;
        return Collections.unmodifiableSet(set);
    }

    @Override
    public Set<ELEMENT> buildCopy() {
        return Collections.unmodifiableSet(new HashSet<>(set));
    }
}