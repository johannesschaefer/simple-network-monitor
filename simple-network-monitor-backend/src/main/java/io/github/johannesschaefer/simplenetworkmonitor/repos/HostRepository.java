package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import io.github.johannesschaefer.simplenetworkmonitor.entities.HostStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(excerptProjection = HostStatus.class)
public interface HostRepository extends PagingAndSortingRepository<Host, String>, HostRepositoryCustom {
    @Query(value = "SELECT c.* FROM COMMAND c, HOST_COMMANDS hc WHERE hc.commands_id = c.id AND hc.host_id = ?1 AND hc.commands_id = ?2", nativeQuery = true)
    Command findCommandByHostAndCommand(String hostId, String commandId);

    @Query(value = "SELECT h FROM Host h WHERE (h.hostname IS NOT NULL AND h.hostname = ?1) OR (h.ipv4 IS NOT NULL AND h.ipv4 = ?2) OR (h.ipv6 IS NOT NULL AND h.ipv6 = ?3)")
    Optional<Host> findByHostnameOrIpv4OrIpv6(String hostname, String ipv4, String ipv6);

    Optional<Host> findByName(String name);
}
