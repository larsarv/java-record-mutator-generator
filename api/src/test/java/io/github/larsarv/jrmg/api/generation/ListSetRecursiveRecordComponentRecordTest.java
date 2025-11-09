package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.NestedSetMutator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ListSetRecursiveRecordComponentRecordTest {

    @Test
    void componentShouldMutateElementInSecondSet() {
        // Arrange
        var mutator = ListSetRecursiveRecordComponentRecordMutator.mutator();
        // Act
        var builtRecord = mutator
                .mutateList(l1 -> l1
                        .add(s1 -> s1
                                .add(r1 -> r1
                                        .setValue("test1")
                                        .mutateList(l2 -> l2
                                                .add(s2 -> s2
                                                        .add(r2 -> r2
                                                                .setValue("test2")))))))
                .build();
        // Assert
        assertEquals("test1", getRecord(builtRecord).value());
        assertEquals("test2", getRecord(getRecord(builtRecord)).value());
    }

    private static ListSetRecursiveRecordComponentRecord getRecord(ListSetRecursiveRecordComponentRecord builtRecord) {
        return builtRecord.list().get(0).iterator().next();
    }
}