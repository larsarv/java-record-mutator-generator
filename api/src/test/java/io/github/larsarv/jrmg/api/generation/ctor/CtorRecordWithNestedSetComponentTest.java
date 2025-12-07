package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithNestedSetComponentTest {
    @Test
    void shouldCreateRecordWithNestedSetComponent() {
        // Arrange
        Set<CtorRecordWithPrimitiveComponents> testSet = Set.of(
                CtorRecordWithPrimitiveComponentsCtor.constructor()
                        .setBooleanComponent(true)
                        .setByteComponent((byte) 1)
                        .setCharComponent('a')
                        .setShortComponent((short) 1)
                        .setIntComponent(1)
                        .setLongComponent(1L)
                        .setFloatComponent(1.0F)
                        .setDoubleComponent(1.0)
                        .build(),
                CtorRecordWithPrimitiveComponentsCtor.constructor()
                        .setBooleanComponent(false)
                        .setByteComponent((byte) 2)
                        .setCharComponent('b')
                        .setShortComponent((short) 2)
                        .setIntComponent(2)
                        .setLongComponent(2L)
                        .setFloatComponent(2.0F)
                        .setDoubleComponent(2.0)
                        .build()
        );
        // Act
        CtorRecordWithNestedSetComponent builtRecord = CtorRecordWithNestedSetComponentCtor.constructor()
                .setSetComponent(testSet)
                .build();
        // Assert
        assertSame(testSet, builtRecord.setComponent());
    }

    @Test
    void shouldCreateRecordWithEmptyNestedSetComponent() {
        // Arrange
        Set<CtorRecordWithPrimitiveComponents> emptySet = Set.of();
        // Act
        CtorRecordWithNestedSetComponent builtRecord = CtorRecordWithNestedSetComponentCtor.constructor()
                .setSetComponent(emptySet)
                .build();
        // Assert
        assertSame(emptySet, builtRecord.setComponent());
        assertEquals(0, builtRecord.setComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullNestedSetComponent() {
        // Act
        CtorRecordWithNestedSetComponent builtRecord = CtorRecordWithNestedSetComponentCtor.constructor()
                .setSetComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.setComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedNestedSetComponent() {
        // Act
        CtorRecordWithNestedSetComponent builtRecord = CtorRecordWithNestedSetComponentCtor.constructor()
                .constructSetComponent(setBuilder -> setBuilder
                        .add(ctor -> ctor
                                .setBooleanComponent(true)
                                .setByteComponent((byte) 1)
                                .setCharComponent('a')
                                .setShortComponent((short) 1)
                                .setIntComponent(1)
                                .setLongComponent(1L)
                                .setFloatComponent(1.0F)
                                .setDoubleComponent(1.0))
                        .add(ctor -> ctor
                                .setBooleanComponent(false)
                                .setByteComponent((byte) 2)
                                .setCharComponent('b')
                                .setShortComponent((short) 2)
                                .setIntComponent(2)
                                .setLongComponent(2L)
                                .setFloatComponent(2.0F)
                                .setDoubleComponent(2.0)))
                .build();
        // Assert
        assertEquals(2, builtRecord.setComponent().size());

        // Find the record with booleanComponent = true
        var trueRecord = builtRecord.setComponent().stream()
                .filter(r -> r.booleanComponent())
                .findFirst()
                .orElseThrow();
        assertEquals(true, trueRecord.booleanComponent());
        assertEquals((byte) 1, trueRecord.byteComponent());
        assertEquals('a', trueRecord.charComponent());
        assertEquals((short) 1, trueRecord.shortComponent());
        assertEquals(1, trueRecord.intComponent());
        assertEquals(1L, trueRecord.longComponent());
        assertEquals(1.0F, trueRecord.floatComponent(), 0.0001);
        assertEquals(1.0, trueRecord.doubleComponent(), 0.001);

        // Find the record with booleanComponent = false
        var falseRecord = builtRecord.setComponent().stream()
                .filter(r -> !r.booleanComponent())
                .findFirst()
                .orElseThrow();
        assertEquals(false, falseRecord.booleanComponent());
        assertEquals((byte) 2, falseRecord.byteComponent());
        assertEquals('b', falseRecord.charComponent());
        assertEquals((short) 2, falseRecord.shortComponent());
        assertEquals(2, falseRecord.intComponent());
        assertEquals(2L, falseRecord.longComponent());
        assertEquals(2.0F, falseRecord.floatComponent(), 0.0001);
        assertEquals(2.0, falseRecord.doubleComponent(), 0.01);
    }
}