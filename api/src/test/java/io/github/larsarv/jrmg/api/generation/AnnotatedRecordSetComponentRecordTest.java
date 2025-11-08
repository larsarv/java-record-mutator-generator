package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotatedRecordSetComponentRecordTest {
    public static final PrimitiveComponentRecord ITEM_1 = PrimitiveComponentRecordMutator.mutator().setIntComponent(1).build();
    public static final PrimitiveComponentRecord ITEM_2 = PrimitiveComponentRecordMutator.mutator().setIntComponent(2).build();
    public static final PrimitiveComponentRecord ITEM_3 = PrimitiveComponentRecordMutator.mutator().setIntComponent(3).build();
    public static final PrimitiveComponentRecord ITEM_4 = PrimitiveComponentRecordMutator.mutator().setIntComponent(4).build();
    private static final AnnotatedRecordSetComponentRecord TEST_RECORD = new AnnotatedRecordSetComponentRecord(
            Set.of(
                    ITEM_1,
                    ITEM_2,
                    ITEM_3));

    @Test
    void setComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordSetComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        AnnotatedRecordSetComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.setComponent(), builtRecord.setComponent());
    }

    @Test
    void setComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = AnnotatedRecordSetComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.setComponent(), mutator.getSetComponent());
    }

    @Test
    void setComponentShouldGetNewValue() {
        // Arrange
        var mutator = AnnotatedRecordSetComponentRecordMutator.mutator();
        Set<PrimitiveComponentRecord> value = Set.of();
        // Act
        AnnotatedRecordSetComponentRecord builtRecord = mutator
                .setSetComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.setComponent());
    }

    @Test
    void setComponentShouldRemoveElement() {
        // Arrange
        var mutator = AnnotatedRecordSetComponentRecordMutator.mutator(TEST_RECORD);
        PrimitiveComponentRecord value = PrimitiveComponentRecordMutator.mutator().build();
        // Act
        AnnotatedRecordSetComponentRecord builtRecord = mutator
                .mutateSetComponent(set -> set
                                .remove(ITEM_2))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains(ITEM_2));
    }

    @Test
    void setComponentShouldMutateElement() {
        // Arrange
        var mutator = AnnotatedRecordSetComponentRecordMutator.mutator(TEST_RECORD);
        PrimitiveComponentRecord value = PrimitiveComponentRecordMutator.mutator().build();
        // Act
        AnnotatedRecordSetComponentRecord builtRecord = mutator
                .mutateSetComponent(list -> list
                        .mutate(ITEM_2, record -> record
                                .setIntComponent(4)))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains(ITEM_2));
        assertTrue(builtRecord.setComponent().contains(ITEM_4));
    }

}