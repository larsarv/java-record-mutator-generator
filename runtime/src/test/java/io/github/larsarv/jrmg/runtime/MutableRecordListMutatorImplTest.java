package io.github.larsarv.jrmg.runtime;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import io.github.larsarv.jrmg.api.RecordMutator;
import org.junit.jupiter.api.Test;

class MutableRecordListMutatorImplTest {

    record TestRecord(
            boolean test
    ) {
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

    @Test
    void shouldReturnCorrectElementWhenGettingFromList() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        TestRecord result = mutator.get(1);

        // Assert
        assertSame(originalList.get(1), result);
    }

    @Test
    void shouldSetElementAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);
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
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);
        TestRecord value = new TestRecord();

        // Act
        mutator.add(value);
        var newList = mutator.build();

        // Assert
        assertEquals(3, newList.size());
        assertSame(value, newList.getLast());
    }

    @Test
    void shouldRemoveElementAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

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
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.filter(t -> t == value);
        var newList = mutator.build();

        // Assert
        assertEquals(1, newList.size());
        assertSame(value, newList.getFirst());
    }

    @Test
    void shouldUpdateAllElementsUsingIndexedFunction() {
        // Arrange
        TestRecord value0 = new TestRecord();
        TestRecord value1 = new TestRecord();
        TestRecord value2 = new TestRecord();
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), value2);
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

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
    void shouldBuildImmutableListCopy() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.build();

        // Assert
        assertEquals(originalList, builtList);
        assertEquals(2, builtList.size());
    }

    @Test
    void shouldHandleNullListInConstructor() {
        // Arrange
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(null, TestRecordMutator::new);

        // Act
        List<TestRecord> builtList = mutator.build();

        // Assert
        assertEquals(0, builtList.size());
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenSettingOutOfBoundsIndex() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.set(1, new TestRecord()));
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenRemovingOutOfBoundsIndex() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> mutator.remove(1));
    }

    @Test
    void shouldAllowAddingNullElementToTheList() {
        // Arrange
        List<TestRecord> originalList = List.of(new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.add((TestRecord) null);
        List<TestRecord> newList = mutator.build();

        // Assert
        assertEquals(2, newList.size());
        assertNull(newList.getLast());
    }

    @Test
    void shouldAddRecordMutatorToTheList() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.add(new TestRecordMutator(new TestRecord(true)));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertEquals(3, mutator.build().size());
        assertTrue(newList.getLast().test());
    }

    @Test
    void shouldSetRecordMutatorAtSpecifiedIndex() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

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
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.mutate(1, m -> m.setTest(true));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertTrue(newList.get(1).test());
    }

    @Test
    void shouldMutateAllElementsUsingIndexedFunction() {
        // Arrange
        List<TestRecord> originalList = Arrays.asList(new TestRecord(), new TestRecord(), new TestRecord());
        MutableRecordListMutatorImpl<TestRecord, TestRecordMutator> mutator = new MutableRecordListMutatorImpl<>(originalList, TestRecordMutator::new);

        // Act
        mutator.mutateAll((index, m) -> m.setTest(true));
        List<TestRecord> newList = mutator.build();

        // Assert
        assertTrue(newList.get(0).test());
        assertTrue(newList.get(1).test());
        assertTrue(newList.get(2).test());
    }

}