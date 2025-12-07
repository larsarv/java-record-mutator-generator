package io.github.larsarv.jrmg.api.generation.mtor;

import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.List;
import java.util.Set;

@GenerateMtor
public record MtorRecordWithListOfSetOfRecursiveComponent(
        List<Set<MtorRecordWithListOfSetOfRecursiveComponent>> list,
        String value
) {
}