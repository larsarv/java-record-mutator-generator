package io.github.larsarv.jrmg.api.generation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapComponentRecordTest {
    private static final Map<StringRecord, StringRecord> TEST_MAP = new HashMap<>();
    
    static {
        TEST_MAP.put(new StringRecord("key1"), new StringRecord("value1"));
        TEST_MAP.put(new StringRecord("key2"), new StringRecord("value2"));
    }

    private static final MapComponentRecord TEST_RECORD = new MapComponentRecord(
            TEST_MAP
    );

    @Test
    void mapComponentShouldMutateKeys() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        mutator.setMapComponent(originalMap);

        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.mutateKey(new StringRecord("key1"), keyMutator -> 
                        keyMutator.setValue("modifiedKey1")))
                .build();
        
        // Assert
        assertEquals(1, builtRecord.mapComponent().size());
        assertTrue(builtRecord.mapComponent().containsKey(new StringRecord("modifiedKey1")));
        assertFalse(builtRecord.mapComponent().containsKey(new StringRecord("key1")));
        assertEquals(new StringRecord("value1"), builtRecord.mapComponent().get(new StringRecord("modifiedKey1")));
    }

    @Test
    void mapComponentShouldMutateValues() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        mutator.setMapComponent(originalMap);
        
        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.mutateValue(new StringRecord("key1"), valueMutator -> 
                        valueMutator.setValue("modifiedValue1")))
                .build();
        
        // Assert
        assertEquals(1, builtRecord.mapComponent().size());
        assertEquals(new StringRecord("modifiedValue1"), builtRecord.mapComponent().get(new StringRecord("key1")));
    }

    @Test
    void mapComponentShouldMutateAllKeys() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        originalMap.put(new StringRecord("key2"), new StringRecord("value2"));
        mutator.setMapComponent(originalMap);
        
        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.mutateAllKeys(keyMutator -> 
                        keyMutator.setValue(keyMutator.build().value() + "_modified")))
                .build();
        
        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertTrue(builtRecord.mapComponent().containsKey(new StringRecord("key1_modified")));
        assertTrue(builtRecord.mapComponent().containsKey(new StringRecord("key2_modified")));
        assertEquals(new StringRecord("value1"), builtRecord.mapComponent().get(new StringRecord("key1_modified")));
        assertEquals(new StringRecord("value2"), builtRecord.mapComponent().get(new StringRecord("key2_modified")));
    }

    @Test
    void mapComponentShouldMutateAllValues() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        originalMap.put(new StringRecord("key2"), new StringRecord("value2"));
        mutator.setMapComponent(originalMap);
        
        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.mutateAllValues((key, valueMutator) -> 
                        valueMutator.setValue(valueMutator.build().value() + "_modified")))
                .build();
        
        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertEquals(new StringRecord("value1_modified"), builtRecord.mapComponent().get(new StringRecord("key1")));
        assertEquals(new StringRecord("value2_modified"), builtRecord.mapComponent().get(new StringRecord("key2")));
    }

    @Test
    void mapComponentShouldAddNewEntry() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        mutator.setMapComponent(originalMap);
        
        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.put(new StringRecord("key2"), new StringRecord("value2")))
                .build();
        
        // Assert
        assertEquals(2, builtRecord.mapComponent().size());
        assertEquals(new StringRecord("value1"), builtRecord.mapComponent().get(new StringRecord("key1")));
        assertEquals(new StringRecord("value2"), builtRecord.mapComponent().get(new StringRecord("key2")));
    }

    @Test
    void mapComponentShouldRemoveEntry() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator();
        Map<StringRecord, StringRecord> originalMap = new HashMap<>();
        originalMap.put(new StringRecord("key1"), new StringRecord("value1"));
        originalMap.put(new StringRecord("key2"), new StringRecord("value2"));
        mutator.setMapComponent(originalMap);
        
        // Act
        MapComponentRecord builtRecord = mutator
                .mutateMapComponent(mapMutator -> 
                    mapMutator.remove(new StringRecord("key1")))
                .build();
        
        // Assert
        assertEquals(1, builtRecord.mapComponent().size());
        assertFalse(builtRecord.mapComponent().containsKey(new StringRecord("key1")));
        assertTrue(builtRecord.mapComponent().containsKey(new StringRecord("key2")));
        assertEquals(new StringRecord("value2"), builtRecord.mapComponent().get(new StringRecord("key2")));
    }

    @Test
    void setMapComponentShouldCreateNewMap() {
        // Arrange
        var mutator = MapComponentRecordMutator.mutator(TEST_RECORD);

        // Act
        MapComponentRecord builtRecord = mutator
                .setMapComponent(map -> map)
                .build();

        // Assert
        assertNotSame(TEST_MAP, builtRecord.mapComponent());
    }

}