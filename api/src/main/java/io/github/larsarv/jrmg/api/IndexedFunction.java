package io.github.larsarv.jrmg.api;

@FunctionalInterface
public interface IndexedFunction<T> {
    public T apply(int index, T item);
}
