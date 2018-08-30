package io.github.johannesschaefer.simplenetworkmonitor.repos;

import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sample;
import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

public class SensorRepositoryImpl implements SensorRepositoryCustom {
    @Autowired
    private SampleRepository sampleRepo;

    @Override
    public Status getStatus(Sensor sensor)
    {
        if (sensor.getSampleTypes() == null) {
            return Status.UNKNOWN;
        }
        Status s = Status.OK;
        for (SampleType sT : sensor.getSampleTypes()) {
            Optional<Sample> sTs = sampleRepo.findFirst1ByTypeOrderByTimeDesc(sT);
            if (!sTs.isPresent()) {
                continue;
            }
            if (sTs.get().getStatus().ordinal() > s.ordinal()) {
                s = sTs.get().getStatus();
            }
        }
        return s;
    }

    @Override
    public Map<String, String> getSecretProperties(Sensor s) {
        Map<String, String> ret = Maps.newHashMap();
        for (String k : s.getSecretProperties().keySet()) {
            ret.put(k, HostRepositoryImpl.SECRET_STRING);
        }
        return ret;
    }
}
