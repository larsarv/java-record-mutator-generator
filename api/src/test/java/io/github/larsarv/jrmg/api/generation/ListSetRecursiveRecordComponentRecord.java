package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.List;
import java.util.Set;

@GenerateMutator
public record ListSetRecursiveRecordComponentRecord(
        List<Set<ListSetRecursiveRecordComponentRecord>> list,
        String value
) {
}