package io.github.larsarv.jrmg.api.generation;


import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record AnnotatedRecordComponentRecord(
        PrimitiveComponentRecord component
) {
}
