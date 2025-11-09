package io.github.larsarv.jrmg.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface for a mutable map that allows for fluent, chainable operations to modify its contents.
 * <p>
 * This interface provides methods to put, remove, filter, and update entries in the map.
 * <p>
 * The interface supports type parameters {@code K} for key type and {@code V} for value type.
 * <p>
 * The {@link #build()} method finalizes all mutations and returns an immutable map.
 *
 * @param <K> the type of keys in the map
 * @param <V> the type of values in the map
 */
public interface SimpleMapMutator<K, V> extends Mutator<Map<K, V>> {
    /**
     * Returns the number of key-value mappings in the map.
     *
     * @return the number of key-value mappings in the map
     */
    int size();

    /**
     * Checks whether the map contains any key-value mappings.
     *
     * @return true if the map is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    V get(K key);

    /**
     * Checks whether the map contains a mapping for the specified key.
     * <p>
     * This method returns true if the map contains a mapping for the specified key, otherwise false.
     *
     * @param key the key whose presence in this map is to be tested
     * @return true if the map contains a mapping for the specified key, false otherwise
     */
    boolean containsKey(K key);

    /**
     * Checks whether the map contains one or more keys mapping to the specified value.
     * <p>
     * This method returns true if the map maps one or more keys to the specified value, otherwise false.
     *
     * @param value the value whose presence in this map is to be tested
     * @return true if the map maps one or more keys to the specified value, false otherwise
     */
    boolean containsValue(V value);

    /**
     * Returns a an unmodifiable Set view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    Set<K> keySet();

    /**
     * Returns an unmodifiable collection view of the values mapped by this mutator.
     *
     * @return an unmodifiable view of the values mapped by this mutator
     */
    Collection<V> values();

    /**
     * Associates the specified value with the specified key in the map.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> put(K key, V value);

    /**
     * Removes the mapping for a key from this map if it is present.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * Returns the value to which this map previously associated the key,
     * or null if the map contained no mapping for the key.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    SimpleMapMutator<K, V> remove(K key);

    /**
     * Filters the map by removing entries that do not satisfy the given predicate.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     * <p>
     * The predicate is applied to each key-value pair in the map. If the predicate returns false
     * for an entry, that entry is removed from the map.
     *
     * @param filterFunction the predicate used to determine whether an entry should be retained
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> filter(BiFunction<K, V, Boolean> filterFunction);

    /**
     * Updates the map by applying the given mutation function to the specified key-value pair.
     * <p>
     * If the key is present in the map, the value is replaced with the result of applying the mutation function.
     * If the key is not present, it is not modified.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param key the key of the entry to update
     * @param mutateFunction the function to apply to the current value, accepting the current value and returning the modified value
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> update(K key, Function<V, V> mutateFunction);

    /**
     * Updates the map by applying the given mutation function to all key-value pairs.
     * <p>
     * The function is applied to each value in the map with its corresponding key.
     * The result of the function becomes the new value for that key.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param mutateFunction the function to apply to each key-value pair, accepting the key and current value and returning the modified value
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> updateAll(BiFunction<K, V, V> mutateFunction);

    /**
     * Puts all of the mappings from the specified map to this map.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     *
     * @param map mappings to be stored in this map
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> putAll(Map<? extends K, ? extends V> map);


    /**
     * Removes all key-value mappings from the map.
     * <p>
     * This operation modifies the map in place and returns a reference to this mutator
     * for method chaining.
     *
     * @return this mutator instance for method chaining
     */
    SimpleMapMutator<K, V> clear();

    /**
     * Finalizes the mutable map and returns an immutable copy.
     * <p>
     * This method creates an immutable map from the current state of the mutator.
     * Any further mutations will trigger a {@link java.lang.IllegalStateException}
     *
     * @return an immutable map containing the current entries
     */
    @Override
    Map<K, V> build();

    /**
     * Finalizes the mutable map and returns an immutable copy.
     * <p>
     * This method creates an immutable map from the current state of the mutator.
     * Any further mutations will not affect the returned map.
     *
     * @return an immutable map containing the current entries
     */
    Map<K, V> buildCopy();
}