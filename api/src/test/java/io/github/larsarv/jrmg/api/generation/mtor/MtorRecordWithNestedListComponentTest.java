package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MtorRecordWithNestedListComponentTest {
    private static final MtorRecordWithNestedListComponent TEST_RECORD = new MtorRecordWithNestedListComponent(
            Arrays.asList(
                    MtorRecordWithPrimitiveComponentsMtor.mutator().build(),
                    MtorRecordWithPrimitiveComponentsMtor.mutator().build(),
                    MtorRecordWithPrimitiveComponentsMtor.mutator().build())
    );

    @Test
    void listComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithNestedListComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithNestedListComponent builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.listComponent(), builtRecord.listComponent());
    }

    @Test
    void listComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithNestedListComponentMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.listComponent(), mutator.getListComponent());
    }

    @Test
    void listComponentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithNestedListComponentMtor.mutator();
        List<MtorRecordWithPrimitiveComponents> value = new ArrayList<>();
        // Act
        MtorRecordWithNestedListComponent builtRecord = mutator
                .setListComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent());
    }

    @Test
    void listComponentShouldSetSecondElement() {
        // Arrange
        var mutator = MtorRecordWithNestedListComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithPrimitiveComponents value = MtorRecordWithPrimitiveComponentsMtor.mutator().build();
        // Act
        MtorRecordWithNestedListComponent builtRecord = mutator
                .mutateListComponent(list -> list
                        .set(1, value))
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent().get(1));
    }

    @Test
    void listComponentShouldMutateSecondElement() {
        // Arrange
        var mutator = MtorRecordWithNestedListComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithPrimitiveComponents value = MtorRecordWithPrimitiveComponentsMtor.mutator().build();
        // Act
        MtorRecordWithNestedListComponent builtRecord = mutator
                .mutateListComponent(list -> list
                        .mutate(1, record -> record
                                .setBooleanComponent(true)))
                .build();
        // Assert
        assertTrue(builtRecord.listComponent().get(1).booleanComponent());
    }
}