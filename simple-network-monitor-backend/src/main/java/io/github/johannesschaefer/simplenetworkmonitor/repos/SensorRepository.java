package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.entities.SensorStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource(excerptProjection = SensorStatus.class)
public interface SensorRepository extends PagingAndSortingRepository<Sensor, String>, SensorRepositoryCustom {
    List<Sensor> findByActiveTrue();
}
