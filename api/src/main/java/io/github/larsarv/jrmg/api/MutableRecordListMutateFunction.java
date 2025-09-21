package io.github.larsarv.jrmg.api;

public interface MutableRecordListMutateFunction<T, M extends RecordMutator<T>> {
    MutableRecordListMutator<T, M> mutate(MutableRecordListMutator<T, M> mutator);
}
