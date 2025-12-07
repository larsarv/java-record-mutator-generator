package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MtorRecordWithNestedSetComponentTest {
    public static final MtorRecordWithPrimitiveComponents ITEM_1 = MtorRecordWithPrimitiveComponentsMtor.mutator().setIntComponent(1).build();
    public static final MtorRecordWithPrimitiveComponents ITEM_2 = MtorRecordWithPrimitiveComponentsMtor.mutator().setIntComponent(2).build();
    public static final MtorRecordWithPrimitiveComponents ITEM_3 = MtorRecordWithPrimitiveComponentsMtor.mutator().setIntComponent(3).build();
    public static final MtorRecordWithPrimitiveComponents ITEM_4 = MtorRecordWithPrimitiveComponentsMtor.mutator().setIntComponent(4).build();
    private static final MtorRecordWithNestedSetComponent TEST_RECORD = new MtorRecordWithNestedSetComponent(
            Set.of(
                    ITEM_1,
                    ITEM_2,
                    ITEM_3));

    @Test
    void setComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithNestedSetComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithNestedSetComponent builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.setComponent(), builtRecord.setComponent());
    }

    @Test
    void setComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithNestedSetComponentMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.setComponent(), mutator.getSetComponent());
    }

    @Test
    void setComponentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithNestedSetComponentMtor.mutator();
        Set<MtorRecordWithPrimitiveComponents> value = Set.of();
        // Act
        MtorRecordWithNestedSetComponent builtRecord = mutator
                .setSetComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.setComponent());
    }

    @Test
    void setComponentShouldRemoveElement() {
        // Arrange
        var mutator = MtorRecordWithNestedSetComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithPrimitiveComponents value = MtorRecordWithPrimitiveComponentsMtor.mutator().build();
        // Act
        MtorRecordWithNestedSetComponent builtRecord = mutator
                .mutateSetComponent(set -> set
                        .remove(ITEM_2))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains(ITEM_2));
    }

    @Test
    void setComponentShouldMutateElement() {
        // Arrange
        var mutator = MtorRecordWithNestedSetComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithPrimitiveComponents value = MtorRecordWithPrimitiveComponentsMtor.mutator().build();
        // Act
        MtorRecordWithNestedSetComponent builtRecord = mutator
                .mutateSetComponent(list -> list
                        .mutate(ITEM_2, record -> record
                                .setIntComponent(4)))
                .build();
        // Assert
        assertFalse(builtRecord.setComponent().contains(ITEM_2));
        assertTrue(builtRecord.setComponent().contains(ITEM_4));
    }

}