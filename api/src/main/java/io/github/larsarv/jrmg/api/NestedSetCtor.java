package io.github.larsarv.jrmg.api;

import java.util.Set;
import java.util.function.Function;

/**
 * Interface for a nested set constructor.
 *
 * @param <ELEMENT> the type of elements in the set
 * @param <BUILDER> the type of the builder for the elements
 */
public interface NestedSetCtor<ELEMENT, BUILDER> extends SimpleSetCtor<ELEMENT> {
    /**
     * Adds an element to the set.
     *
     * @param record the element to add
     * @return this constructor
     */
    @Override
    NestedSetCtor<ELEMENT, BUILDER> add(ELEMENT record);

    /**
     * Adds an element to the set using a builder function.
     *
     * @param buildFunction the function to build the element
     * @return this constructor
     */
    NestedSetCtor<ELEMENT, BUILDER> add(Function<BUILDER, Builder<ELEMENT>> buildFunction);

    Set<ELEMENT> build();
}