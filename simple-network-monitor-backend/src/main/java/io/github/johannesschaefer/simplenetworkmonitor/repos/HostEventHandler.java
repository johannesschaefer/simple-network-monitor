package io.github.johannesschaefer.simplenetworkmonitor.repos;

import io.github.johannesschaefer.simplenetworkmonitor.ScheduleService;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@RepositoryEventHandler(Host.class)
public class HostEventHandler {

    @Autowired
    private Logger log;

    @Autowired
    private ScheduleService scheduleService;

    @HandleAfterCreate
    @HandleAfterDelete
    @HandleAfterSave
    void updateSchedule(Host host) {
        scheduleService.updateSchedule();
    }
}
