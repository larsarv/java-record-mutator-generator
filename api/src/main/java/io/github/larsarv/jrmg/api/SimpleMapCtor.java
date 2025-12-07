package io.github.larsarv.jrmg.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Interface for a simple map constructor.
 * <p>
 * Provides methods to construct a map in a fluent manner.
 *
 * @param <KEY>   the type of keys in the map
 * @param <VALUE> the type of values in the map
 */
public interface SimpleMapCtor<KEY, VALUE> extends Builder<Map<KEY, VALUE>> {
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
     * @return this constructor
     */
    SimpleMapCtor<KEY, VALUE> put(KEY key, VALUE value);

    /**
     * Copies all of the mappings from the specified map to this map.
     *
     * @param map mappings to be stored in this map
     * @return this constructor
     */
    SimpleMapCtor<KEY, VALUE> putAll(Map<? extends KEY, ? extends VALUE> map);

    /**
     * Builds a copy of the map.
     *
     * @return a new map containing the entries of the constructed map
     */
    Map<KEY, VALUE> buildCopy();
}