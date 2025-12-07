package io.github.larsarv.jrmg.api.generation.mtor;

import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.List;

@GenerateMtor
public record MtorRecordWithNestedListComponent(
        List<MtorRecordWithPrimitiveComponents> listComponent
) {
}
