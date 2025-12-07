package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithListOfListOfStringComponentTest {
    @Test
    void shouldCreateRecordWithListOfListOfStringComponent() {
        // Arrange
        List<List<String>> testList = Arrays.asList(
                List.of("apple", "banana"),
                List.of("cherry", "date")
        );
        // Act
        CtorRecordWithListOfListOfStringComponent builtRecord = CtorRecordWithListOfListOfStringComponentCtor.constructor()
                .setListComponent(testList)
                .build();
        // Assert
        assertSame(testList, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithEmptyListOfListOfStringComponent() {
        // Arrange
        List<List<String>> emptyList = List.of();
        // Act
        CtorRecordWithListOfListOfStringComponent builtRecord = CtorRecordWithListOfListOfStringComponentCtor.constructor()
                .setListComponent(emptyList)
                .build();
        // Assert
        assertSame(emptyList, builtRecord.listComponent());
        assertEquals(0, builtRecord.listComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullListOfListOfStringComponent() {
        // Act
        CtorRecordWithListOfListOfStringComponent builtRecord = CtorRecordWithListOfListOfStringComponentCtor.constructor()
                .setListComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedListOfListOfStringComponent() {
        // Act
        CtorRecordWithListOfListOfStringComponent builtRecord = CtorRecordWithListOfListOfStringComponentCtor.constructor()
                .constructListComponent(listBuilder -> listBuilder
                        .add(innerListBuilder -> innerListBuilder
                                .add("apple")
                                .add("banana"))
                        .add(innerListBuilder -> innerListBuilder
                                .add("cherry")
                                .add("date")))
                .build();
        // Assert
        assertEquals(2, builtRecord.listComponent().size());
        assertEquals(2, builtRecord.listComponent().get(0).size());
        assertEquals("apple", builtRecord.listComponent().get(0).get(0));
        assertEquals("banana", builtRecord.listComponent().get(0).get(1));
        assertEquals(2, builtRecord.listComponent().get(1).size());
        assertEquals("cherry", builtRecord.listComponent().get(1).get(0));
        assertEquals("date", builtRecord.listComponent().get(1).get(1));
    }
}