package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CtorRecordWithPrimitiveComponentsTest {
    private static final CtorRecordWithPrimitiveComponents TEST_RECORD = new CtorRecordWithPrimitiveComponents(
            true,
            (byte) 1,
            'a',
            (short) 1,
            1,
            1L,
            1.0F,
            1.0
    );

    @Test
    void shouldCreateRecordWithBooleanComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertTrue(builtRecord.booleanComponent());
    }

    @Test
    void shouldCreateRecordWithByteComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 10)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals((byte) 10, builtRecord.byteComponent());
    }

    @Test
    void shouldCreateRecordWithCharComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('x')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals('x', builtRecord.charComponent());
    }

    @Test
    void shouldCreateRecordWithShortComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 100)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals((short) 100, builtRecord.shortComponent());
    }

    @Test
    void shouldCreateRecordWithIntComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(10)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals(10, builtRecord.intComponent());
    }

    @Test
    void shouldCreateRecordWithLongComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(100L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals(100L, builtRecord.longComponent());
    }

    @Test
    void shouldCreateRecordWithFloatComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(10.5f)
                .setDoubleComponent(1.0)
                .build();
        // Assert
        assertEquals(10.5f, builtRecord.floatComponent(), 0.0001);
    }

    @Test
    void shouldCreateRecordWithDoubleComponent() {
        // Act
        CtorRecordWithPrimitiveComponents builtRecord = CtorRecordWithPrimitiveComponentsCtor.constructor()
                .setBooleanComponent(true)
                .setByteComponent((byte) 1)
                .setCharComponent('a')
                .setShortComponent((short) 1)
                .setIntComponent(1)
                .setLongComponent(1L)
                .setFloatComponent(1.0F)
                .setDoubleComponent(10.5)
                .build();
        // Assert
        assertEquals(10.5, builtRecord.doubleComponent(), 0.001);
    }
}