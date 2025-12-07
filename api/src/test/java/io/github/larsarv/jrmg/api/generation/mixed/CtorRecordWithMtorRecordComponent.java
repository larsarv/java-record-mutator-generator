package io.github.larsarv.jrmg.api.generation.mixed;

import io.github.larsarv.jrmg.api.GenerateCtor;

@GenerateCtor
public record CtorRecordWithMtorRecordComponent(
        MtorRecordWithStringComponent component
) {
}