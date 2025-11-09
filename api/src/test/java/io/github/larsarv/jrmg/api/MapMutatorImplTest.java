package io.github.larsarv.jrmg.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class MapMutatorImplTest {

    record KeyRecord(String value) {}
    static class KeyRecordMutator implements Mutator<KeyRecord> {
        String value;

        public KeyRecordMutator(KeyRecord keyRecord) {
            if (keyRecord != null) {
                this.value = keyRecord.value;
            }
        }

        @Override
        public KeyRecord build() {
            return new KeyRecord(value);
        }

        public KeyRecordMutator setValue(String value) {
            this.value = value;
            return this;
        }
    }
    
    record ValueRecord(String value) {}
    static class ValueRecordMutator implements Mutator<ValueRecord> {
        String value;

        public ValueRecordMutator(ValueRecord valueRecord) {
            if (valueRecord != null) {
                this.value = valueRecord.value;
            }
        }

        @Override
        public ValueRecord build() {
            return new ValueRecord(value);
        }

        public ValueRecordMutator setValue(String value) {
            this.value = value;
            return this;
        }
    }

    @Test
    void shouldReturnCorrectElementWhenGettingFromMap() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        ValueRecord result = mutator.get(new KeyRecord("key2"));

        // Assert
        assertSame(originalMap.get(new KeyRecord("key2")), result);
    }

    @Test
    void shouldPutElementInMap() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);
        ValueRecord value = new ValueRecord("value3");

        // Act
        mutator.put(new KeyRecord("key3"), value);
        var newMap = mutator.build();

        // Assert
        assertEquals(3, newMap.size());
        assertSame(value, newMap.get(new KeyRecord("key3")));
    }

    @Test
    void shouldRemoveElementFromMap() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.remove(new KeyRecord("key2"));

        // Assert
        assertEquals(2, mutator.size());
        assertFalse(mutator.containsKey(new KeyRecord("key2")));
        assertTrue(mutator.containsKey(new KeyRecord("key1")));
        assertTrue(mutator.containsKey(new KeyRecord("key3")));
    }

    @Test
    void shouldFilterMapByProvidedPredicate() {
        // Arrange
        ValueRecord value = new ValueRecord("target");
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), value,
            new KeyRecord("key3"), new ValueRecord("value3"),
            new KeyRecord("key4"), new ValueRecord("value4"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.filter((k, v) -> v == value);
        var newMap = mutator.build();

        // Assert
        assertEquals(1, newMap.size());
        assertSame(value, newMap.get(new KeyRecord("key2")));
    }

    @Test
    void shouldUpdateAllElementsUsingFunction() {
        // Arrange
        ValueRecord value0 = new ValueRecord("updated0");
        ValueRecord value1 = new ValueRecord("updated1");
        ValueRecord value2 = new ValueRecord("value2");
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), value2);
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.updateAll((key, item) -> {
            if (key.value().equals("key1")) {
                return value0;
            } else if (key.value().equals("key2")) {
                return value1;
            } else {
                return item;
            }
        });

        // Assert
        assertSame(value0, mutator.get(new KeyRecord("key1")));
        assertSame(value1, mutator.get(new KeyRecord("key2")));
        assertSame(value2, mutator.get(new KeyRecord("key3")));
    }

    @Test
    void shouldReturnImmutableMapCopyOnBuild() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Map<KeyRecord, ValueRecord> builtMap = mutator.build();

        // Assert
        assertEquals(originalMap, builtMap);
        assertEquals(2, builtMap.size());
    }

    @Test
    void shouldThrowExceptionOnModificationAfterBuild() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Map<KeyRecord, ValueRecord> builtMap = mutator.build();

        // Assert
        assertThrows(IllegalStateException.class, () -> mutator.put(new KeyRecord("key3"), new ValueRecord("value3")));
        assertDoesNotThrow(() -> mutator.get(new KeyRecord("key1")));
    }

    @Test
    void shouldReturnImmutableMapCopyOnBuildCopy() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Map<KeyRecord, ValueRecord> builtMap = mutator.buildCopy();

        // Assert
        assertEquals(originalMap, builtMap);
        assertEquals(2, builtMap.size());
    }

    @Test
    void shouldNotThrowExceptionOnModificationAfterBuildCopy() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Map<KeyRecord, ValueRecord> builtMap = mutator.buildCopy();

        // Assert
        assertDoesNotThrow(() -> mutator.put(new KeyRecord("key3"), new ValueRecord("value3")));
    }

    @Test
    void shouldHandleNullMapInConstructor() {
        // Arrange
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(null, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Map<KeyRecord, ValueRecord> builtMap = mutator.build();

        // Assert
        assertEquals(0, builtMap.size());
    }

    @Test
    void shouldAllowAddingNullElementToTheMap() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(new KeyRecord("key1"), new ValueRecord("value1"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.put(new KeyRecord("key2"), (ValueRecord) null);
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals(2, newMap.size());
        assertNull(newMap.get(new KeyRecord("key2")));
    }

    @Test
    void shouldAddRecordMutatorToTheMap() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.put(new KeyRecord("key3"), m -> m.setValue("updatedValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals(3, mutator.build().size());
        assertEquals("updatedValue", newMap.get(new KeyRecord("key3")).value());
    }

    @Test
    void shouldPutWithValueMutator() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
                new KeyRecord("key1"), new ValueRecord("value1"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
                new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.put(new KeyRecord("key2"), m -> m.setValue("newValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertTrue(newMap.containsKey(new KeyRecord("key2")));
        assertEquals("newValue", newMap.get(new KeyRecord("key2")).value());
    }

    @Test
    void shouldMutateElementAtSpecifiedKey() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.mutateValue(new KeyRecord("key2"), m -> m.setValue("updatedValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals("updatedValue", newMap.get(new KeyRecord("key2")).value());
    }

    @Test
    void shouldTestContainsKey() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        boolean result = mutator.containsKey(new KeyRecord("key1"));

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldTestContainsValue() {
        // Arrange
        ValueRecord valueRecord = new ValueRecord("targetValue");
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), valueRecord);
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        boolean result = mutator.containsValue(valueRecord);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldTestSize() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        int size = mutator.size();

        // Assert
        assertEquals(3, size);
    }

    @Test
    void shouldTestIsEmpty() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of();
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        boolean isEmpty = mutator.isEmpty();

        // Assert
        assertTrue(isEmpty);
    }

    @Test
    void shouldTestIsEmptyWhenNotEmpty() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(new KeyRecord("key1"), new ValueRecord("value1"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        boolean isEmpty = mutator.isEmpty();

        // Assert
        assertFalse(isEmpty);
    }

    @Test
    void shouldTestKeySet() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Set<KeyRecord> keySet = mutator.keySet();

        // Assert
        assertEquals(Set.of(new KeyRecord("key1"), new KeyRecord("key2"), new KeyRecord("key3")), keySet);
    }

    @Test
    void shouldTestValues() {
        // Arrange
        ValueRecord value1 = new ValueRecord("value1");
        ValueRecord value2 = new ValueRecord("value2");
        ValueRecord value3 = new ValueRecord("value3");
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), value1,
            new KeyRecord("key2"), value2,
            new KeyRecord("key3"), value3);
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        Collection<ValueRecord> values = mutator.values();

        // Assert
        assertEquals(3, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
        assertTrue(values.contains(value3));
    }

    @Test
    void shouldTestPutAll() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(new KeyRecord("key1"), new ValueRecord("value1"));
        Map<KeyRecord, ValueRecord> additionalMap = Map.of(
            new KeyRecord("key2"), new ValueRecord("value2"),
            new KeyRecord("key3"), new ValueRecord("value3"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.putAll(additionalMap);
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals(3, newMap.size());
        assertTrue(newMap.containsKey(new KeyRecord("key1")));
        assertTrue(newMap.containsKey(new KeyRecord("key2")));
        assertTrue(newMap.containsKey(new KeyRecord("key3")));
    }

    @Test
    void shouldTestClear() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);
 
        // Act
        mutator.clear();
 
        // Assert
        assertEquals(0, mutator.size());
        assertTrue(mutator.isEmpty());
    }
    
    @Test
    void shouldUpdateElementUsingFunction() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.update(new KeyRecord("key2"), v -> new ValueRecord("updatedValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals("updatedValue", newMap.get(new KeyRecord("key2")).value());
    }
    
    @Test
    void shouldPutWithKeyMutator() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.put(m -> m.setValue("newKey"), new ValueRecord("newValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertTrue(newMap.containsKey(new KeyRecord("newKey")));
        assertEquals("newValue", newMap.get(new KeyRecord("newKey")).value());
    }
    
    @Test
    void shouldMutateKey() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("oldKey"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.mutateKey(new KeyRecord("oldKey"), m -> m.setValue("newKey"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertFalse(newMap.containsKey(new KeyRecord("oldKey")));
        assertTrue(newMap.containsKey(new KeyRecord("newKey")));
        assertEquals("value1", newMap.get(new KeyRecord("newKey")).value());
    }
    
    @Test
    void shouldMutateAllKeys() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.mutateAllKeys(m -> m.setValue(m.build().value() + "_modified"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertTrue(newMap.containsKey(new KeyRecord("key1_modified")));
        assertTrue(newMap.containsKey(new KeyRecord("key2_modified")));
        assertEquals("value1", newMap.get(new KeyRecord("key1_modified")).value());
        assertEquals("value2", newMap.get(new KeyRecord("key2_modified")).value());
    }
    
    @Test
    void shouldMutateAllValues() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"),
            new KeyRecord("key2"), new ValueRecord("value2"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.mutateAllValues((k, m) -> m.setValue(k.value() + "_modified"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals("key1_modified", newMap.get(new KeyRecord("key1")).value());
        assertEquals("key2_modified", newMap.get(new KeyRecord("key2")).value());
    }
    
    @Test
    void shouldPutWithKeyAndValueMutator() {
        // Arrange
        Map<KeyRecord, ValueRecord> originalMap = Map.of(
            new KeyRecord("key1"), new ValueRecord("value1"));
        MapMutatorImpl<KeyRecord, ValueRecord, KeyRecordMutator, ValueRecordMutator> mutator =
            new MapMutatorImpl<>(originalMap, KeyRecordMutator::new, ValueRecordMutator::new);

        // Act
        mutator.put(m -> m.setValue("newKey"), m -> m.setValue("newValue"));
        Map<KeyRecord, ValueRecord> newMap = mutator.build();

        // Assert
        assertEquals(2, newMap.size());
        assertTrue(newMap.containsKey(new KeyRecord("newKey")));
        assertEquals("newValue", newMap.get(new KeyRecord("newKey")).value());
    }
    
}