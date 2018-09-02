package io.github.johannesschaefer.simplenetworkmonitor.repos;

import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.entities.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

public class HostRepositoryImpl implements HostRepositoryCustom {

    public static final String SECRET_STRING = "43serER435gegEWR4t34";

    @Autowired
    private HostRepository hostRepo;

    @Autowired
    private SampleRepository sampleRepo;

    @Override
    public Status getStatus(Host host)
    {
        if (host.getSensors() == null) {
            return Status.UNKNOWN;
        }

        Status s = Status.OK;
        for(Sensor sensor : host.getSensors()) {

            if (sensor.getSampleTypes() == null) {
                s = Status.UNKNOWN;
            }
            for (SampleType sT : sensor.getSampleTypes()) {
                Optional<Sample> sTs = sampleRepo.findFirst1ByTypeOrderByTimeDesc(sT);
                if (!sTs.isPresent()) {
                    continue;
                }
                if (sTs.get().getStatus().ordinal() > s.ordinal()) {
                    s = sTs.get().getStatus();
                }
            }
        }
        return s;
    }

    @Override
    public Map<String, String> getSecretProperties(Host host) {
        Map<String, String> ret = Maps.newHashMap();
        for (String k : host.getSecretProperties().keySet()) {
            ret.put(k, SECRET_STRING);
        }
        return ret;
    }

    @Override
    public int getOk(Host host) {
        return getStatusCount(host, Status.OK);
    }

    private int getStatusCount(Host host, Status status) {
        if (host.getSensors() == null){
            return 0;
        }

        int ret = 0;
        for (Sensor s : host.getSensors()) {
            for (SampleType sT : s.getSampleTypes()) {
                Optional<Sample> sTs = sampleRepo.findFirst1ByTypeOrderByTimeDesc(sT);
                if (sTs.isPresent() && sTs.get().getStatus() == status) {
                    ret++;
                }
            }
        }
        return ret;
    }

    @Override
    public int getWarn(Host host) {
        return getStatusCount(host, Status.WARN);
    }

    @Override
    public int getCritical(Host host) {
        return getStatusCount(host, Status.CRITICAL);
    }

    @Override
    public int getUnknown(Host host) {
        return getStatusCount(host, Status.UNKNOWN);
    }
/*
    @Override
    public Host save(Host host) {
        return hostRepo.save(host);
    }*/
}
