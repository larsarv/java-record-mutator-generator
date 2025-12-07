package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CtorRecordWithStringComponentTest {
    @Test
    void shouldCreateRecordWithStringValue() {
        // Act
        CtorRecordWithStringComponent builtRecord = CtorRecordWithStringComponentCtor.constructor()
                .setValue("test value")
                .build();
        // Assert
        assertEquals("test value", builtRecord.value());
    }

    @Test
    void shouldCreateRecordWithNullStringValue() {
        // Act
        CtorRecordWithStringComponent builtRecord = CtorRecordWithStringComponentCtor.constructor()
                .setValue(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.value());
    }
}