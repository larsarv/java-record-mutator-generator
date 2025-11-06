package io.github.larsarv.jrmg.test.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnotatedRecordComponentRecordTest {
    private static final PrimitiveComponentRecord COMPONENT_RECORD = new PrimitiveComponentRecord(
            true,
            (byte) 1,
            'a',
            (short) 1,
            1,
            1L,
            1.0F,
            1.0
    );

    private static final AnnotatedRecordComponentRecord TEST_RECORD = new AnnotatedRecordComponentRecord(COMPONENT_RECORD);

    @Test
    void componentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.component(), mutator.getComponent());
    }

    @Test
    void componentShouldRetainOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        AnnotatedRecordComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.component(), builtRecord.component());
    }

    @Test
    void componentShouldGetNewValue() {
        // Arrange
        var mutator = AnnotatedRecordComponentRecordMutator.mutator();
        // Act
        AnnotatedRecordComponentRecord builtRecord = mutator
                .setComponent(COMPONENT_RECORD)
                .build();
        // Assert
        assertSame(COMPONENT_RECORD, builtRecord.component());
    }

    @Test
    void componentMutatorShouldMutateOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordComponentRecordMutator.mutator();
        // Act
        AnnotatedRecordComponentRecord builtRecord = mutator
                .mutateComponent(component -> component
                        .setBooleanComponent(true))
                .build();
        // Assert
        assertTrue(builtRecord.component().booleanComponent());
    }

}