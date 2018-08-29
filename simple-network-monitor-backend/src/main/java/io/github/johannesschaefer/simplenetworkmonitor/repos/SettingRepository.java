package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface SettingRepository extends PagingAndSortingRepository<Setting, String> {
}
