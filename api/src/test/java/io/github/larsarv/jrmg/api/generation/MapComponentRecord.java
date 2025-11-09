package io.github.larsarv.jrmg.api.generation;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.List;
import java.util.Map;

@GenerateMutator
public record MapComponentRecord(
        Map<StringRecord,StringRecord> mapComponent
) {
}
