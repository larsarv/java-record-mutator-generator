package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CtorRecordWithSimpleSetComponentTest {
    @Test
    void shouldCreateRecordWithSetComponent() {
        // Arrange
        Set<String> testSet = Set.of("apple", "banana", "cherry");
        // Act
        CtorRecordWithSimpleSetComponent builtRecord = CtorRecordWithSimpleSetComponentCtor.constructor()
                .setSetComponent(testSet)
                .build();
        // Assert
        assertSame(testSet, builtRecord.setComponent());
    }

    @Test
    void shouldCreateRecordWithEmptySetComponent() {
        // Arrange
        Set<String> emptySet = Set.of();
        // Act
        CtorRecordWithSimpleSetComponent builtRecord = CtorRecordWithSimpleSetComponentCtor.constructor()
                .setSetComponent(emptySet)
                .build();
        // Assert
        assertSame(emptySet, builtRecord.setComponent());
        assertEquals(0, builtRecord.setComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullSetComponent() {
        // Act
        CtorRecordWithSimpleSetComponent builtRecord = CtorRecordWithSimpleSetComponentCtor.constructor()
                .setSetComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.setComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedSimpleSetComponent() {
        // Act
        CtorRecordWithSimpleSetComponent builtRecord = CtorRecordWithSimpleSetComponentCtor.constructor()
                .constructSetComponent(setBuilder -> setBuilder
                        .add("apple")
                        .add("banana")
                        .add("cherry"))
                .build();
        // Assert
        assertEquals(3, builtRecord.setComponent().size());
        assertTrue(builtRecord.setComponent().contains("apple"));
        assertTrue(builtRecord.setComponent().contains("banana"));
        assertTrue(builtRecord.setComponent().contains("cherry"));
    }
}