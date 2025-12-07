package io.github.larsarv.jrmg.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface for a simple map mutator.
 * <p>
 * Provides methods to modify a map in a fluent manner.
 *
 * @param <KEY>   the type of keys in the map
 * @param <VALUE> the type of values in the map
 */
public interface SimpleMapMtor<KEY, VALUE> extends Builder<Map<KEY, VALUE>> {

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    int size();

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
     */
    VALUE get(KEY key);

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key the key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     */
    boolean containsKey(KEY key);

    /**
     * Returns {@code true} if this map maps one or more keys to the specified value.
     *
     * @param value the value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the specified value
     */
    boolean containsValue(VALUE value);

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    Set<KEY> keySet();

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     *
     * @return a collection view of the values contained in this map
     */
    Collection<VALUE> values();

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> put(KEY key, VALUE value);

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key the key whose mapping is to be removed from the map
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> remove(KEY key);

    /**
     * Removes all entries of the map that satisfy the given predicate.
     *
     * @param filterFunction a predicate which returns {@code true} for entries to be removed
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> filter(BiFunction<KEY, VALUE, Boolean> filterFunction);

    /**
     * Updates the value for the specified key using the provided function.
     *
     * @param key            the key whose value is to be updated
     * @param mutateFunction the function to apply to the existing value
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> update(KEY key, Function<VALUE, VALUE> mutateFunction);

    /**
     * Updates all values in the map using the provided function.
     *
     * @param mutateFunction the function to apply to each key-value pair
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> updateAll(BiFunction<KEY, VALUE, VALUE> mutateFunction);

    /**
     * Copies all of the mappings from the specified map to this map.
     *
     * @param map mappings to be stored in this map
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> putAll(Map<? extends KEY, ? extends VALUE> map);


    /**
     * Removes all of the mappings from this map.
     *
     * @return this mutator
     */
    SimpleMapMtor<KEY, VALUE> clear();

    /**
     * Builds the final immutable map.
     *
     * @return the immutable map
     */
    @Override
    Map<KEY, VALUE> build();

    /**
     * Builds a copy of the map.
     *
     * @return a new map containing the entries of the mutated map
     */
    Map<KEY, VALUE> buildCopy();
}