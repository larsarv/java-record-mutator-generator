package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ListListComponentRecordTest {
    private static final ListListComponentRecord TEST_RECORD = new ListListComponentRecord(
            Arrays.asList(List.of("apple"), List.of("banana"), List.of("cherry"))
    );

    @Test
    void listComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = ListListComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        ListListComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.listComponent(), builtRecord.listComponent());
    }

    @Test
    void listComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = ListListComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.listComponent(), mutator.getListComponent());
    }

    @Test
    void listComponentShouldGetNewValue() {
        // Arrange
        var mutator = ListListComponentRecordMutator.mutator();
        List<List<String>> value = List.of(List.of("apple"), List.of("banana"));
        // Act
        ListListComponentRecord builtRecord = mutator
                .setListComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent());
    }

    @Test
    void listComponentShouldSetFirstElementOfSecondElement() {
        // Arrange
        var mutator = ListListComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        ListListComponentRecord builtRecord = mutator
                .mutateListComponent(list1 -> list1
                        .mutate(1, list2 -> list2
                                .set(0, "apple")))
                .build();
        // Assert
        assertEquals("apple", builtRecord.listComponent().get(1).get(0));
    }

}