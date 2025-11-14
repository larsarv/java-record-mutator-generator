package io.github.larsarv.jrmg.api;

/**
 * A functional interface used at the last step in a mutators constructor pattern.
 *
 * @param <T> the type of record being built
 * @param <M> the type of mutator used to build the record
 */
public interface MutatorConstructor<T, M extends Mutator<T>> {
    /**
     * Finalizes the mutator and returns the constructed record.
     *
     * @return the mutator
     */
    M done();

    /**
     * Finalizes the record construction and returns the built record instance.
     *
     * @return the constructed record instance
     */
    T buildRecord();
}
