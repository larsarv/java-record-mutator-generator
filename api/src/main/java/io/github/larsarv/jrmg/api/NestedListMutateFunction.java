package io.github.larsarv.jrmg.api;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.List} with a mutable element type.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * This interface is used by generated record mutators.
 *
 * @param <E> the type of elements stored in the list.
 * @param <MFP> the type of the mutate function parameter.
 * @param <MFR> the return type mutate function.
 */
@FunctionalInterface
public interface NestedListMutateFunction<E, MFP, MFR extends Builder<E>> {
    /**
     * Applies a mutation operation with the provided {@link NestedListMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    NestedListMutator<E, MFP, MFR> mutate(NestedListMutator<E, MFP, MFR> mutator);
}