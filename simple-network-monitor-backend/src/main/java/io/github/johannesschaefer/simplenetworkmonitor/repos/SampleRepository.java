package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Sample;
import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RepositoryRestResource
public interface SampleRepository extends PagingAndSortingRepository<Sample, String> {
    //@Query("select s.status from Sample s WHERE s.type = ?1 ORDER BY s.time DESC")
    //Status getStatus(SampleType sT);

    Optional<Sample> findFirst1ByTypeOrderByTimeDesc(SampleType sT);

    @Query("SELECT s FROM Sample s WHERE s.sensor.id = ?1 AND s.type.id = ?2 ORDER BY s.time ASC")
    List<Sample> findBySensorAndType(String sensorId, String typeId);
}
