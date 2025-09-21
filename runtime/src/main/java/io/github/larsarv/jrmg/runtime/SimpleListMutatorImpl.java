package io.github.larsarv.jrmg.runtime;

import io.github.larsarv.jrmg.api.SimpleListMutator;
import io.github.larsarv.jrmg.api.IndexedFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SimpleListMutatorImpl<T> implements SimpleListMutator<T> {
    private final List<T> list;

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
    public List<T> build() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
