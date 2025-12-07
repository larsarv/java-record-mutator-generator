package io.github.larsarv.jrmg.api.generation.mtor;

import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.Map;

@GenerateMtor
public record MtorRecordWithNestedMapComponent(
        Map<MtorRecordWithStringComponent, MtorRecordWithStringComponent> mapComponent
) {
}
