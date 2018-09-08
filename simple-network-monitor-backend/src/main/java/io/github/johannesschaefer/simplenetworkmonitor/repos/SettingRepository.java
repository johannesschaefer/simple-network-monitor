package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.entities.SettingProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
@RepositoryRestResource(excerptProjection = SettingProjection.class)
public interface SettingRepository extends PagingAndSortingRepository<Setting, String>, SettingRepositoryCustom  {
    Optional<Setting> findByName(String name);
}
