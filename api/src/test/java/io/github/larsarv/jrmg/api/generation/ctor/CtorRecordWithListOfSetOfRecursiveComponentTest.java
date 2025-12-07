package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CtorRecordWithListOfSetOfRecursiveComponentTest {
    @Test
    void shouldCreateRecordWithListOfSetOfRecursiveComponent() {
        // Act
        CtorRecordWithListOfSetOfRecursiveComponent builtRecord = CtorRecordWithListOfSetOfRecursiveComponentCtor.constructor()
                .setList(List.of(Set.of(
                        CtorRecordWithListOfSetOfRecursiveComponentCtor.constructor()
                                .setList(List.of())
                                .setValue("nested")
                                .build()
                )))
                .setValue("test")
                .build();
        // Assert
        assertEquals("test", builtRecord.value());
        assertEquals(1, builtRecord.list().size());
        assertEquals(1, builtRecord.list().get(0).size());
        assertEquals("nested", builtRecord.list().get(0).iterator().next().value());
    }

    @Test
    void shouldCreateRecordWithEmptyListOfSetOfRecursiveComponent() {
        // Act
        CtorRecordWithListOfSetOfRecursiveComponent builtRecord = CtorRecordWithListOfSetOfRecursiveComponentCtor.constructor()
                .setList(List.of())
                .setValue("test")
                .build();
        // Assert
        assertEquals("test", builtRecord.value());
        assertEquals(0, builtRecord.list().size());
    }

    @Test
    void shouldCreateRecordWithNullListOfSetOfRecursiveComponent() {
        // Act
        CtorRecordWithListOfSetOfRecursiveComponent builtRecord = CtorRecordWithListOfSetOfRecursiveComponentCtor.constructor()
                .setList(null)
                .setValue("test")
                .build();
        // Assert
        assertEquals("test", builtRecord.value());
        assertEquals(null, builtRecord.list());
    }

    @Test
    void shouldCreateRecordWithConstructedListOfSetOfRecursiveComponent() {
        // Act
        CtorRecordWithListOfSetOfRecursiveComponent builtRecord = CtorRecordWithListOfSetOfRecursiveComponentCtor.constructor()
                .constructList(listBuilder -> listBuilder
                        .add(setBuilder -> setBuilder
                                .add(recursiveCtor -> recursiveCtor
                                        .setList(List.of())
                                        .setValue("nested1"))
                                .add(recursiveCtor -> recursiveCtor
                                        .setList(List.of())
                                        .setValue("nested2"))))
                .setValue("test")
                .build();
        // Assert
        assertEquals("test", builtRecord.value());
        assertEquals(1, builtRecord.list().size());
        assertEquals(2, builtRecord.list().get(0).size());

        // Check that both nested records have empty lists and the correct values
        var nestedValues = builtRecord.list().get(0).stream()
                .map(CtorRecordWithListOfSetOfRecursiveComponent::value)
                .sorted()
                .toArray(String[]::new);
        assertEquals("nested1", nestedValues[0]);
        assertEquals("nested2", nestedValues[1]);

        // Check that the nested records have empty lists
        var nestedLists = builtRecord.list().get(0).stream()
                .map(CtorRecordWithListOfSetOfRecursiveComponent::list)
                .toArray(List[]::new);
        assertEquals(0, nestedLists[0].size());
        assertEquals(0, nestedLists[1].size());
    }
}