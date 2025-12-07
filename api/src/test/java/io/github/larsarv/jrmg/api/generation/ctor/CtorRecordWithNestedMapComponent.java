package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

import java.util.Map;

@GenerateCtor
public record CtorRecordWithNestedMapComponent(
        Map<CtorRecordWithStringComponent, CtorRecordWithStringComponent> mapComponent
) {
}