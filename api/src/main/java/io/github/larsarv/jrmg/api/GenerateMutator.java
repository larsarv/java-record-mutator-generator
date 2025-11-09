package io.github.larsarv.jrmg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that a record type should generate a mutator class
 * capable of modifying its record components.
 * <p>
 * When applied to a record class, this annotation triggers the generation of
 * a mutator implementation that conforms to the {@link Mutator}
 * interface, enabling fluent, side-effect-free modification of record instances.
 * <p>
 * The generated mutator will support operations such as getting, setting and mutation of the record components.
 * <p>
 * This annotation is intended for use with record types and is processed at
 * compile time to generate appropriate mutator logic.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateMutator {
}
