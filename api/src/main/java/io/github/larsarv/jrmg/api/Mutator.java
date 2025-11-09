package io.github.larsarv.jrmg.api;

/**
 * A functional interface implemented by all record mutators.
 * Implementations provide a fluent API to set record components and ultimately build a new instance.
 *
 * @param <T> the type of record being built
 */
public interface Mutator<T> {
    /**
     * Returns a new immutable instance.
     * <p>
     * This method must be called to complete the mutation process.
     *
     * @return the immutable instance with the applied mutations
     */
    T build();
}
