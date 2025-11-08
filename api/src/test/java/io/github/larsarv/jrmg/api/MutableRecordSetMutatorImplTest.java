package io.github.larsarv.jrmg.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class MutableRecordSetMutatorImplTest {

    record TestRecord(boolean test) {
        public TestRecord() {
            this(false);
        }
    }
    static class TestRecordMutator implements RecordMutator<TestRecord> {
        boolean test;
        
        public TestRecordMutator(TestRecord testRecord) {
            if (testRecord != null) {
                this.test = testRecord.test;
            }
        }

        @Override
        public TestRecord build() {
            return new TestRecord(test);
        }

        public TestRecordMutator setTest(boolean value) {
            test = value;
            return this;
        }
    }
    record StringRecord(String value) {}
    static class StringRecordMutator implements RecordMutator<StringRecord> {
        String value;

        public StringRecordMutator(StringRecord stringRecord) {
            if (stringRecord != null) {
                this.value = stringRecord.value;
            }
        }

        @Override
        public StringRecord build() {
            return new StringRecord(value);
        }

        public StringRecordMutator setValue(String value) {
            this.value = value;
            return this;
        }
    }

    @Test
    void shouldSetElementInSet() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("banana"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);


        // Act
        boolean result = mutator.contains(originalList.get(1));

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldAddElementToTheSet() {
        // Arrange
        var originalSet = Set.of(new StringRecord("apple"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(originalSet, StringRecordMutator::new);
        StringRecord value = new StringRecord("banana");

        // Act
        mutator.add(value);
        var newSet = mutator.build();

        // Assert
        assertEquals(3, newSet.size());
        assertTrue(newSet.contains(value));
    }

    @Test
    void shouldRemoveElementFromSet() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("banana"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        mutator.remove(originalList.get(1));
        var newSet = mutator.build();

        // Assert
        assertEquals(2, newSet.size());
        assertFalse(newSet.contains(originalList.get(1)));
    }

    @Test
    void shouldFilterSetByProvidedPredicate() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("banana"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        mutator.filter(i -> !i.value().equals("banana"));
        var newSet = mutator.build();

        // Assert
        assertEquals(2, newSet.size());
        assertFalse(newSet.contains(originalList.get(1)));
    }

    @Test
    void shouldUpdateAllElementsUsingIndexedFunction() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("banana"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        mutator.updateAll(item -> new StringRecord("banana"));
        var newSet = mutator.build();

        // Assert
        assertEquals(1, newSet.size());
        assertTrue(newSet.contains(originalList.get(1)));
    }

    @Test
    void shouldReturnImmutableSetCopyOnBuild() {
        // Arrange
        Set<TestRecord> originalSet = Set.of(new TestRecord(true), new TestRecord(false));
        MutableRecordSetMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordSetMutatorImpl<>(originalSet, TestRecordMutator::new);

        // Act
        Set<TestRecord> builtSet = mutator.build();

        // Assert
        assertEquals(originalSet, builtSet);
        assertEquals(2, builtSet.size());
    }

    @Test
    void shouldThrowExceptionOnModificationAfterBuild() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        Set<StringRecord> builtSet = mutator.build();

        // Assert
        assertThrows(IllegalStateException.class, () -> mutator.add(new StringRecord("banana")));
        assertDoesNotThrow(() -> mutator.contains(originalList.get(1)));
    }

    @Test
    void shouldReturnImmutableSetCopyOnBuildCopy() {
        // Arrange
        Set<TestRecord> originalSet = Set.of(new TestRecord(true), new TestRecord(false));
        MutableRecordSetMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordSetMutatorImpl<>(originalSet, TestRecordMutator::new);

        // Act
        Set<TestRecord> builtSet = mutator.buildCopy();

        // Assert
        assertEquals(originalSet, builtSet);
        assertEquals(2, builtSet.size());
    }

    @Test
    void shouldNotThrowExceptionOnModificationAfterBuildCopy() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        Set<StringRecord> builtSet = mutator.buildCopy();

        // Assert
        assertDoesNotThrow(() -> mutator.add(new StringRecord("banana")));
    }

    @Test
    void shouldHandleNullSetInConstructor() {
        // Arrange
        MutableRecordSetMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordSetMutatorImpl<>(null, TestRecordMutator::new);

        // Act
        Set<TestRecord> builtSet = mutator.build();

        // Assert
        assertEquals(0, builtSet.size());
    }

    @Test
    void shouldAllowAddingNullElementToTheSet() {
        // Arrange
        Set<TestRecord> originalSet = Set.of(new TestRecord());
        MutableRecordSetMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordSetMutatorImpl<>(originalSet, TestRecordMutator::new);

        // Act
        mutator.add((TestRecord) null);
        Set<TestRecord> newSet = mutator.build();

        // Assert
        assertEquals(2, newSet.size());
    }

    @Test
    void shouldAddRecordMutatorToTheSet() {
        // Arrange
        var originalList = List.of(new StringRecord("apple"), new StringRecord("cherry"));
        var mutator = new MutableRecordSetMutatorImpl<>(Set.copyOf(originalList), StringRecordMutator::new);

        // Act
        mutator.add(new StringRecordMutator(new StringRecord("banana")));
        Set<StringRecord> newSet = mutator.build();

        // Assert
        assertEquals(3, mutator.build().size());
    }

}