package io.github.larsarv.jrmg.api;

/**
 * A functional interface implemented by all record mutators.
 * Implementations provide a fluent API to set record components and ultimately build a new record instance.
 *
 * @param <T> the type of record being built
 */
public interface RecordMutator<T> {
    /**
     * Returns a new immutable record instance.
     * <p>
     * This method must be called to complete the mutation process. Any futher operations on the record mutator will
     * not affect the retuned record.
     *
     * @return the immutable record instance with the applied mutations
     */
    T build();
}
