package io.github.larsarv.jrmg.test.project;


import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record AnnotatedRecordComponentRecord(
        PrimitiveComponentRecord component
) {
}
