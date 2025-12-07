package io.github.larsarv.jrmg.api;

/**
 * Functional interface for a simple function.
 *
 * @param <T> the type of the input and output
 */
@FunctionalInterface
public interface SimpleFunction<T> {
    /**
     * Applies this function to the given argument.
     *
     * @param item the function argument
     * @return the function result
     */
    T apply(T item);
}
