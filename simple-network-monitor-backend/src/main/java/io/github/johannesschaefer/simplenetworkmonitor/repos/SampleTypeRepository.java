package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
@RepositoryRestResource
public interface SampleTypeRepository extends PagingAndSortingRepository<SampleType, String>, SampleTypeRepositoryCustom {
    Optional<SampleType> findByNameAndSensor(String name, Sensor sensor);
}
