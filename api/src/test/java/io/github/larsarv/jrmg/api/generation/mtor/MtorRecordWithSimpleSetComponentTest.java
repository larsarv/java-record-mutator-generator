package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MtorRecordWithSimpleSetComponentTest {
    private static final MtorRecordWithSimpleSetComponent TEST_RECORD = new MtorRecordWithSimpleSetComponent(
            Set.of("apple", "banana", "cherry")
    );

    @Test
    void setComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithSimpleSetComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithSimpleSetComponent builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.setComponent(), builtRecord.setComponent());
    }

    @Test
    void setComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithSimpleSetComponentMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.setComponent(), mutator.getSetComponent());
    }

    @Test
    void setComponentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithSimpleSetComponentMtor.mutator();
        Set<String> value = Set.of();
        // Act
        MtorRecordWithSimpleSetComponent builtRecord = mutator
                .setSetComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.setComponent());
    }

    @Test
    void setComponentShouldRemoveElement() {
        // Arrange
        var mutator = MtorRecordWithSimpleSetComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithSimpleSetComponent builtRecord = mutator
                .mutateSetComponent(set -> set
                        .remove("banana"))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains("banana"));
        assertEquals(2, builtRecord.setComponent().size());
    }

}