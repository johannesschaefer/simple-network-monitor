package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.google.common.base.Objects;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepositoryImpl;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SettingRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@CrossOrigin
@Controller
public class SettingController {
    @Autowired
    private Logger log;

    @Autowired
    private SettingRepository settingRepo;

    @PostMapping("/settings/update")
    public ResponseEntity createSensor(@RequestBody Setting setting) {
        Optional<Setting> sett = settingRepo.findById(setting.getName());
        if (sett.isPresent()) {
            if ((!"password".equals(sett.get().getType()) || !HostRepositoryImpl.SECRET_STRING.equals(setting.getValue())) &&
                    !Objects.equal(sett.get(), setting)) {
                sett.get().setValue(setting.getValue());
                settingRepo.save(sett.get());
            }
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
