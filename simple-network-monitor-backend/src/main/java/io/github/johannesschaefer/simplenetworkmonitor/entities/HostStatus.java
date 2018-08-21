package io.github.johannesschaefer.simplenetworkmonitor.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

@Projection(types = Host.class)
public interface HostStatus {
    String getId();

    String getName();

    String getDescription();

    String getHostname();

    String getIpv4();

    String getIpv6();

    List<Command> getCommands();

    List<Sensor> getSensors();

    Map<String, String> getProperties();

    @Value("#{@hostRepository.getSecretProperties(target)}")
    Map<String, String> getSecretProperties();

    @Value("#{@hostRepository.getStatus(target)}")
    String getStatus();

    @Value("#{@hostRepository.getOk(target)}")
    int getOk();

    @Value("#{@hostRepository.getWarn(target)}")
    int getWarn();

    @Value("#{@hostRepository.getCritical(target)}")
    int getCritical();

    @Value("#{@hostRepository.getUnknown(target)}")
    int getUnknown();
}
