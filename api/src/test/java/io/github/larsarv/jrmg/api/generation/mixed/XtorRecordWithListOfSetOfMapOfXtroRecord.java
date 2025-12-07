package io.github.larsarv.jrmg.api.generation.mixed;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@GenerateCtor
@GenerateMtor
public record XtorRecordWithListOfSetOfMapOfXtroRecord(
        List<Set<Map<String, XtorRecordWithStringComponent>>> setComponent) {
}
