package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotatedRecordListComponentRecordTest {
    private static final AnnotatedRecordListComponentRecord TEST_RECORD = new AnnotatedRecordListComponentRecord(
            Arrays.asList(
                    PrimitiveComponentRecordMutator.mutator().build(),
                    PrimitiveComponentRecordMutator.mutator().build(),
                    PrimitiveComponentRecordMutator.mutator().build())
    );

    @Test
    void listComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordListComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        AnnotatedRecordListComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.listComponent(), builtRecord.listComponent());
    }

    @Test
    void listComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordListComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.listComponent(), mutator.getListComponent());
    }

    @Test
    void listComponentShouldGetNewValue() {
        // Arrange
        var mutator = AnnotatedRecordListComponentRecordMutator.mutator();
        List<PrimitiveComponentRecord> value = new ArrayList<>();
        // Act
        AnnotatedRecordListComponentRecord builtRecord = mutator
                .setListComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent());
    }

    @Test
    void listComponentShouldSetSecondElement() {
        // Arrange
        var mutator = AnnotatedRecordListComponentRecordMutator.mutator(TEST_RECORD);
        PrimitiveComponentRecord value = PrimitiveComponentRecordMutator.mutator().build();
        // Act
        AnnotatedRecordListComponentRecord builtRecord = mutator
                .mutateListComponent(list -> list
                                .set(1, value))
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent().get(1));
    }

    @Test
    void listComponentShouldMutateSecondElement() {
        // Arrange
        var mutator = AnnotatedRecordListComponentRecordMutator.mutator(TEST_RECORD);
        PrimitiveComponentRecord value = PrimitiveComponentRecordMutator.mutator().build();
        // Act
        AnnotatedRecordListComponentRecord builtRecord = mutator
                .mutateListComponent(list -> list
                        .mutate(1, record -> record
                                .setBooleanComponent(true)))
                .build();
        // Assert
        assertTrue(builtRecord.listComponent().get(1).booleanComponent());
    }

}