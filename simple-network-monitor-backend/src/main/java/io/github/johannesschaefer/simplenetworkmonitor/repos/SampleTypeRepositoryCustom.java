package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;

public interface SampleTypeRepositoryCustom {
    SampleType findOrCreateSampleType(SampleType type);
}
