package io.github.larsarv.jrmg.test.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleRecordTest {
    private static final SimpleRecord TEST_RECORD = new SimpleRecord(
            "original string",
            new Object()
    );

    @Test
    void stringComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = new SimpleRecordMutator(TEST_RECORD);
        // Act
        SimpleRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.stringComponent(), builtRecord.stringComponent());
    }

    @Test
    void stringComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = new SimpleRecordMutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.stringComponent(), mutator.getStringComponent());
    }

    @Test
    void stringComponentShouldGetNewValue() {
        // Arrange
        var mutator = new SimpleRecordMutator();
        String value = "string";
        // Act
        SimpleRecord builtRecord = mutator
                .setStringComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.stringComponent());
    }

    @Test
    void objectComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = new SimpleRecordMutator(TEST_RECORD);
        // Act
        SimpleRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.objectComponent(), builtRecord.objectComponent());
    }

    @Test
    void objectComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = new SimpleRecordMutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.objectComponent(), mutator.getObjectComponent());
    }

    @Test
    void objectComponentShouldGetNewValue() {
        // Arrange
        var mutator = new SimpleRecordMutator();
        Object value = new Object();
        // Act
        SimpleRecord builtRecord = mutator
                .setObjectComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.objectComponent());
    }
}