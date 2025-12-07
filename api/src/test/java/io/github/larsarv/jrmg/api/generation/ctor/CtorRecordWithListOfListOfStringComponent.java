package io.github.larsarv.jrmg.api.generation.ctor;

import io.github.larsarv.jrmg.api.GenerateCtor;

import java.util.List;

@GenerateCtor
public record CtorRecordWithListOfListOfStringComponent(
        List<List<String>> listComponent
) {
}