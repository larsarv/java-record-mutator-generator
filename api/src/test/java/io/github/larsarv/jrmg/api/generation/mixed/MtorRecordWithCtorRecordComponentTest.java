package io.github.larsarv.jrmg.api.generation.mixed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MtorRecordWithCtorRecordComponentTest {
    @Test
    void shouldCreateRecordWithMtorRecordComponent() {
        // Arrange
        CtorRecordWithStringComponent testComponent = CtorRecordWithStringComponentCtor.constructor()
                .setValue("test")
                .build();

        // Act
        MtorRecordWithCtorRecordComponent builtRecord = MtorRecordWithCtorRecordComponentMtor.mutator()
                .setComponent(testComponent)
                .build();

        // Assert
        assertSame(testComponent, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithNullComponent() {
        // Act
        MtorRecordWithCtorRecordComponent builtRecord = MtorRecordWithCtorRecordComponentMtor.mutator()
                .setComponent(null)
                .build();

        // Assert
        assertNull(builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithConstructedMtorRecordComponent() {
        // Act
        MtorRecordWithCtorRecordComponent builtRecord = MtorRecordWithCtorRecordComponentMtor.mutator()
                .constructComponent(ctor -> ctor.setValue("constructed"))
                .build();

        // Assert
        assertEquals("constructed", builtRecord.component().value());
    }
}