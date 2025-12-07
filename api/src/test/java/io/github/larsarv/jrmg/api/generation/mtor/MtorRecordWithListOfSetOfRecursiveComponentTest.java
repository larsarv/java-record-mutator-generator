package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MtorRecordWithListOfSetOfRecursiveComponentTest {

    private static MtorRecordWithListOfSetOfRecursiveComponent getRecord(MtorRecordWithListOfSetOfRecursiveComponent builtRecord) {
        return builtRecord.list().get(0).iterator().next();
    }

    @Test
    void componentShouldMutateElementInSecondSet() {
        // Arrange
        var mutator = MtorRecordWithListOfSetOfRecursiveComponentMtor.mutator();
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
}