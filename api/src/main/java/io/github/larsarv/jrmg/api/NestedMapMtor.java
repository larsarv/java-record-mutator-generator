package io.github.larsarv.jrmg.api;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface for a nested map mutator.
 * <p>
 * Extends {@link SimpleMapMtor} to provide methods for mutating nested values using a builder.
 *
 * @param <KEY>     the type of keys in the map
 * @param <VALUE>   the type of values in the map
 * @param <BUILDER> the type of the builder for the values
 */
public interface NestedMapMtor<KEY, VALUE, BUILDER> extends SimpleMapMtor<KEY, VALUE> {
    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> put(KEY key, VALUE value);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> remove(KEY key);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> filter(BiFunction<KEY, VALUE, Boolean> filterFunction);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> update(KEY key, Function<VALUE, VALUE> mutateFunction);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> updateAll(BiFunction<KEY, VALUE, VALUE> mutateFunction);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> putAll(Map<? extends KEY, ? extends VALUE> map);

    @Override
    NestedMapMtor<KEY, VALUE, BUILDER> clear();

    /**
     * Associates the specified value with the specified key in this map, created by the provided builder function.
     *
     * @param key            the key with which the specified value is to be associated
     * @param mutateFunction a function that accepts a builder and returns a builder for the new value
     * @return this mutator
     */
    NestedMapMtor<KEY, VALUE, BUILDER> put(KEY key, Function<BUILDER, Builder<VALUE>> mutateFunction);

    /**
     * Mutates the value associated with the specified key using the provided function.
     *
     * @param key            the key whose associated value is to be mutated
     * @param mutateFunction a function that accepts a builder for the existing value and returns a builder for the mutated value
     * @return this mutator
     */
    NestedMapMtor<KEY, VALUE, BUILDER> mutateValue(KEY key, Function<BUILDER, Builder<VALUE>> mutateFunction);

    /**
     * Mutates all values in the map using the provided function.
     *
     * @param mutateFunction a function that accepts the key and a builder for the existing value, and returns a builder for the mutated value
     * @return this mutator
     */
    NestedMapMtor<KEY, VALUE, BUILDER> mutateAllValues(BiFunction<KEY, BUILDER, Builder<VALUE>> mutateFunction);

    /**
     * Builds the final immutable map.
     *
     * @return the immutable map
     */
    Map<KEY, VALUE> build();
}