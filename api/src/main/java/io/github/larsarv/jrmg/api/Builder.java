package io.github.larsarv.jrmg.api;

/**
 * A functional interface implemented by all mutators.
 * Implementations provide a fluent API to mutate and ultimately build a new instance.
 *
 * @param <T> the type of record being built
 */
public interface Builder<T> {
    /**
     * Returns a new immutable instance.
     *
     * @return the immutable instance with the applied mutations
     */
    T build();
}
