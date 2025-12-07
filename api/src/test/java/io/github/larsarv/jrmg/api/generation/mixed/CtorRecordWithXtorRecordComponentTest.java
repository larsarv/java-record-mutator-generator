package io.github.larsarv.jrmg.api.generation.mixed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithXtorRecordComponentTest {
    @Test
    void shouldCreateRecordWithNullComponent() {
        // Act
        CtorRecordWithXtorRecordComponent builtRecord = CtorRecordWithXtorRecordComponentCtor.constructor()
                .setComponent(null)
                .build();

        // Assert
        assertEquals(null, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithMtorRecordComponent() {
        // Arrange
        XtorRecordWithStringComponent testComponent = XtorRecordWithStringComponentMtor.mutator()
                .setValue("test")
                .build();

        // Act
        CtorRecordWithXtorRecordComponent builtRecord = CtorRecordWithXtorRecordComponentCtor.constructor()
                .setComponent(testComponent)
                .build();

        // Assert
        assertSame(testComponent, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithConstructedMtorRecordComponent() {
        // Act
        CtorRecordWithXtorRecordComponent builtRecord = CtorRecordWithXtorRecordComponentCtor.constructor()
                .constructComponent(ctor -> ctor.setValue("constructed"))
                .build();

        // Assert
        assertEquals("constructed", builtRecord.component().value());
    }
}