package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class MtorRecordWithObjectComponentsTest {
    private static final MtorRecordWithObjectComponents TEST_RECORD = new MtorRecordWithObjectComponents(
            "original string",
            new Object()
    );

    @Test
    void stringComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithObjectComponents builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.stringComponent(), builtRecord.stringComponent());
    }

    @Test
    void stringComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.stringComponent(), mutator.getStringComponent());
    }

    @Test
    void stringComponentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator();
        String value = "string";
        // Act
        MtorRecordWithObjectComponents builtRecord = mutator
                .setStringComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.stringComponent());
    }

    @Test
    void objectComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithObjectComponents builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.objectComponent(), builtRecord.objectComponent());
    }

    @Test
    void objectComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.objectComponent(), mutator.getObjectComponent());
    }

    @Test
    void objectComponentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithObjectComponentsMtor.mutator();
        Object value = new Object();
        // Act
        MtorRecordWithObjectComponents builtRecord = mutator
                .setObjectComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.objectComponent());
    }
}