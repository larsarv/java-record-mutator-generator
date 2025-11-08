package io.github.larsarv.jrmg.api;

/**
 * A functional interface representing a function that accepts an item.
 * It returns a modified item. This is typically used for updating elements in a set.
 *
 * @param <T> the type of the item being processed
 */
@FunctionalInterface
public interface SimpleFunction<T> {
    /**
     * Applies a transformation to an item.
     * The function should return the modified item, which may be the same as the original
     * or a new instance if the transformation alters the item's state.
     *
     * @param item the item to be transformed
     * @return the transformed item
     */
    T apply(T item);
}
