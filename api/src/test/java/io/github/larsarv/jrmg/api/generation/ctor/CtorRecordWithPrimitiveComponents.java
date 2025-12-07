package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

@GenerateCtor
public record CtorRecordWithPrimitiveComponents(
        boolean booleanComponent,
        byte byteComponent,
        char charComponent,
        short shortComponent,
        int intComponent,
        long longComponent,
        float floatComponent,
        double doubleComponent
) {
}