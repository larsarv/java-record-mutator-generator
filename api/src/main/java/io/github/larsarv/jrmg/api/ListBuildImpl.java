package io.github.larsarv.jrmg.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of {@link NestedListMtor} and {@link NestedListCtor}.
 * <p>
 * This class provides the logic for mutating and constructing lists, including support for nested builders.
 *
 * @param <ELEMENT> the type of elements in the list
 * @param <BUILDER> the type of the builder for the elements
 */
public class ListBuildImpl<ELEMENT, BUILDER> implements NestedListMtor<ELEMENT, BUILDER>, NestedListCtor<ELEMENT, BUILDER> {
    private final List<ELEMENT> list;
    private final Function<ELEMENT, BUILDER> elementBuilderFactory;
    private boolean locked = false;

    /**
     * Constructs a new ListBuildImpl.
     *
     * @param list                  the initial list (can be null)
     * @param elementBuilderFactory a factory function to create builders for elements
     */
    public ListBuildImpl(List<ELEMENT> list, Function<ELEMENT, BUILDER> elementBuilderFactory) {
        this.list = list == null ? new ArrayList<>() : new ArrayList<>(list);
        this.elementBuilderFactory = elementBuilderFactory;
    }

    /**
     * Creates a new builder instance.
     *
     * @param list                  the initial list
     * @param elementMutatorFactory a factory function to create builders for elements
     * @param <E>                   the type of elements
     * @param <B>                   the type of the builder
     * @return a new ListBuildImpl instance
     */
    public static <E, B> ListBuildImpl<E, B> builder(
            List<E> list,
            Function<E, B> elementMutatorFactory
    ) {
        return new ListBuildImpl<>(list, elementMutatorFactory);
    }


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ELEMENT get(int index) {
        return list.get(index);
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> set(int index, ELEMENT record) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, record);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> add(ELEMENT item) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(item);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> remove(int index) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.remove(index);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> filter(Predicate<ELEMENT> filterFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> updateAll(IndexedFunction<ELEMENT, ELEMENT> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        for (int index = 0; index != list.size(); ++index) {
            ELEMENT orgItem = list.get(index);
            ELEMENT newItem = mutateFunction.apply(index, orgItem);
            if (newItem != orgItem) {
                list.set(index, newItem);
            }
        }
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> sort(Comparator<? super ELEMENT> comparator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.sort(comparator);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> move(int fromIndex, int toIndex) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        if (fromIndex < 0 || fromIndex >= list.size() || toIndex < 0 || toIndex >= list.size()) {
            throw new IndexOutOfBoundsException("Index: " + fromIndex + ", Size: " + list.size());
        }
        ELEMENT item = list.remove(fromIndex);
        list.add(toIndex, item);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> set(int index, Builder<ELEMENT> recordMutator) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.set(index, recordMutator.build());
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> add(Function<BUILDER, Builder<ELEMENT>> mutateFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        list.add(mutateFunction.apply(elementBuilderFactory.apply(null)).build());
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> mutate(int index, Function<BUILDER, Builder<ELEMENT>> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        ELEMENT orgValue = list.get(index);
        ELEMENT newValue = modifierFunction.apply(elementBuilderFactory.apply(orgValue)).build();
        list.set(index, newValue);
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> mutateAll(IndexedFunction<BUILDER, Builder<ELEMENT>> modifierFunction) {
        if (locked) {
            throw new IllegalStateException("List is locked and cannot be modified.");
        }
        for (int index = 0; index < list.size(); index++) {
            ELEMENT orgValue = list.get(index);
            ELEMENT newValue = modifierFunction.apply(index, elementBuilderFactory.apply(orgValue)).build();
            list.set(index, newValue);
        }
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> findFirstAndMutate(Predicate<ELEMENT> predicate, Function<BUILDER, Builder<ELEMENT>> mutatorFunction) {
        for (int index = 0; index < list.size(); index++) {
            ELEMENT orgValue = list.get(index);
            if (predicate.test(orgValue)) {
                list.set(index, mutatorFunction.apply(elementBuilderFactory.apply(orgValue)).build());
                return this;
            }
        }
        return this;
    }

    @Override
    public ListBuildImpl<ELEMENT, BUILDER> findAllAndMutate(Predicate<ELEMENT> predicate, Function<BUILDER, Builder<ELEMENT>> mutatorFunction) {
        for (int index = 0; index < list.size(); index++) {
            ELEMENT orgValue = list.get(index);
            if (predicate.test(orgValue)) {
                list.set(index, mutatorFunction.apply(elementBuilderFactory.apply(orgValue)).build());
            }
        }
        return this;
    }

    @Override
    public List<ELEMENT> build() {
        this.locked = true;
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<ELEMENT> buildCopy() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
