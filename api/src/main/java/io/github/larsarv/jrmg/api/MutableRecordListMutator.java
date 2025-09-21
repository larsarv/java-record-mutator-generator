package io.github.larsarv.jrmg.api;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MutableRecordListMutator<T, M extends RecordMutator<T>> extends SimpleListMutator<T> {
    @Override
    T get(int index);
    @Override
    MutableRecordListMutator<T, M> set(int index, T record);
    @Override
    MutableRecordListMutator<T, M> add(T record);
    @Override
    MutableRecordListMutator<T, M> remove(int index);
    @Override
    MutableRecordListMutator<T, M> filter(Predicate<T> filterFunction);
    @Override
    MutableRecordListMutator<T, M> updateAll(IndexedFunction<T> mutateFunction);

    MutableRecordListMutator<T, M> add(M recordMutator);
    MutableRecordListMutator<T, M> set(int index, M recordMutator);
    MutableRecordListMutator<T, M> mutate(int index, Function<M, M> mutateFunction);
    MutableRecordListMutator<T, M> mutateAll(IndexedFunction<M> mutateFunction);

    List<T> build();
}
