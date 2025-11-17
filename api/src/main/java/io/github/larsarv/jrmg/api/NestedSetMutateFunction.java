package io.github.larsarv.jrmg.api;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.Set} with a record annotated with
 * {@link GenerateMutator} as the element type.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * This interface is used by generated record mutators.
 *
 * @param <E> the type of elements stored in the set.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
@FunctionalInterface
public interface NestedSetMutateFunction<E, MFP, MFR extends Builder<E>> {
    /**
     * Applies a mutation operation with the provided {@link NestedSetMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedSetMutator<E, MFP, MFR> mutate(NestedSetMutator<E, MFP, MFR> mutator);
}
