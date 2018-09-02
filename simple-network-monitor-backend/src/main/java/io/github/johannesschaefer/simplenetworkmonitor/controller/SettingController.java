package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepositoryImpl;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SettingRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

@CrossOrigin
@Controller
public class SettingController {
    @Autowired
    private Logger log;

    @Autowired
    private SettingRepository settingRepo;

    @Value("${defaultNetwork}")
    private String defaultNetwork;

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
}
