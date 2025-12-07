package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

@GenerateCtor
public record CtorRecordWithObjectComponents(
        String stringComponent,
        Object objectComponent
) {
}