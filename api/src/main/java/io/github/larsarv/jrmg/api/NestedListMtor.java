package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for a nested list mutator.
 * <p>
 * Extends {@link SimpleListMtor} to provide methods for mutating nested elements using a builder.
 *
 * @param <ELEMENT> the type of elements in the list
 * @param <BUILDER> the type of the builder for the elements
 */
public interface NestedListMtor<ELEMENT, BUILDER> extends SimpleListMtor<ELEMENT> {
    @Override
    ELEMENT get(int index);

    @Override
    NestedListMtor<ELEMENT, BUILDER> set(int index, ELEMENT record);

    @Override
    NestedListMtor<ELEMENT, BUILDER> add(ELEMENT item);

    @Override
    NestedListMtor<ELEMENT, BUILDER> remove(int index);

    @Override
    NestedListMtor<ELEMENT, BUILDER> filter(Predicate<ELEMENT> filterFunction);

    @Override
    NestedListMtor<ELEMENT, BUILDER> updateAll(IndexedFunction<ELEMENT, ELEMENT> mutateFunction);

    @Override
    NestedListMtor<ELEMENT, BUILDER> sort(Comparator<? super ELEMENT> comparator);

    @Override
    NestedListMtor<ELEMENT, BUILDER> move(int fromIndex, int toIndex);

    /**
     * Appends a new element to the list, created by the provided builder function.
     *
     * @param mutateFunction a function that accepts a builder and returns a builder for the new element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> add(Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Replaces the element at the specified position in the list with the result of the provided builder.
     *
     * @param index   the index of the element to replace
     * @param mutator the builder for the new element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> set(int index, Builder<ELEMENT> mutator);

    /**
     * Mutates the element at the specified position in the list using the provided function.
     *
     * @param index          the index of the element to mutate
     * @param mutateFunction a function that accepts a builder for the existing element and returns a builder for the mutated element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> mutate(int index, Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Mutates all elements in the list using the provided function.
     *
     * @param mutateFunction a function that accepts the index and a builder for the existing element, and returns a builder for the mutated element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> mutateAll(IndexedFunction<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Finds the first element matching the predicate and mutates it using the provided function.
     *
     * @param predicate      the predicate to match the element
     * @param mutateFunction a function that accepts a builder for the matching element and returns a builder for the mutated element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> findFirstAndMutate(Predicate<ELEMENT> predicate, Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Finds all elements matching the predicate and mutates them using the provided function.
     *
     * @param predicate      the predicate to match elements
     * @param mutateFunction a function that accepts a builder for the matching element and returns a builder for the mutated element
     * @return this mutator
     */
    NestedListMtor<ELEMENT, BUILDER> findAllAndMutate(Predicate<ELEMENT> predicate, Function<BUILDER, Builder<ELEMENT>> mutateFunction);

    /**
     * Builds the final immutable list.
     *
     * @return the immutable list
     */
    List<ELEMENT> build();
}