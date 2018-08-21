package io.github.johannesschaefer.simplenetworkmonitor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import io.github.johannesschaefer.simplenetworkmonitor.repos.CommandRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SampleRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SensorRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    private SampleRepository sampleRepo;

    @Autowired
    private ScheduleService scheduleService;

    @Value("${libPath}")
    private String libPath;

    @PostConstruct
    public void initFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();


        TypeReference<List<Command>> cmdTypeReference = new TypeReference<List<Command>>(){};
        InputStream cmdStream = this.getClass().getClassLoader().getResourceAsStream("commands.json");
        List<Command> cmds = objectMapper.readValue(cmdStream, cmdTypeReference);
        for (Command cmd : cmds) {
            log.info("loading cmd {}", cmd.getName());
            commandRepo.save(cmd);
        }

        TypeReference<List<Host>> hostTypeReference = new TypeReference<List<Host>>(){};
        InputStream hostsStream = this.getClass().getClassLoader().getResourceAsStream("hosts.json");
        objectMapper.addHandler(new DeserializationProblemHandler() {
            @Override
            public Object handleMissingInstantiator(DeserializationContext ctxt, Class<?> instClass, ValueInstantiator valueInsta, JsonParser p, String msg) throws IOException {
                if (instClass.equals(Command.class)) {
                    return commandRepo.findByName(p.getText());
                }
                else {
                    return this.handleMissingInstantiator(ctxt, instClass, valueInsta, p, msg);
                }
            }
        });

        List<Host> hosts = objectMapper.readValue(hostsStream, hostTypeReference);
        for (Host host : hosts) {
            log.info("loading host {}", host.getName());
            hostRepo.save(host);
        }

        scheduleService.startSchedules();
    }
}