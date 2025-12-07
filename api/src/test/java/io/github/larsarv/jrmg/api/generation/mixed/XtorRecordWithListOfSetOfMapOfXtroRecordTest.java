package io.github.larsarv.jrmg.api.generation.mixed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XtorRecordWithListOfSetOfMapOfXtroRecordTest {
    @Test
    public void testConstructor() {
        // Act
        XtorRecordWithListOfSetOfMapOfXtroRecord builtRecord = XtorRecordWithListOfSetOfMapOfXtroRecordMtor.mutator()
                .constructSetComponent(set -> set
                        .add(list -> list
                                .add(map -> map
                                        .put("key", record -> record
                                                .setValue("value")))))
                .build();

        // Assert
        assertEquals("value", builtRecord.setComponent().get(0).iterator().next().get("key").value());
    }
}
