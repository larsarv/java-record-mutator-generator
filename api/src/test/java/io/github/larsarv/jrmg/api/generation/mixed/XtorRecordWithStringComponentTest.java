package io.github.larsarv.jrmg.api.generation.mixed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XtorRecordWithStringComponentTest {
    @Test
    void shouldGenerateCtorAndMtor() {
        XtorRecordWithStringComponent record = XtorRecordWithStringComponentCtor.constructor()
                .setValue("test")
                .build();

        assertNotNull(record);
        assertEquals("test", record.value());

        XtorRecordWithStringComponent mutated = XtorRecordWithStringComponentMtor.mutator(record)
                .setValue("mutated")
                .build();

        assertNotNull(mutated);
        assertEquals("mutated", mutated.value());
    }
}