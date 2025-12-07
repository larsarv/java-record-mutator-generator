package io.github.larsarv.jrmg.api.generation.mtor;

import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.Set;

@GenerateMtor
public record MtorRecordWithNestedSetComponent(
        Set<MtorRecordWithPrimitiveComponents> setComponent
) {
}
