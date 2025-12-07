package io.github.larsarv.jrmg.api.generation.mtor;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MtorRecordWithNestedMapComponentTest {
    private static final Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> TEST_MAP = new HashMap<>();
    private static final MtorRecordWithNestedMapComponent TEST_RECORD = new MtorRecordWithNestedMapComponent(
            TEST_MAP
    );

    static {
        TEST_MAP.put(new MtorRecordWithStringComponent("key1"), new MtorRecordWithStringComponent("value1"));
        TEST_MAP.put(new MtorRecordWithStringComponent("key2"), new MtorRecordWithStringComponent("value2"));
    }

    @Test
    void mapComponentShouldMutateValues() {
        // Arrange
        var mutator = MtorRecordWithNestedMapComponentMtor.mutator();
        Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> originalMap = new HashMap<>();
        originalMap.put(new MtorRecordWithStringComponent("key1"), new MtorRecordWithStringComponent("value1"));
        mutator.setMapComponent(originalMap);

        // Act
        MtorRecordWithNestedMapComponent builtRecord = mutator
                .mutateMapComponent(mapMutator ->
                        mapMutator.mutateValue(new MtorRecordWithStringComponent("key1"), valueMutator ->
                                valueMutator.setValue("modifiedValue1")))
                .build();

        // Assert
        assertEquals(1, builtRecord.mapComponent().size());
        assertEquals(new MtorRecordWithStringComponent("modifiedValue1"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key1")));
    }

    @Test
    void mapComponentShouldMutateAllValues() {
        // Arrange
        var mutator = MtorRecordWithNestedMapComponentMtor.mutator();
        Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> originalMap = new HashMap<>();
        originalMap.put(new MtorRecordWithStringComponent("key1"), new MtorRecordWithStringComponent("value1"));
        originalMap.put(new MtorRecordWithStringComponent("key2"), new MtorRecordWithStringComponent("value2"));
        mutator.setMapComponent(originalMap);

        // Act
        MtorRecordWithNestedMapComponent builtRecord = mutator
                .mutateMapComponent(mapMutator ->
                        mapMutator.mutateAllValues((key, valueMutator) ->
                                valueMutator.setValue(valueMutator.build().value() + "_modified")))
                .build();

        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertEquals(new MtorRecordWithStringComponent("value1_modified"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key1")));
        assertEquals(new MtorRecordWithStringComponent("value2_modified"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key2")));
    }

    @Test
    void mapComponentShouldAddNewEntry() {
        // Arrange
        var mutator = MtorRecordWithNestedMapComponentMtor.mutator();
        Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> originalMap = new HashMap<>();
        originalMap.put(new MtorRecordWithStringComponent("key1"), new MtorRecordWithStringComponent("value1"));
        mutator.setMapComponent(originalMap);

        // Act
        MtorRecordWithNestedMapComponent builtRecord = mutator
                .mutateMapComponent(mapMutator ->
                        mapMutator.put(new MtorRecordWithStringComponent("key2"), new MtorRecordWithStringComponent("value2")))
                .build();

        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertEquals(new MtorRecordWithStringComponent("value1"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key1")));
        assertEquals(new MtorRecordWithStringComponent("value2"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key2")));
    }

    @Test
    void mapComponentShouldRemoveEntry() {
        // Arrange
        var mutator = MtorRecordWithNestedMapComponentMtor.mutator();
        Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> originalMap = new HashMap<>();
        originalMap.put(new MtorRecordWithStringComponent("key1"), new MtorRecordWithStringComponent("value1"));
        originalMap.put(new MtorRecordWithStringComponent("key2"), new MtorRecordWithStringComponent("value2"));
        mutator.setMapComponent(originalMap);

        // Act
        MtorRecordWithNestedMapComponent builtRecord = mutator
                .mutateMapComponent(mapMutator ->
                        mapMutator.remove(new MtorRecordWithStringComponent("key1")))
                .build();

        // Assert
        assertEquals(1, builtRecord.mapComponent().size());
        assertFalse(builtRecord.mapComponent().containsKey(new MtorRecordWithStringComponent("key1")));
        assertTrue(builtRecord.mapComponent().containsKey(new MtorRecordWithStringComponent("key2")));
        assertEquals(new MtorRecordWithStringComponent("value2"), builtRecord.mapComponent().get(new MtorRecordWithStringComponent("key2")));
    }

    @Test
    void setMapComponentShouldCreateNewMap() {
        // Arrange
        var mutator = MtorRecordWithNestedMapComponentMtor.mutator(TEST_RECORD);

        // Act
        MtorRecordWithNestedMapComponent builtRecord = mutator
                .setMapComponent(map -> map)
                .build();

        // Assert
        assertNotSame(TEST_MAP, builtRecord.mapComponent());
    }

}