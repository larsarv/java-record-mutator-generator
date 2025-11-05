package io.github.larsarv.jrmg.api;

/**
 * A functional interface representing a function that accepts an index and an item.
 * It returns a modified item. This is typically used for updating elements in a
 * list or collection where each element can be transformed based on its position.
 *
 * @param <T> the type of the item being processed
 */
@FunctionalInterface
public interface IndexedFunction<T> {
    /**
     * Applies a transformation to an item at the specified index.
     * The function should return the modified item, which may be the same as the original
     * or a new instance if the transformation alters the item's state.
     *
     * @param index the position of the item in the collection being modified
     * @param item the item to be transformed
     * @return the transformed item
     */
    T apply(int index, T item);
}
