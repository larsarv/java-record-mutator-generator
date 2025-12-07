package io.github.larsarv.jrmg.api.generation.ctor;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CtorRecordWithNestedMapComponentTest {
    @Test
    void shouldCreateRecordWithNestedMapComponent() {
        // Arrange
        Map<CtorRecordWithStringComponent, CtorRecordWithStringComponent> testMap = new HashMap<>();
        testMap.put(
                CtorRecordWithStringComponentCtor.constructor().setValue("key1").build(),
                CtorRecordWithStringComponentCtor.constructor().setValue("value1").build()
        );
        testMap.put(
                CtorRecordWithStringComponentCtor.constructor().setValue("key2").build(),
                CtorRecordWithStringComponentCtor.constructor().setValue("value2").build()
        );
        // Act
        CtorRecordWithNestedMapComponent builtRecord = CtorRecordWithNestedMapComponentCtor.constructor()
                .setMapComponent(testMap)
                .build();
        // Assert
        assertSame(testMap, builtRecord.mapComponent());
    }

    @Test
    void shouldCreateRecordWithEmptyNestedMapComponent() {
        // Arrange
        Map<CtorRecordWithStringComponent, CtorRecordWithStringComponent> emptyMap = new HashMap<>();
        // Act
        CtorRecordWithNestedMapComponent builtRecord = CtorRecordWithNestedMapComponentCtor.constructor()
                .setMapComponent(emptyMap)
                .build();
        // Assert
        assertSame(emptyMap, builtRecord.mapComponent());
        assertEquals(0, builtRecord.mapComponent().size());
    }

    @Test
    void shouldCreateRecordWithNullNestedMapComponent() {
        // Act
        CtorRecordWithNestedMapComponent builtRecord = CtorRecordWithNestedMapComponentCtor.constructor()
                .setMapComponent(null)
                .build();
        // Assert
        assertEquals(null, builtRecord.mapComponent());
    }

    @Test
    void shouldCreateRecordWithConstructedNestedMapComponent() {
        // Act
        CtorRecordWithNestedMapComponent builtRecord = CtorRecordWithNestedMapComponentCtor.constructor()
                .constructMapComponent(mapBuilder -> mapBuilder
                        .put(
                                new CtorRecordWithStringComponent("key1"),
                                valueCtor -> valueCtor.setValue("value1")
                        )
                        .put(
                                new CtorRecordWithStringComponent("key2"),
                                valueCtor -> valueCtor.setValue("value2")
                        ))
                .build();
        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertEquals("value1", builtRecord.mapComponent().get(
                CtorRecordWithStringComponentCtor.constructor().setValue("key1").build()).value());
        assertEquals("value2", builtRecord.mapComponent().get(
                CtorRecordWithStringComponentCtor.constructor().setValue("key2").build()).value());
    }
}