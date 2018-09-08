package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.repos.CommandRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class CommandController {
    @Autowired
    private Logger log;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CommandRepository commandRepo;

    @Value("${unsecureExport}")
    private boolean unsecureExport;

    @GetMapping("/commands/export")
    @ResponseBody
    public String export() throws JsonProcessingException {
        List<Command> cmdList = Lists.newArrayList(commandRepo.findAll());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmdList.stream().map(CommandController::exportMapping).collect(Collectors.toList()));
    }

    private static Map<String, Object> exportMapping(Command command) {
        HashMap<String, Object> ret = Maps.newHashMap();
        ret.put("name", command.getName());
        ret.put("description", command.getDescription());
        ret.put("exec", command.getExec());
        ret.put("version", command.getVersion());
        return ret;
    }
}
