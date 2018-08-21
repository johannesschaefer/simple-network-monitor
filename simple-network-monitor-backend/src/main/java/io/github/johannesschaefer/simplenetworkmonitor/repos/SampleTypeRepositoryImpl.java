package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SampleTypeRepositoryImpl implements SampleTypeRepositoryCustom {
    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Override
    public SampleType findOrCreateSampleType(SampleType type) {
        Optional<SampleType> sampleType = sampleTypeRepository.findByNameAndSensor(type.getName(), type.getSensor());
        return sampleType.orElseGet(() -> sampleTypeRepository.save(type));
    }
}
