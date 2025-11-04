package io.github.larsarv.jrmg.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class MutableRecordListMutatorImpl<T, E extends RecordMutator<T>> implements MutableRecordListMutator<T, E> {
    private final List<T> list;
    private final Function<T, E> elementMutatorFactory;

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
