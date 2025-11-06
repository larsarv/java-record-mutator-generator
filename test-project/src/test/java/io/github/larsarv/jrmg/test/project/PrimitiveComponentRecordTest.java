package io.github.larsarv.jrmg.test.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveComponentRecordTest {
    private static final PrimitiveComponentRecord TEST_RECORD = new PrimitiveComponentRecord(
            true,
            (byte)1,
            'a',
            (short)1,
            1,
            1L,
            1.0F,
            1.0
    );
    @Test
    void booleanComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.booleanComponent(), builtRecord.booleanComponent());
    }

    @Test
    void booleanComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.booleanComponent(), mutator.getBooleanComponent());
    }

    @Test
    void booleanComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setBooleanComponent(true)
                .build();
        // Assert
        assertTrue(builtRecord.booleanComponent());
    }

    @Test
    void byteComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.byteComponent(), builtRecord.byteComponent());
    }

    @Test
    void byteComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.byteComponent(), mutator.getByteComponent());
    }

    @Test
    void byteComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setByteComponent((byte) 10)
                .build();
        // Assert
        assertEquals((byte) 10, builtRecord.byteComponent());
    }

    @Test
    void charComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.charComponent(), builtRecord.charComponent());
    }

    @Test
    void charComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.charComponent(), mutator.getCharComponent());
    }

    @Test
    void charComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setCharComponent('x')
                .build();
        // Assert
        assertEquals('x', builtRecord.charComponent());
    }

    @Test
    void shortComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.shortComponent(), builtRecord.shortComponent());
    }

    @Test
    void shortComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.shortComponent(), mutator.getShortComponent());
    }

    @Test
    void shortComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setShortComponent((short) 100)
                .build();
        // Assert
        assertEquals((short) 100, builtRecord.shortComponent());
    }

    @Test
    void intComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.intComponent(), builtRecord.intComponent());
    }

    @Test
    void intComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.intComponent(), mutator.getIntComponent());
    }

    @Test
    void intComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setIntComponent(100)
                .build();
        // Assert
        assertEquals(100, builtRecord.intComponent());
    }

    @Test
    void longComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.longComponent(), builtRecord.longComponent());
    }

    @Test
    void longComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.longComponent(), mutator.getLongComponent());
    }

    @Test
    void longComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setLongComponent(100L)
                .build();
        // Assert
        assertEquals(100L, builtRecord.longComponent());
    }

    @Test
    void floatComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.floatComponent(), builtRecord.floatComponent(), 0.0001);
    }

    @Test
    void floatComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.floatComponent(), mutator.getFloatComponent(), 0.0001);
    }

    @Test
    void floatComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setFloatComponent(10.5f)
                .build();
        // Assert
        assertEquals(10.5f, builtRecord.floatComponent(), 0.0001);
    }

    @Test
    void doubleComponentShouldRetainOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Act
        PrimitiveComponentRecord builtRecord = mutator.build();
        // Assert
        assertEquals(TEST_RECORD.doubleComponent(), builtRecord.doubleComponent(), 0.0001);
    }

    @Test
    void doubleComponentGetterShouldReturnOriginalValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator(TEST_RECORD);
        // Assert
        assertEquals(TEST_RECORD.doubleComponent(), mutator.getDoubleComponent(), 0.0001);
    }

    @Test
    void doubleComponentShouldGetNewValue() {
        // Arrange
        var mutator = PrimitiveComponentRecordMutator.mutator();
        // Act
        PrimitiveComponentRecord builtRecord = mutator
                .setDoubleComponent(10.5)
                .build();
        // Assert
        assertEquals(10.5, builtRecord.doubleComponent(), 0.0001);
    }
}