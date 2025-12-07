package io.github.larsarv.jrmg.api;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for a nested set mutator.
 * <p>
 * Extends {@link SimpleSetMtor} to provide methods for mutating nested elements using a builder.
 *
 * @param <ELEMENT> the type of elements in the set
 * @param <BUILDER> the type of the builder for the elements
 */
public interface NestedSetMtor<ELEMENT, BUILDER> extends SimpleSetMtor<ELEMENT> {
    @Override
    NestedSetMtor<ELEMENT, BUILDER> add(ELEMENT record);

    @Override
    NestedSetMtor<ELEMENT, BUILDER> remove(ELEMENT record);

    @Override
    NestedSetMtor<ELEMENT, BUILDER> filter(Predicate<ELEMENT> filterFunction);

    @Override
    NestedSetMtor<ELEMENT, BUILDER> update(ELEMENT item, SimpleFunction<ELEMENT> mutateFunction);

    @Override
    NestedSetMtor<ELEMENT, BUILDER> updateAll(SimpleFunction<ELEMENT> mutateFunction);

    /**
     * Adds a new element to the set, created by the provided builder function.
     *
     * @param mutateFunction a function that accepts a builder and returns a builder for the new element
     * @return this mutator
     */
    NestedSetMtor<ELEMENT, BUILDER> add(Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Mutates an element in the set using the provided function.
     *
     * @param item           the element to mutate
     * @param mutateFunction a function that accepts a builder for the existing element and returns a builder for the mutated element
     * @return this mutator
     */
    NestedSetMtor<ELEMENT, BUILDER> mutate(ELEMENT item, Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Mutates all elements in the set using the provided function.
     *
     * @param mutateFunction a function that accepts a builder for the existing element and returns a builder for the mutated element
     * @return this mutator
     */
    NestedSetMtor<ELEMENT, BUILDER> mutateAll(Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Builds the final immutable set.
     *
     * @return the immutable set
     */
    Set<ELEMENT> build();
}