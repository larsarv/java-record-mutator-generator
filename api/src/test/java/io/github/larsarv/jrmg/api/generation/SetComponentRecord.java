package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.List;
import java.util.Set;

@GenerateMutator
public record SetComponentRecord(
        Set<String> setComponent
) {
}
