package io.github.larsarv.jrmg.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class ListMutatorImplTest {

    record TestRecord(boolean test) {
        public TestRecord() {
            this(false);
        }
    }
    static class TestRecordMutator implements Mutator<TestRecord> {
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
    static class StringRecordMutator implements Mutator<StringRecord> {
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
    void shouldReturnCorrectElementWhenGettingFromList() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        TestRecord result = mutator.get(1);

        // Assert
        assertSame(originalList.get(1), result);
    }

    @Test
    void shouldSetElementAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);
        TestRecord value = new TestRecord();

        // Act
        mutator.set(1, value);

        // Assert
        assertSame(value, mutator.get(1));
    }

    @Test
    void shouldAddElementToTheEndOfList() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);
        TestRecord value = new TestRecord();

        // Act
        mutator.add(value);
        var newList = mutator.build();

        // Assert
        assertEquals(3, newList.size());
        assertSame(value, newList.get(newList.size() - 1));
    }

    @Test
    void shouldRemoveElementAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.remove(1);

        // Assert
        assertEquals(2, mutator.size());
        assertSame(originalList.get(0), mutator.get(0));
        assertSame(originalList.get(2), mutator.get(1));
    }

    @Test
    void shouldFilterListByProvidedPredicate() {
        // Arrange
        TestRecord value = new TestRecord();
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), value, new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.filter(t -> t == value);
        var newList = mutator.build();

        // Assert
        assertEquals(1, newList.size());
        assertSame(value, newList.get(0));
    }

    @Test
    void shouldUpdateAllElementsUsingIndexedFunction() {
        // Arrange
        TestRecord value0 = new TestRecord();
        TestRecord value1 = new TestRecord();
        TestRecord value2 = new TestRecord();
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), value2);
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.updateAll((index, item) -> {
            if (index == 0) {
                return value0;
            } else if (index == 1) {
                return value1;
            } else {
                return item;
            }
        });

        // Assert
        assertSame(value0, mutator.get(0));
        assertSame(value1, mutator.get(1));
        assertSame(value2, mutator.get(2));
    }

    @Test
    void shouldReturnImmutableListCopyOnBuild() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.build();

        // Assert
        assertEquals(originalList, builtList);
        assertEquals(2, builtList.size());
    }

    @Test
    void shouldThrowExceptionOnModificationAfterBuild() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.build();

        // Assert
        assertThrows(IllegalStateException.class, () -> mutator.move(0, 1));
        assertDoesNotThrow(() -> mutator.get(0));
    }

    @Test
    void shouldReturnImmutableListCopyOnBuildCopy() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.buildCopy();

        // Assert
        assertEquals(originalList, builtList);
        assertEquals(2, builtList.size());
    }

    @Test
    void shouldNotThrowExceptionOnModificationAfterBuildCopy() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.buildCopy();

        // Assert
        assertDoesNotThrow(() -> mutator.move(0, 1));
    }

    @Test
    void shouldHandleNullListInConstructor() {
        // Arrange
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(null, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.build();

        // Assert
        assertEquals(0, builtList.size());
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenSettingOutOfBoundsIndex() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.set(1, new TestRecord()));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenRemovingOutOfBoundsIndex() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.remove(1));
    }

    @Test
    void shouldAllowAddingNullElementToTheList() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.add((TestRecord) null);
        List<TestRecord> newList = mutator.build();

        // Assert
        assertEquals(2, newList.size());
        assertNull(newList.get(newList.size() - 1));
    }

    @Test
    void shouldAddRecordMutatorToTheList() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.add(m -> m.setTest(true));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertEquals(3, mutator.build().size());
        assertTrue(newList.get(newList.size() - 1).test());
    }

    @Test
    void shouldSetRecordMutatorAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.set(1, new TestRecordMutator(new TestRecord(true)));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertTrue(newList.get(1).test());
    }

    @Test
    void shouldMutateElementAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.mutate(1, m -> m.setTest(true));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertTrue(newList.get(1).test());
    }

    @Test
    void shouldSortListWithNaturalOrderComparator() {
        // Arrange
        List<StringRecord> originalList = Arrays.asList(
                new StringRecord("banana"),
                new StringRecord("apple"),
                new StringRecord("cherry"));

        var mutator = new ListMutatorImpl<>(originalList, StringRecordMutator::new);

        // Act
        mutator.sort(Comparator.comparing(r -> r.value));
        List<StringRecord> builtList = mutator.build();

        // Assert
        assertEquals(
                Arrays.asList(
                        new StringRecord("apple"),
                        new StringRecord("banana"),
                        new StringRecord("cherry")),
                builtList);
    }

    @Test
    void shouldMoveElementFromIndex0ToIndex1() {
        // Arrange
        List<StringRecord> originalList = Arrays.asList(
                new StringRecord("banana"),
                new StringRecord("apple"),
                new StringRecord("cherry"));

        var mutator = new ListMutatorImpl<>(originalList, StringRecordMutator::new);

        // Act
        mutator.move(0, 1);
        List<StringRecord> builtList = mutator.build();

        // Assert
        assertEquals(
                Arrays.asList(
                        new StringRecord("apple"),
                        new StringRecord("banana"),
                        new StringRecord("cherry")),
                builtList);
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenMovingFromIndex0ToIndex3() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.move(0, 3));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenMovingFromIndex2ToIndexMinus1() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        ListMutatorImpl<TestRecord, TestRecordMutator> mutator = new ListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.move(3, -1));
    }
}