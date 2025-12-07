package io.github.larsarv.jrmg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that a record type should generate both a mutator and a constructor class.
 * <p>
 * This is a convenience annotation that is equivalent to annotating the record with both
 * {@link GenerateMtor} and {@link GenerateCtor}.
 * <p>
 * When applied to a record class, this annotation triggers the generation of:
 * <ul>
 *     <li>A mutator implementation that conforms to the {@link Builder} interface.</li>
 *     <li>A constructor implementation that conforms to the {@link Builder} interface.</li>
 * </ul>
 * <p>
 * This annotation is intended for use with record types and is processed at
 * compile time to generate appropriate logic.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateCtorAndMtor {
}