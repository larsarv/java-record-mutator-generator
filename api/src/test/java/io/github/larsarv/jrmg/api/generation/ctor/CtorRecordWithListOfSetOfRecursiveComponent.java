package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

import java.util.List;
import java.util.Set;

@GenerateCtor
public record CtorRecordWithListOfSetOfRecursiveComponent(
        List<Set<CtorRecordWithListOfSetOfRecursiveComponent>> list,
        String value
) {
}