package io.github.larsarv.jrmg.api.generation.mtor;

import io.github.larsarv.jrmg.api.GenerateMtor;

@GenerateMtor
public record MtorRecordWithPrimitiveComponents(
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
