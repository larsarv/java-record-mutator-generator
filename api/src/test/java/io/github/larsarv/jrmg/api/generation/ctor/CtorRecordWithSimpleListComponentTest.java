package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithSimpleListComponentTest {
    @Test
    void shouldCreateRecordWithListComponent() {
        // Arrange
        List<Object> testList = Arrays.asList(new Object(), new Object(), new Object());
        // Act
        CtorRecordWithSimpleListComponent builtRecord = CtorRecordWithSimpleListComponentCtor.constructor()
                .setListComponent(testList)
                .build();
        // Assert
        assertSame(testList, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithEmptyListComponent() {
        // Arrange
        List<Object> emptyList = new ArrayList<>();
        // Act
        CtorRecordWithSimpleListComponent builtRecord = CtorRecordWithSimpleListComponentCtor.constructor()
                .setListComponent(emptyList)
                .build();
        // Assert
        assertSame(emptyList, builtRecord.listComponent());
        assertEquals(0, builtRecord.listComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullListComponent() {
        // Act
        CtorRecordWithSimpleListComponent builtRecord = CtorRecordWithSimpleListComponentCtor.constructor()
                .setListComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedSimpleListComponent() {
        // Act
        CtorRecordWithSimpleListComponent builtRecord = CtorRecordWithSimpleListComponentCtor.constructor()
                .constructListComponent(listBuilder -> listBuilder
                        .add("string1")
                        .add(123)
                        .add(new Object()))
                .build();
        // Assert
        assertEquals(3, builtRecord.listComponent().size());
        assertEquals("string1", builtRecord.listComponent().get(0));
        assertEquals(123, builtRecord.listComponent().get(1));
        assertEquals(Object.class, builtRecord.listComponent().get(2).getClass());
    }
}