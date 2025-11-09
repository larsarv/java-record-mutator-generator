package io.github.larsarv.jrmg.api;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.Set} with a record annotated with
 * {@link GenerateMutator} as the element type.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * This interface is used by generated record mutators.
 *
 * @param <T> the type of records managed by the mutator
 * @param <M> the type of record mutator used to create or mutate the record
 */
@FunctionalInterface
public interface NestedSetMutateFunction<T, M extends Mutator<T>> {
    /**
     * Applies a mutation operation with the provided {@link NestedSetMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedSetMutator<T, M> mutate(NestedSetMutator<T, M> mutator);
}
