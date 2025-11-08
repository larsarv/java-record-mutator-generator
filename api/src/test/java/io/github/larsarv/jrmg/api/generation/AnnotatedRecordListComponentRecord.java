package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.List;

@GenerateMutator
public record AnnotatedRecordListComponentRecord(
        List<PrimitiveComponentRecord> listComponent
) {
}
