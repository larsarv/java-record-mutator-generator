package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.function.Function;

/**
 * Interface for a nested map constructor.
 * <p>
 * Extends {@link SimpleMapCtor} to provide methods for constructing nested values using a builder.
 *
 * @param <KEY>     the type of keys in the map
 * @param <VALUE>   the type of values in the map
 * @param <BUILDER> the type of the builder for the values
 */
public interface NestedMapCtor<KEY, VALUE, BUILDER> extends SimpleMapCtor<KEY, VALUE> {
    @Override
    NestedMapCtor<KEY, VALUE, BUILDER> put(KEY key, VALUE value);

    @Override
    NestedMapCtor<KEY, VALUE, BUILDER> putAll(Map<? extends KEY, ? extends VALUE> map);

    /**
     * Associates the specified value with the specified key in this map, created by the provided builder function.
     *
     * @param key           the key with which the specified value is to be associated
     * @param buildFunction a function that accepts a builder and returns a builder for the new value
     * @return this constructor
     */
    NestedMapCtor<KEY, VALUE, BUILDER> put(KEY key, Function<BUILDER, Builder<VALUE>> buildFunction);

    /**
     * Builds the final immutable map.
     *
     * @return the immutable map
     */
    Map<KEY, VALUE> build();
}