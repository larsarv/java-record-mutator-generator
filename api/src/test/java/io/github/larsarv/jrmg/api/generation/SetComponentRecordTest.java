package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetComponentRecordTest {
    private static final SetComponentRecord TEST_RECORD = new SetComponentRecord(
            Set.of("apple", "banana", "cherry")
    );

    @Test
    void setComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = SetComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        SetComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.setComponent(), builtRecord.setComponent());
    }

    @Test
    void setComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = SetComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.setComponent(), mutator.getSetComponent());
    }

    @Test
    void setComponentShouldGetNewValue() {
        // Arrange
        var mutator = SetComponentRecordMutator.mutator();
        Set<String> value = Set.of();
        // Act
        SetComponentRecord builtRecord = mutator
                .setSetComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.setComponent());
    }

    @Test
    void setComponentShouldRemoveElement() {
        // Arrange
        var mutator = SetComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        SetComponentRecord builtRecord = mutator
                .mutateSetComponent(set -> set
                                .remove("banana"))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains("banana"));
        assertEquals(2, builtRecord.setComponent().size());
    }

}