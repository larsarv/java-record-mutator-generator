package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithCtorRecordComponentTest {
    @Test
    void shouldCreateRecordWithCtorRecordComponent() {
        // Arrange
        CtorRecordWithPrimitiveComponents testComponent = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Act
        CtorRecordWithCtorRecordComponent builtRecord = CtorRecordWithCtorRecordComponentCtor.constructor()
                .setComponent(testComponent)
                .build();
        // Assert
        assertSame(testComponent, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithNullCtorRecordComponent() {
        // Act
        CtorRecordWithCtorRecordComponent builtRecord = CtorRecordWithCtorRecordComponentCtor.constructor()
                .setComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithConstructedCtorRecordComponent() {
        // Act
        CtorRecordWithCtorRecordComponent builtRecord = CtorRecordWithCtorRecordComponentCtor.constructor()
                .constructComponent(ctor -> ctor
                        .setBooleanComponent(true)
                        .setByteComponent((byte) 1)
                        .setCharComponent('a')
                        .setShortComponent((short) 1)
                        .setIntComponent(1)
                        .setLongComponent(1L)
                        .setFloatComponent(1.0F)
                        .setDoubleComponent(1.0))
                .build();
        // Assert
        assertEquals(true, builtRecord.component().booleanComponent());
        assertEquals((byte) 1, builtRecord.component().byteComponent());
        assertEquals('a', builtRecord.component().charComponent());
        assertEquals((short) 1, builtRecord.component().shortComponent());
        assertEquals(1, builtRecord.component().intComponent());
        assertEquals(1L, builtRecord.component().longComponent());
        assertEquals(1.0F, builtRecord.component().floatComponent(), 0.0001);
        assertEquals(1.0, builtRecord.component().doubleComponent(), 0.001);
    }
}