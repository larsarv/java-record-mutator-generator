package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CtorRecordWithListOfSetOfCtorRecordComponentTest {
    @Test
    void shouldCreateRecordWithListOfSetOfCtorRecordComponent() {
        // Arrange
        List<Set<CtorRecordWithStringComponent>> testList = Arrays.asList(
                Set.of(
                        CtorRecordWithStringComponentCtor.constructor().setValue("apple").build(),
                        CtorRecordWithStringComponentCtor.constructor().setValue("banana").build()
                ),
                Set.of(
                        CtorRecordWithStringComponentCtor.constructor().setValue("cherry").build()
                )
        );
        // Act
        CtorRecordWithListOfSetOfCtorRecordComponent builtRecord = CtorRecordWithListOfSetOfCtorRecordComponentCtor.constructor()
                .setComponent(testList)
                .build();
        // Assert
        assertSame(testList, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithEmptyListOfSetOfCtorRecordComponent() {
        // Arrange
        List<Set<CtorRecordWithStringComponent>> emptyList = List.of();
        // Act
        CtorRecordWithListOfSetOfCtorRecordComponent builtRecord = CtorRecordWithListOfSetOfCtorRecordComponentCtor.constructor()
                .setComponent(emptyList)
                .build();
        // Assert
        assertSame(emptyList, builtRecord.component());
        assertEquals(0, builtRecord.component().size());
    }

    @Test
    void shouldCreateRecordWithNullListOfSetOfCtorRecordComponent() {
        // Act
        CtorRecordWithListOfSetOfCtorRecordComponent builtRecord = CtorRecordWithListOfSetOfCtorRecordComponentCtor.constructor()
                .setComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.component());
    }

    @Test
    void shouldCreateRecordWithConstructedListOfSetOfCtorRecordComponent() {
        // Act
        CtorRecordWithListOfSetOfCtorRecordComponent builtRecord = CtorRecordWithListOfSetOfCtorRecordComponentCtor.constructor()
                .constructComponent(listBuilder -> listBuilder
                        .add(setBuilder -> setBuilder
                                .add(stringCtor -> stringCtor.setValue("apple"))
                                .add(stringCtor -> stringCtor.setValue("banana")))
                        .add(setBuilder -> setBuilder
                                .add(stringCtor -> stringCtor.setValue("cherry"))))
                .build();
        // Assert
        assertEquals(2, builtRecord.component().size());
        assertEquals(2, builtRecord.component().get(0).size());
        assertTrue(builtRecord.component().get(0).contains(
                CtorRecordWithStringComponentCtor.constructor().setValue("apple").build()));
        assertTrue(builtRecord.component().get(0).contains(
                CtorRecordWithStringComponentCtor.constructor().setValue("banana").build()));
        assertEquals(1, builtRecord.component().get(1).size());
        assertTrue(builtRecord.component().get(1).contains(
                CtorRecordWithStringComponentCtor.constructor().setValue("cherry").build()));
    }
}