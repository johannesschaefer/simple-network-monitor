package io.github.johannesschaefer.simplenetworkmonitor.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

@Projection(types = Sensor.class)
public interface SensorStatus {
    String getId();

    String getName();

    boolean getActive();

    long getInterval();

    List<SampleType> getSampleTypes();

    Map<String, String> getProperties();

    @Value("#{@sensorRepository.getSecretProperties(target)}")
    Map<String, String> getSecretProperties();

    Command getCommand();

    Host getHost();

    @Value("#{@sensorRepository.getStatus(target)}")
    String getStatus();
}