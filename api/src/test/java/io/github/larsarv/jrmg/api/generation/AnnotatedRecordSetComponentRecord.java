package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.Set;

@GenerateMutator
public record AnnotatedRecordSetComponentRecord(
        Set<PrimitiveComponentRecord> setComponent
) {
}
