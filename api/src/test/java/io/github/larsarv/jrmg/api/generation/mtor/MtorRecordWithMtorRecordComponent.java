package io.github.larsarv.jrmg.api.generation.mtor;


import io.github.larsarv.jrmg.api.GenerateMtor;

@GenerateMtor
public record MtorRecordWithMtorRecordComponent(
        MtorRecordWithPrimitiveComponents component
) {
}
