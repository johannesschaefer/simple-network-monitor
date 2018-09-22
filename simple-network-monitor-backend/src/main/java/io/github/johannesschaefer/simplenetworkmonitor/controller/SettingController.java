package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepositoryImpl;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SettingRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class SettingController {
    @Autowired
    private Logger log;

    @Autowired
    private SettingRepository settingRepo;

    @Autowired
    private ObjectMapper mapper;

    @Value("${defaultNetwork}")
    private String defaultNetwork;

    @Value("${unsecureExport}")
    private boolean unsecureExport;

    @PostMapping("/settings/update")
    public ResponseEntity createSetting(@RequestBody Setting setting) {
        Optional<Setting> sett = settingRepo.findById(setting.getName());

        if (!sett.isPresent()) {
            throw new IllegalStateException("no such setting in database: " + setting.getName());
        }

        Setting sett1 = sett.get();
        if ((!"password".equals(sett1.getType()) || !HostRepositoryImpl.SECRET_STRING.equals(setting.getValue())) &&
                !Objects.equal(sett1, setting)) {

            if (sett1.isRequired() && Strings.emptyToNull(setting.getValue()) == null) {
                throw new IllegalArgumentException(sett1.getDisplayName() + " is required");
            }
            if (sett1.getMaxLength() != null && Strings.nullToEmpty(setting.getValue()).length() > sett1.getMaxLength()) {
                throw new IllegalArgumentException(sett1.getDisplayName() + " is longer than " + sett1.getMaxLength());
            }
            if (sett1.getMin() != null && Integer.parseInt(setting.getValue()) < sett1.getMin()) {
                throw new IllegalArgumentException(sett1.getDisplayName() + " is lower than " + sett1.getMin());
            }
            if (sett1.getMax() != null && Integer.parseInt(setting.getValue()) > sett1.getMax()) {
                throw new IllegalArgumentException(sett1.getDisplayName() + " is greater than " + sett1.getMax());
            }

            sett1.setValue(setting.getValue());
            settingRepo.save(sett1);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/settings/networks")
    @ResponseBody
    public List<String> getNetworkInterfaces() throws SocketException {
        List<String> ret = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(defaultNetwork)) {
            ret.add(defaultNetwork);
        }
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netInt : Collections.list(networkInterfaces)) {
            if(netInt.isVirtual() || netInt.isLoopback() || !netInt.isUp()) {
                continue;
            }
            netInt.getInterfaceAddresses().forEach(x -> ret.add(x.getAddress().toString().substring(1) + "/" + x.getNetworkPrefixLength()));
        }
        return ret;
    }

    private static Setting clearPassword(Setting a) {
        if ("password".equals(a.getType())) {
            a.setValue(null);
        }
        return a;
    }

    @GetMapping("/settings/export")
    @ResponseBody
    public String export() throws JsonProcessingException {
        if (unsecureExport) {
            return mapper.writeValueAsString(settingRepo.findAll());
        }
        else {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Lists.newArrayList(settingRepo.findAll()).stream().map(SettingController::clearPassword).collect(Collectors.toList()));
        }
    }
}
