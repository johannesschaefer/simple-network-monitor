package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.google.common.base.Strings;
import io.github.johannesschaefer.simplenetworkmonitor.ScheduleService;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.repos.CommandRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepositoryImpl;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SensorRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Controller
public class SensorController {
    @Autowired
    private Logger log;

    @Autowired
    private SensorRepository sensorRepo;

    @Autowired
    private CommandRepository commandRepo;

    @Autowired
    private HostRepository hostRepo;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/sensors/create")
    public ResponseEntity createSensor(@RequestBody Sensor sensor) {
        if (!Strings.isNullOrEmpty(sensor.getId())) {
            Optional<Sensor> sensorOrg = sensorRepo.findById(sensor.getId());
            for (Map.Entry<String, String> secProp : sensor.getSecretProperties().entrySet()) {
                if (HostRepositoryImpl.SECRET_STRING.equals(secProp.getValue())) {
                    secProp.setValue(sensorOrg.get().getSecretProperties().get(secProp.getKey()));
                }
            }
        }

        sensor.setCommand(commandRepo.findById(sensor.getCommand().getId()).get());

        Sensor s;
        if (!Strings.isNullOrEmpty(sensor.getId())) {
            Sensor sensorOrg = sensorRepo.findById(sensor.getId()).get();
            sensorOrg.setName(sensor.getName());
            sensorOrg.setCommand(sensor.getCommand());
            sensorOrg.setActive(sensor.isActive());
            sensorOrg.setProperties(sensor.getProperties());
            sensorOrg.setSecretProperties(sensor.getSecretProperties());
            sensorOrg.setInterval(sensor.getInterval());
            s = sensorRepo.save(sensorOrg);
        }
        else {
            sensor.setHost(hostRepo.findById(sensor.getHost().getId()).get());
            s = sensorRepo.save(sensor);
        }

        scheduleService.addSensor(s);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sensors/delete/{sid}")
    public ResponseEntity deleteSensor(@PathVariable("sid") String sensorId) {
        sensorRepo.findById(sensorId).ifPresent(this::delete);

        scheduleService.updateSchedule();

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void delete(Sensor sensor) {
        sensor.getSamples().clear();
        sensorRepo.save(sensor);
        sensorRepo.delete(sensor);
    }
}
