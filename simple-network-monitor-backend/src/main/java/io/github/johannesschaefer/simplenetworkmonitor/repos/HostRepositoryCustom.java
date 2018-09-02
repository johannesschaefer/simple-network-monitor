package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;

import java.util.Map;

public interface HostRepositoryCustom {
    Status getStatus(Host host);

    Map<String, String> getSecretProperties(Host host);

    int getOk(Host host);

    int getWarn(Host host);

    int getCritical(Host host);

    int getUnknown(Host host);

    //Host save(Host host);
}
