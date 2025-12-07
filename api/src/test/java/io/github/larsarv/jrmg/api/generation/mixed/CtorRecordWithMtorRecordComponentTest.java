package io.github.larsarv.jrmg.api.generation.mixed;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CtorRecordWithMtorRecordComponentTest {
    @Test
    void shouldCreateRecordWithNullComponent() {
        // Act
        CtorRecordWithMtorRecordComponent builtRecord = CtorRecordWithMtorRecordComponentCtor.constructor()
                .setComponent(null)
                .build();

        // Assert
        assertEquals(null, builtRecord.component());
    }


    @Test
    void shouldNotHaveMutateComponentMethod() {
        Class<?> clazz = CtorRecordWithMtorRecordComponentCtor.ComponentConstructorSetter.class;

        // Check that mutateComponent method does not exist by looking through all declared methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("mutateComponent")) {
                fail("mutateComponent method should not exist in CtorRecordWithMtorRecordComponentCtor, but was found");
            }
        }

        // Also check all public methods
        methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("mutateComponent")) {
                fail("mutateComponent method should not exist in CtorRecordWithMtorRecordComponentCtor, but was found");
            }
        }

        // Verify the method doesn't exist by trying to get it directly
        Exception exception = assertThrows(NoSuchMethodException.class, () -> {
            clazz.getMethod("mutateComponent", java.util.function.Function.class);
        });
    }

    @Test
    void shouldNotHaveConstructComponentMethod() {
        Class<?> clazz = CtorRecordWithMtorRecordComponentCtor.ComponentConstructorSetter.class;

        // Check that constructComponent method does not exist by looking through all declared methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("constructComponent")) {
                fail("constructComponent method should not exist in CtorRecordWithMtorRecordComponentCtor, but was found: " + method.getName());
            }
        }

        // Also check all public methods
        methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("constructComponent")) {
                fail("constructComponent method should not exist in CtorRecordWithMtorRecordComponentCtor, but was found: " + method.getName());
            }
        }

        // Verify the method doesn't exist by trying to get it directly
        Exception exception = assertThrows(NoSuchMethodException.class, () -> {
            clazz.getMethod("constructComponent", java.util.function.Function.class);
        });
    }
}
