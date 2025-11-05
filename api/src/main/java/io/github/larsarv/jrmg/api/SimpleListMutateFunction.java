package io.github.larsarv.jrmg.api;

/**
 * A functional interface that defines a mutation operation on a {@link java.util.List}.
 * <p>
 * The function accepts a mutator instance and returns a modified version of it.
 * <p>
 * This interface is used by generated record mutators.
 * <p>
 * The interface is {@link FunctionalInterface} and can be used with lambda expressions
 * or method references for concise mutation composition.
 *
 * @param <T> the type of elements in the list being mutated
 */
@FunctionalInterface
public interface SimpleListMutateFunction<T> {
    /**
     * Applies a mutation operation with the provided {@link SimpleListMutator} instance.
     *
     * @param mutator the mutator instance to be mutated
     * @return a modified version of the mutator instance with applied mutations
     */
    SimpleListMutator<T> mutate(SimpleListMutator<T> mutator);
}
