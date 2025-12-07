package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

@GenerateCtor
public record CtorRecordWithCtorRecordComponent(
        CtorRecordWithPrimitiveComponents component
) {
}