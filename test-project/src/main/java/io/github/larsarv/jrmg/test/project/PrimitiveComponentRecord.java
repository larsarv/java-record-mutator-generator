package io.github.larsarv.jrmg.test.project;

import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record PrimitiveComponentRecord(
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
