package io.github.larsarv.jrmg.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class SimpleSetMutatorImplTest {

    @Test
    void shouldReturnCorrectSizeWhenGettingSize() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        int size = mutator.size();

        // Assert
        assertEquals(3, size);
    }

    @Test
    void shouldCheckIfSetContainsElement() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        boolean contains = mutator.contains("banana");

        // Assert
        assertTrue(contains);
    }

    @Test
    void shouldAddElementToSet() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.add("cherry");
        Set<String> newSet = mutator.build();

        // Assert
        assertEquals(3, newSet.size());
        assertTrue(newSet.contains("cherry"));
    }

    @Test
    void shouldRemoveElementFromSet() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.remove("banana");
        Set<String> newSet = mutator.build();

        // Assert
        assertEquals(2, newSet.size());
        assertFalse(newSet.contains("banana"));
    }

    @Test
    void shouldFilterSetByProvidedPredicate() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry", "date");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.filter(s -> s.length() > 5);
        Set<String> newSet = mutator.build();

        // Assert
        assertEquals(2, newSet.size());
        assertTrue(newSet.contains("banana"));
        assertTrue(newSet.contains("cherry"));
    }

    @Test
    void shouldUpdateAllElementsUsingIndexedFunction() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.updateAll( item -> "updated_" + item);
        Set<String> newSet = mutator.build();

        // Assert
        assertTrue(newSet.contains("updated_apple"));
        assertTrue(newSet.contains("updated_banana"));
        assertTrue(newSet.contains("updated_cherry"));
    }

    @Test
    void shouldUpdateElementUsingUpdateFunction() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.update("banana", i -> "updated_banana");
        Set<String> newSet = mutator.build();

        // Assert
        assertFalse(newSet.contains("banana"));
        assertTrue(newSet.contains("updated_banana"));
    }

    @Test
    void shouldNotUpdateElementIfItDoesNotExist() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.update("orange", i -> "updated_orange");
        Set<String> newSet = mutator.build();

        // Assert
        assertFalse(newSet.contains("updated_orange"));
        assertEquals(originalSet, newSet);
    }

    @Test
    void shouldReturnImmutableSetOnBuild() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        Set<String> builtSet = mutator.build();

        // Assert
        assertEquals(originalSet, builtSet);
        assertEquals(2, builtSet.size());
    }

    @Test
    void shouldThrowExceptionOnModificationAfterBuild() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.add("grape");
        Set<String> builtSet = mutator.build();

        // Assert
        assertThrows(IllegalStateException.class, () -> mutator.remove("banana"));
        assertDoesNotThrow(() -> mutator.contains("apple"));
    }

    @Test
    void shouldReturnImmutableSetCopyOnBuildCopy() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        Set<String> builtSet = mutator.buildCopy();

        // Assert
        assertEquals(originalSet, builtSet);
        assertEquals(2, builtSet.size());
    }

    @Test
    void shouldNotThrowExceptionOnModificationAfterBuildCopy() {
        // Arrange
        Set<String> originalSet = Set.of("apple", "banana", "cherry");
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(originalSet);

        // Act
        mutator.add("grape");
        Set<String> builtSet = mutator.buildCopy();

        // Assert
        assertDoesNotThrow(() -> mutator.remove("banana"));
    }

    @Test
    void shouldHandleNullSetInConstructor() {
        // Arrange
        SimpleSetMutatorImpl<String> mutator = new SimpleSetMutatorImpl<>(null);

        // Act
        Set<String> builtSet = mutator.build();

        // Assert
        assertEquals(0, builtSet.size());
    }
}