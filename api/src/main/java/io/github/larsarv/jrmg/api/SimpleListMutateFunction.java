package io.github.larsarv.jrmg.api;

public interface SimpleListMutateFunction<T> {
    SimpleListMutator<T> mutate(SimpleListMutator<T> mutator);
}
