package io.github.larsarv.jrmg.api.generation.mixed;

import io.github.larsarv.jrmg.api.GenerateMtor;

@GenerateMtor
public record MtorRecordWithCtorRecordComponent(
        CtorRecordWithStringComponent component
) {
}