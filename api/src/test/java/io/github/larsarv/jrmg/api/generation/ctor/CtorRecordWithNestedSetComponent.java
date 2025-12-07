package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

import java.util.Set;

@GenerateCtor
public record CtorRecordWithNestedSetComponent(
        Set<CtorRecordWithPrimitiveComponents> setComponent
) {
}