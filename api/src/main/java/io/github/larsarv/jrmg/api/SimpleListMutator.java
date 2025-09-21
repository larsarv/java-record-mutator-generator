package io.github.larsarv.jrmg.api;

import java.util.List;
import java.util.function.Predicate;

public interface SimpleListMutator<T> {
    int size();
    T get(int index);
    SimpleListMutator<T> set(int index, T record);
    SimpleListMutator<T> add(T record);
    SimpleListMutator<T> remove(int index);
    SimpleListMutator<T> filter(Predicate<T> filterFunction);
    SimpleListMutator<T> updateAll(IndexedFunction<T> indexedMutateFunction);

    List<T> build();
}
