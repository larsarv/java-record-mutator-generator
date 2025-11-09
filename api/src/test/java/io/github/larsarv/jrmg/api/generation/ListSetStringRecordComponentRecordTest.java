package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ListSetStringRecordComponentRecordTest {
    private static final ListSetStringRecordComponentRecord TEST_RECORD = new ListSetStringRecordComponentRecord(
            Arrays.asList(
                    Set.of(new StringRecord("apple")),
                    Set.of(new StringRecord("banana"))
            )
    );

    @Test
    void componentShouldRetainOriginalValue() {
        // Arrange
        var mutator = ListSetStringRecordComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        ListSetStringRecordComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.component(), builtRecord.component());
    }

    @Test
    void componentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = ListSetStringRecordComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.component(), mutator.getComponent());
    }

    @Test
    void componentShouldGetNewValue() {
        // Arrange
        var mutator = ListSetStringRecordComponentRecordMutator.mutator();
        List<Set<StringRecord>> value = List.of(
                Set.of(new StringRecord("cherry"))
        );
        // Act
        ListSetStringRecordComponentRecord builtRecord = mutator
                .setComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.component());
    }

    @Test
    void componentShouldAddElementToSecondSet() {
        // Arrange
        var mutator = ListSetStringRecordComponentRecordMutator.mutator(TEST_RECORD);
        StringRecord newValue = new StringRecord("cherry");
        // Act
        ListSetStringRecordComponentRecord builtRecord = mutator
                .mutateComponent(list1 -> list1
                        .mutate(1, set1 -> set1
                                .add(newValue)))
                .build();
        // Assert
        assertEquals(2, builtRecord.component().get(1).size());
        assert(builtRecord.component().get(1).contains(newValue));
    }

    @Test
    void componentShouldRemoveElementFromSecondSet() {
        // Arrange
        var mutator = ListSetStringRecordComponentRecordMutator.mutator(TEST_RECORD);
        Set<StringRecord> originalSet = TEST_RECORD.component().get(1);
        StringRecord toRemove = originalSet.iterator().next();
        // Act
        ListSetStringRecordComponentRecord builtRecord = mutator
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
        var mutator = ListSetStringRecordComponentRecordMutator.mutator(TEST_RECORD);
        StringRecord elementToMutate = TEST_RECORD.component().get(1).iterator().next();
        // Act
        ListSetStringRecordComponentRecord builtRecord = mutator
                .mutateComponent(list1 -> list1
                        .mutate(1, set1 -> set1
                                .mutate(elementToMutate, stringRecordMutator -> stringRecordMutator
                                        .setValue("mutated_value"))))
                .build();
        // Assert
        assertEquals(1, builtRecord.component().get(1).size());
        StringRecord mutatedElement = builtRecord.component().get(1).iterator().next();
        assertEquals("mutated_value", mutatedElement.value());
    }
}