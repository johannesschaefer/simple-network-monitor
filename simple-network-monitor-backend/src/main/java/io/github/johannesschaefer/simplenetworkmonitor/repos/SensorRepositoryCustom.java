package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;

import java.util.Map;

public interface SensorRepositoryCustom {
    Status getStatus(Sensor s);

    Map<String, String> getSecretProperties(Sensor s);
}
