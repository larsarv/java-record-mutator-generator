package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithNestedListComponentTest {
    @Test
    void shouldCreateRecordWithNestedListComponent() {
        // Arrange
        List<CtorRecordWithPrimitiveComponents> testList = Arrays.asList(
                CtorRecordWithPrimitiveComponentsCtor.constructor()
                        .setBooleanComponent(true)
                        .setByteComponent((byte) 1)
                        .setCharComponent('a')
                        .setShortComponent((short) 1)
                        .setIntComponent(1)
                        .setLongComponent(1L)
                        .setFloatComponent(1.0F)
                        .setDoubleComponent(1.0)
                        .build(),
                CtorRecordWithPrimitiveComponentsCtor.constructor()
                        .setBooleanComponent(false)
                        .setByteComponent((byte) 2)
                        .setCharComponent('b')
                        .setShortComponent((short) 2)
                        .setIntComponent(2)
                        .setLongComponent(2L)
                        .setFloatComponent(2.0F)
                        .setDoubleComponent(2.0)
                        .build()
        );
        // Act
        CtorRecordWithNestedListComponent builtRecord = CtorRecordWithNestedListComponentCtor.constructor()
                .setListComponent(testList)
                .build();
        // Assert
        assertSame(testList, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithEmptyNestedListComponent() {
        // Arrange
        List<CtorRecordWithPrimitiveComponents> emptyList = new ArrayList<>();
        // Act
        CtorRecordWithNestedListComponent builtRecord = CtorRecordWithNestedListComponentCtor.constructor()
                .setListComponent(emptyList)
                .build();
        // Assert
        assertSame(emptyList, builtRecord.listComponent());
        assertEquals(0, builtRecord.listComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullNestedListComponent() {
        // Act
        CtorRecordWithNestedListComponent builtRecord = CtorRecordWithNestedListComponentCtor.constructor()
                .setListComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.listComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedNestedListComponent() {
        // Act
        CtorRecordWithNestedListComponent builtRecord = CtorRecordWithNestedListComponentCtor.constructor()
                .constructListComponent(listBuilder -> listBuilder
                        .add(ctor -> ctor
                                .setBooleanComponent(true)
                                .setByteComponent((byte) 1)
                                .setCharComponent('a')
                                .setShortComponent((short) 1)
                                .setIntComponent(1)
                                .setLongComponent(1L)
                                .setFloatComponent(1.0F)
                                .setDoubleComponent(1.0))
                        .add(ctor -> ctor
                                .setBooleanComponent(false)
                                .setByteComponent((byte) 2)
                                .setCharComponent('b')
                                .setShortComponent((short) 2)
                                .setIntComponent(2)
                                .setLongComponent(2L)
                                .setFloatComponent(2.0F)
                                .setDoubleComponent(2.0)))
                .build();
        // Assert
        assertEquals(2, builtRecord.listComponent().size());
        assertEquals(true, builtRecord.listComponent().get(0).booleanComponent());
        assertEquals((byte) 1, builtRecord.listComponent().get(0).byteComponent());
        assertEquals('a', builtRecord.listComponent().get(0).charComponent());
        assertEquals((short) 1, builtRecord.listComponent().get(0).shortComponent());
        assertEquals(1, builtRecord.listComponent().get(0).intComponent());
        assertEquals(1L, builtRecord.listComponent().get(0).longComponent());
        assertEquals(1.0F, builtRecord.listComponent().get(0).floatComponent(), 0.0001);
        assertEquals(1.0, builtRecord.listComponent().get(0).doubleComponent(), 0.001);

        assertEquals(false, builtRecord.listComponent().get(1).booleanComponent());
        assertEquals((byte) 2, builtRecord.listComponent().get(1).byteComponent());
        assertEquals('b', builtRecord.listComponent().get(1).charComponent());
        assertEquals((short) 2, builtRecord.listComponent().get(1).shortComponent());
        assertEquals(2, builtRecord.listComponent().get(1).intComponent());
        assertEquals(2L, builtRecord.listComponent().get(1).longComponent());
        assertEquals(2.0F, builtRecord.listComponent().get(1).floatComponent(), 0.0001);
        assertEquals(2.0, builtRecord.listComponent().get(1).doubleComponent(), 0.001);
    }
}