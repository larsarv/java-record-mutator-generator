package io.github.larsarv.jrmg.test.project;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotatedRecordListComponentRecordTest {
    private static final AnnotatedRecordListComponentRecord TEST_RECORD = new AnnotatedRecordListComponentRecord(
            Arrays.asList(new PrimitiveComponentRecordMutator().build(), new PrimitiveComponentRecordMutator().build(), new PrimitiveComponentRecordMutator().build())
    );

    @Test
    void listComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = new AnnotatedRecordListComponentRecordMutator(TEST_RECORD);
        // Act
        AnnotatedRecordListComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.listComponent(), builtRecord.listComponent());
    }

    @Test
    void listComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = new AnnotatedRecordListComponentRecordMutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.listComponent(), mutator.getListComponent());
    }

    @Test
    void listComponentShouldGetNewValue() {
        // Arrange
        var mutator = new AnnotatedRecordListComponentRecordMutator();
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
        var mutator = new AnnotatedRecordListComponentRecordMutator(TEST_RECORD);
        PrimitiveComponentRecord value = new PrimitiveComponentRecordMutator().build();
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
        var mutator = new AnnotatedRecordListComponentRecordMutator(TEST_RECORD);
        PrimitiveComponentRecord value = new PrimitiveComponentRecordMutator().build();
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