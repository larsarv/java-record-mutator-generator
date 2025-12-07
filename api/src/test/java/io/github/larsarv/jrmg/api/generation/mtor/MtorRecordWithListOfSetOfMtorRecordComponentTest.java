package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MtorRecordWithListOfSetOfMtorRecordComponentTest {
    private static final MtorRecordWithListOfSetOfMtorRecordComponent TEST_RECORD = new MtorRecordWithListOfSetOfMtorRecordComponent(
            Arrays.asList(
                    Set.of(new MtorRecordWithStringComponent("apple")),
                    Set.of(new MtorRecordWithStringComponent("banana"))
            )
    );

    @Test
    void componentShouldRetainOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator(TEST_RECORD);
        // Act
        MtorRecordWithListOfSetOfMtorRecordComponent builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.component(), builtRecord.component());
    }

    @Test
    void componentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.component(), mutator.getComponent());
    }

    @Test
    void componentShouldGetNewValue() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator();
        List<Set<MtorRecordWithStringComponent>> value = List.of(
                Set.of(new MtorRecordWithStringComponent("cherry"))
        );
        // Act
        MtorRecordWithListOfSetOfMtorRecordComponent builtRecord = mutator
                .setComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.component());
    }

    @Test
    void componentShouldAddElementToSecondSet() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithStringComponent newValue = new MtorRecordWithStringComponent("cherry");
        // Act
        MtorRecordWithListOfSetOfMtorRecordComponent builtRecord = mutator
                .mutateComponent(list1 -> list1
                        .mutate(1, set1 -> set1
                                .add(newValue)))
                .build();
        // Assert
        assertEquals(2, builtRecord.component().get(1).size());
        assert (builtRecord.component().get(1).contains(newValue));
    }

    @Test
    void componentShouldRemoveElementFromSecondSet() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator(TEST_RECORD);
        Set<MtorRecordWithStringComponent> originalSet = TEST_RECORD.component().get(1);
        MtorRecordWithStringComponent toRemove = originalSet.iterator().next();
        // Act
        MtorRecordWithListOfSetOfMtorRecordComponent builtRecord = mutator
                .mutateComponent(list1 -> list1
                        .mutate(1, set1 -> set1
                                .remove(toRemove)))
                .build();
        // Assert
        assertEquals(0, builtRecord.component().get(1).size());
    }

    @Test
    void componentShouldMutateElementInSecondSet() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfMtorRecordComponentMtor.mutator(TEST_RECORD);
        MtorRecordWithStringComponent elementToMutate = TEST_RECORD.component().get(1).iterator().next();
        // Act
        MtorRecordWithListOfSetOfMtorRecordComponent builtRecord = mutator
                .mutateComponent(list1 -> list1
                        .mutate(1, set1 -> set1
                                .mutate(elementToMutate, stringRecordMutator -> stringRecordMutator
                                        .setValue("mutated_value"))))
                .build();
        // Assert
        assertEquals(1, builtRecord.component().get(1).size());
        MtorRecordWithStringComponent mutatedElement = builtRecord.component().get(1).iterator().next();
        assertEquals("mutated_value", mutatedElement.value());
    }
}