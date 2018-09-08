package io.github.johannesschaefer.simplenetworkmonitor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Setting;
import io.github.johannesschaefer.simplenetworkmonitor.repos.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class StartUpInit {
    @Autowired
    private Logger log;

    @Autowired
    private HostRepository hostRepo;

    @Autowired
    private SensorRepository sensorRepo;

    @Autowired
    private CommandRepository commandRepo;

    @Autowired
    private SettingRepository settingRepo;

    @Autowired
    private SampleRepository sampleRepo;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${hosts-file}")
    private Resource hostsResource;

    @Value("${commands-file}")
    private Resource commandResource;

    @Value("${settings-file}")
    private Resource settingResource;


    @Autowired
    Environment env;

    private <T>  void doIfPresent(Optional<T> is, Consumer<T> isTrue, Runnable isFalse) {
        if (is.isPresent()) {
            isFalse.run();
        }
        else {
            isTrue.accept(null);
        }
    }

    @PostConstruct
    public void initFromFile() throws IOException {
        TypeReference<List<Setting>> settingTypeReference = new TypeReference<List<Setting>>(){};
        InputStream settingStream = settingResource.getInputStream();
        List<Setting> settings = objectMapper.readValue(settingStream, settingTypeReference);
        for (Setting setting : settings) {
            doIfPresent(settingRepo.findByName(setting.getName()), (__) -> {
                log.info("Loading setting {}", setting.getName());
                settingRepo.save(setting);
            }, () -> log.info( "Setting {} already in DB", setting.getName()));

        }

        TypeReference<List<Command>> cmdTypeReference = new TypeReference<List<Command>>(){};
        InputStream cmdStream = commandResource.getInputStream();
        List<Command> cmds = objectMapper.readValue(cmdStream, cmdTypeReference);
        for (Command cmd : cmds) {
            doIfPresent( commandRepo.findByName(cmd.getName()), (__) -> {
                log.info("loading command {}", cmd.getName());
                commandRepo.save(cmd);
            }, () -> log.info( "Command {} already in DB", cmd.getName()));
        }

        TypeReference<List<Host>> hostTypeReference = new TypeReference<List<Host>>(){};
        InputStream hostsStream = hostsResource.getInputStream();
        objectMapper.addHandler(new DeserializationProblemHandler() {
            @Override
            public Object handleMissingInstantiator(DeserializationContext ctxt, Class<?> instClass, ValueInstantiator valueInsta, JsonParser p, String msg) throws IOException {
                if (instClass.equals(Command.class)) {
                    return commandRepo.findByName(p.getText()).get();
                }
                else {
                    return this.handleMissingInstantiator(ctxt, instClass, valueInsta, p, msg);
                }
            }
        });

        List<Host> hosts = objectMapper.readValue(hostsStream, hostTypeReference);
        for (Host host : hosts) {
            doIfPresent(hostRepo.findByName(host.getName()), (__) -> {
                log.info("loading host {}", host.getName());
                host.getSensors().forEach(x -> x.setHost(host));
                hostRepo.save(host);
            }, () -> log.info("Host {} already in DB", host.getName()));
        }

        scheduleService.startSchedules();
    }
}