package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListComponentRecordTest {
    private static final ListComponentRecord TEST_RECORD = new ListComponentRecord(
            Arrays.asList(new Object(), new Object(), new Object())
    );

    @Test
    void listComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = ListComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        ListComponentRecord builtRecord = mutator.build();
        // Assert
        assertSame(TEST_RECORD.listComponent(), builtRecord.listComponent());
    }

    @Test
    void listComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = ListComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertSame(TEST_RECORD.listComponent(), mutator.getListComponent());
    }

    @Test
    void listComponentShouldGetNewValue() {
        // Arrange
        var mutator = ListComponentRecordMutator.mutator(TEST_RECORD);
        List<Object> value = new ArrayList<>();
        // Act
        ListComponentRecord builtRecord = mutator
                .setListComponent(value)
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent());
    }

    @Test
    void listComponentShouldGetNewValueThroughFunction() {
        // Arrange
        var mutator = ListComponentRecordMutator.mutator();
        List<Object> value = new ArrayList<>();
        // Act
        ListComponentRecord builtRecord = mutator
                .setListComponent(list -> list)
                .build();
        // Assert
        assertEquals(0, builtRecord.listComponent().size());
    }

    @Test
    void listComponentShouldSetSecondElement() {
        // Arrange
        var mutator = ListComponentRecordMutator.mutator(TEST_RECORD);
        Object value = new Object();
        // Act
        ListComponentRecord builtRecord = mutator
                .mutateListComponent(list -> list
                                .set(1, value))
                .build();
        // Assert
        assertSame(value, builtRecord.listComponent().get(1));
    }

}