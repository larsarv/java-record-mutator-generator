package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithObjectComponentsTest {
    @Test
    void shouldCreateRecordWithStringComponent() {
        // Act
        CtorRecordWithObjectComponents builtRecord = CtorRecordWithObjectComponentsCtor.constructor()
                .setStringComponent("test string")
                .setObjectComponent(new Object())
                .build();
        // Assert
        assertEquals("test string", builtRecord.stringComponent());
    }

    @Test
    void shouldCreateRecordWithObjectComponent() {
        // Arrange
        Object testObject = new Object();
        // Act
        CtorRecordWithObjectComponents builtRecord = CtorRecordWithObjectComponentsCtor.constructor()
                .setStringComponent("test string")
                .setObjectComponent(testObject)
                .build();
        // Assert
        assertSame(testObject, builtRecord.objectComponent());
    }

    @Test
    void shouldCreateRecordWithNullComponents() {
        // Act
        CtorRecordWithObjectComponents builtRecord = CtorRecordWithObjectComponentsCtor.constructor()
                .setStringComponent(null)
                .setObjectComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.stringComponent());
        assertEquals(null, builtRecord.objectComponent());
    }
}