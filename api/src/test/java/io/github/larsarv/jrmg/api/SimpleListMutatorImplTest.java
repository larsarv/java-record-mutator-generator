package io.github.larsarv.jrmg.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class SimpleListMutatorImplTest {

    @Test
    void shouldReturnCorrectElementWhenGettingFromList() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        String result = mutator.get(1);

        // Assert
        assertEquals("banana", result);
    }

    @Test
    void shouldSetElementAtSpecifiedIndex() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.set(1, "grape");

        // Assert
        assertEquals("grape", mutator.get(1));
    }

    @Test
    void shouldAddElementToTheEndOfList() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.add("cherry");
        var newList = mutator.build();

        // Assert
        assertEquals(3, newList.size());
        assertTrue(newList.contains("cherry"));
    }

    @Test
    void shouldRemoveElementAtSpecifiedIndex() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.remove(1);

        // Assert
        assertEquals(2, mutator.size());
        assertEquals("apple", mutator.get(0));
        assertEquals("cherry", mutator.get(1));
    }

    @Test
    void shouldFilterListByProvidedPredicate() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry", "date");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.filter(s -> s.length() > 5);
        var newList = mutator.build();

        // Assert
        assertEquals(2, newList.size());
        assertTrue(newList.contains("banana"));
        assertTrue(newList.contains("cherry"));
    }

    @Test
    void shouldUpdateAllElementsUsingIndexedFunction() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.updateAll((index, item) -> {
            if (index == 0) {
                return "updatedApple";
            } else if (index == 1) {
                return "updatedBanana";
            } else {
                return item;
            }
        });

        // Assert
        assertEquals("updatedApple", mutator.get(0));
        assertEquals("updatedBanana", mutator.get(1));
        assertEquals("cherry", mutator.get(2));
    }

    @Test
    void shouldReturnImmutableListCopyOnBuild() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        List<String> builtList = mutator.build();

        // Assert
        assertEquals(originalList, builtList);
        assertEquals(2, builtList.size());
    }

    @Test
    void shouldThrowExceptionOnModificationAfterBuild() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.set(1, "grape");
        List<String> builtList = mutator.build();

        // Assert
        assertThrows(IllegalStateException.class, () -> mutator.move(0, 1));
        assertDoesNotThrow(() -> mutator.get(0));
    }

    @Test
    void shouldReturnImmutableListCopyOnBuildCopy() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        List<String> builtList = mutator.buildCopy();

        // Assert
        assertEquals(originalList, builtList);
        assertEquals(2, builtList.size());
    }

    @Test
    void shouldNotThrowExceptionOnModificationAfterBuildCopy() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.set(1, "grape");
        List<String> builtList = mutator.buildCopy();

        // Assert
        assertDoesNotThrow(() -> mutator.move(0, 1));
    }

    @Test
    void shouldHandleNullListInConstructor() {
        // Arrange
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(null);

        // Act
        List<String> builtList = mutator.build();

        // Assert
        assertEquals(0, builtList.size());
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenSettingOutOfBoundsIndex() {
        // Arrange
        List<String> originalList = List.of("apple");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.set(1, "grape"));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenRemovingOutOfBoundsIndex() {
        // Arrange
        List<String> originalList = List.of("apple");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.remove(1));
    }

    @Test
    void shouldAllowAddingNullElementToTheList() {
        // Arrange
        List<String> originalList = List.of("apple");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.add(null);

        // Assert
        assertEquals(2, mutator.build().size());
        assertTrue(mutator.build().contains(null));
    }

    @Test
    void shouldSortListWithNaturalOrderComparator() {
        // Arrange
        List<String> originalList = Arrays.asList("banana", "apple", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.sort(String::compareTo);

        // Assert
        List<String> builtList = mutator.build();
        assertEquals(Arrays.asList("apple", "banana", "cherry"), builtList);
    }

    @Test
    void shouldMoveElementFromIndex0ToIndex1() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act
        mutator.move(0, 1);

        // Assert
        List<String> builtList = mutator.build();
        assertEquals(Arrays.asList("banana", "apple", "cherry"), builtList);
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenMovingFromIndex0ToIndex3() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.move(0, 3));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenMovingFromIndex2ToIndexMinus1() {
        // Arrange
        List<String> originalList = Arrays.asList("apple", "banana", "cherry");
        SimpleListMutatorImpl<String> mutator = new SimpleListMutatorImpl<>(originalList);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.move(3, -1));
    }
}