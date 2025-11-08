package io.github.larsarv.jrmg.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * An implementation of {@link SimpleListMutator} that provides a fluent API for mutating a list.
 * <p>
 * This implementation maintains a mutable list internally and provides methods to
 * modify its contents. It allows for adding, removing, filtering, and updating elements of the list.
 * <p>
 * This implementation maintains a mutable list internally and provides methods to
 * modify its contents.
 * <p>
 * The constructor accepts a list, which is copied to the internal list. If the provided list is null,
 * an empty list is created.
 * <p>
 * This class is designed for use in fluent APIs where operations are chained together before finalizing
 * the result with {@link #build()}.
 *
 * @param <T> the type of elements in the list
 */
public class SimpleListMutatorImpl<T> implements SimpleListMutator<T> {
    private final List<T> list;

    /**
     * Constructs a new instance of SimpleListMutatorImpl for the specified list and element mutator factory.
     *
     * @param list the list to be copied into the internal mutable list; may be {@code null}
     */
    public SimpleListMutatorImpl(List<T> list) {
        this.list = list == null ? new ArrayList<>(): new ArrayList<>(list);
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
    public SimpleListMutator<T> set(int index, T record) {
        list.set(index, record);
        return this;
    }

    @Override
    public SimpleListMutator<T> add(T record) {
        list.add(record);
        return this;
    }

    @Override
    public SimpleListMutator<T> remove(int index) {
        list.remove(index);
        return this;
    }

    @Override
    public SimpleListMutator<T> filter(Predicate<T> filterFunction) {
        list.removeIf(t -> !filterFunction.test(t));
        return this;
    }

    @Override
    public SimpleListMutator<T> updateAll(IndexedFunction<T> indexedMutateFunction) {
        for (int index = 0; index != list.size(); ++index) {
            T orgItem = list.get(index);
            T newItem = indexedMutateFunction.apply(index, orgItem);
            if (newItem != orgItem) {
                list.set(index, newItem);
            }
        }
        return this;
    }


    @Override
    public SimpleListMutator<T> sort(Comparator<? super T> comparator) {
        list.sort(comparator);
        return this;
    }

    @Override
    public SimpleListMutator<T> move(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= list.size() || toIndex < 0 || toIndex >= list.size()) {
            throw new IndexOutOfBoundsException("Index: " + fromIndex + ", Size: " + list.size());
        }
        T item = list.remove(fromIndex);
        list.add(toIndex, item);
        return this;
    }

    @Override
    public List<T> build() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
