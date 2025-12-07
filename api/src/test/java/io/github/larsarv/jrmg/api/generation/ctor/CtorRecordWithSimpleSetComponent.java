package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

import java.util.Set;

@GenerateCtor
public record CtorRecordWithSimpleSetComponent(
        Set<String> setComponent
) {
}