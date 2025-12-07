package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MtorRecordWithMtorRecordComponentTest {
    private static final MtorRecordWithPrimitiveComponents COMPONENT_RECORD = new MtorRecordWithPrimitiveComponents(
            true,
            (byte) 1,
            'a',
            (short) 1,
            1,
            1L,
            1.0F,
            1.0
    );

    private static final MtorRecordWithMtorRecordComponent TEST_RECORD = new MtorRecordWithMtorRecordComponent(COMPONENT_RECORD);

    @Test
    void componentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithMtorRecordComponentMtor.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.component(), mutator.getComponent());
    }

    @Test
    void componentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithMtorRecordComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithMtorRecordComponent builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.component(), builtRecord.component());
    }

    @Test
    void componentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithMtorRecordComponentMtor.mutator();
        // Act
        MtorRecordWithMtorRecordComponent builtRecord = mutator
                .setComponent(COMPONENT_RECORD)
                .build();
        // Assert
        assertSame(COMPONENT_RECORD, builtRecord.component());
    }

    @Test
    void componentMutatorShouldMutateOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithMtorRecordComponentMtor.mutator();
        // Act
        MtorRecordWithMtorRecordComponent builtRecord = mutator
                .mutateComponent(component -> component
                        .setBooleanComponent(true))
                .build();
        // Assert
        assertTrue(builtRecord.component().booleanComponent());
    }

    @Test
    void componentSetterShouldReturnNewValue() {
        // Arrange
        var mutator = MtorRecordWithMtorRecordComponentMtor.mutator();
        mutator.setComponent(COMPONENT_RECORD);
        // Act
        MtorRecordWithMtorRecordComponent builtRecord = mutator
                .setComponent(component -> component)
                .build();
        // Assert
        assertFalse(builtRecord.component().booleanComponent());
        assertEquals(0, builtRecord.component().byteComponent());
    }

}