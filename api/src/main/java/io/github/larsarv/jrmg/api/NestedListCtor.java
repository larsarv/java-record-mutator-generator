package io.github.larsarv.jrmg.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Interface for a nested list constructor.
 * <p>
 * Extends {@link SimpleListCtor} to provide methods for constructing nested elements using a builder.
 *
 * @param <ELEMENT>    the type of elements in the list
 * @param <CFPBUILDER> the type of the builder for the elements
 */
public interface NestedListCtor<ELEMENT, CFPBUILDER> extends SimpleListCtor<ELEMENT> {
    @Override
    NestedListCtor<ELEMENT, CFPBUILDER> add(ELEMENT item);

    @Override
    SimpleListCtor<ELEMENT> sort(Comparator<? super ELEMENT> comparator);

    /**
     * Appends a new element to the list, created by the provided builder function.
     *
     * @param buildFunction a function that accepts a builder and returns a builder for the new element
     * @return this constructor
     */
    NestedListCtor<ELEMENT, CFPBUILDER> add(Function<CFPBUILDER, Builder<ELEMENT>> buildFunction);

    /**
     * Builds the final immutable list.
     *
     * @return the immutable list
     */
    List<ELEMENT> build();
}