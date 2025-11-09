package io.github.larsarv.jrmg.api;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.List} with a mutable element type.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * This interface is used by generated record mutators.
 *
 * @param <T> the type of element managed by the mutator
 * @param <M> the type of mutator used to create or mutate the element
 */
@FunctionalInterface
public interface NestedListMutateFunction<T, M extends Mutator<T>> {
    /**
     * Applies a mutation operation with the provided {@link NestedListMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedListMutator<T, M> mutate(NestedListMutator<T, M> mutator);
}