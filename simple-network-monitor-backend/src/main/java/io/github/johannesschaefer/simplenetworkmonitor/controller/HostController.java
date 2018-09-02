package io.github.johannesschaefer.simplenetworkmonitor.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.johannesschaefer.simplenetworkmonitor.SNMUtils;
import io.github.johannesschaefer.simplenetworkmonitor.ScheduleService;
import io.github.johannesschaefer.simplenetworkmonitor.dto.CommandResult;
import io.github.johannesschaefer.simplenetworkmonitor.dto.HostOverview;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Command;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Host;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;
import io.github.johannesschaefer.simplenetworkmonitor.repos.CommandRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.HostRepositoryImpl;
import org.apache.commons.io.IOUtils;
import org.nmap4j.Nmap4j;
import org.nmap4j.core.nmap.NMapExecutionException;
import org.nmap4j.core.nmap.NMapInitializationException;
import org.nmap4j.data.NMapRun;
import org.nmap4j.data.host.Address;
import org.nmap4j.data.host.ports.Port;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Controller
public class HostController {
    @Autowired
    private Logger log;

    @Autowired
    private HostRepository hostRepo;

    @Autowired
    private CommandRepository commandRepo;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/hosts/autodiscovery")
    @ResponseBody
    public List<Host> autoDiscovery(@RequestParam("network") String network) throws NMapExecutionException, NMapInitializationException {
        Nmap4j nmap = new Nmap4j("/usr/");
        nmap.includeHosts(network);
        nmap.addFlags("-sS -p T:21,22,53,80,443,8080");
        log.info("running autodiscovery");
        nmap.execute();
        if(nmap.hasError()) {
            throw new RuntimeException(nmap.getExecutionResults().getErrors());
        }

        List<Host> hosts = Lists.newArrayList();

        final NMapRun result = nmap.getResult();
        for (org.nmap4j.data.nmaprun.Host nmapHost : result.getHosts()) {
            if("reset".equals(nmapHost.getStatus().getReason()) || nmapHost.getHostnames() == null || nmapHost.getHostnames().getHostname() == null) {
                continue;
            }
            Host host = new Host();
            host.setName(nmapHost.getHostnames().getHostname().getName());
            host.setHostname(nmapHost.getHostnames().getHostname().getName());
            for (Address address : nmapHost.getAddresses()) {
                if (address.getAddrtype().equals("ipv4")) {
                    host.setIpv4(address.getAddr());
                    addSensor(host, "ping", "check_ping_v4");
                }
                else if(address.getAddrtype().equals("ipv6")) {
                    host.setIpv6(address.getAddr());
                    addSensor(host, "ping6", "check_ping_v6");
                }
            }
            if (nmapHost.getPorts() != null) {
                for (Port port : nmapHost.getPorts().getPorts()) {
                    if (!"open".equals(port.getState().getState())) {
                        continue;
                    }
                    if (port.getPortId() == 21) {
                        addSensor(host, "ftp", "check_ftp");
                    }
                    if (port.getPortId() == 22) {
                        addSensor(host, "ssh", "check_ssh");
                    }
                    if (port.getPortId() == 53) {
                        addSensor(host, "dns", "check_dns");
                    }
                    if (port.getPortId() == 80) {
                        addSensor(host, "http", "check_http");
                    }
                    if (port.getPortId() == 443) {
                        addSensor(host, "http", "check_https");
                    }
                    if (port.getPortId() == 8080) {
                        Map<String, String> props = Maps.newHashMap();
                        props.put("port", "8080");
                        addSensor(host, "http", "check_http", props);
                    }
                }
            }

            if (host.getHostname().equals("fritz.box")) {
                addSensor(host, "FritzBox Status", "check_fritz_status");
                addSensor(host, "FritzBox Down Stream", "check_fritz_downstream");
                addSensor(host, "FritzBox Up Stream", "check_fritz_upstream");
                addSensor(host, "FritzBox Up Stream Rate", "check_fritz_upstreamrate");
                addSensor(host, "FritzBox Down Stream Rate", "check_fritz_downstreamrate");
                host.getSecretProperties().put("password", "");
            }

            Command wakeonlan = commandRepo.findByName("wakeonlan");
            host.getCommands().add(wakeonlan);
            // TODO: fetch mac address
            host.getProperties().put("mac_address", "");

            if (!hostRepo.findByHostnameOrIpv4OrIpv6(host.getHostname(), host.getIpv4(), host.getIpv6()).isPresent()) {
                hosts.add(host);
            }
        }

        return hosts;
    }

    @PostMapping("/hosts/create")
    public ResponseEntity createHost(@RequestBody Host host) {
        if (!Strings.isNullOrEmpty(host.getId())) {
            Optional<Host> hostOrg = hostRepo.findById(host.getId());
            for (Map.Entry<String, String> secProp : host.getSecretProperties().entrySet()) {
                if (HostRepositoryImpl.SECRET_STRING.equals(secProp.getValue())) {
                    secProp.setValue(hostOrg.get().getSecretProperties().get(secProp.getKey()));
                }
            }
        }
        host.getSensors().forEach(x -> x.setHost(host));

        if (!Strings.isNullOrEmpty(host.getId())) {
            Optional<Host> hostOrg = hostRepo.findById(host.getId());
            if (hostOrg.isPresent()) {
                for (Sensor s : host.getSensors()) {
                    if (s.getCommand() == null) {
                        Optional<Sensor> sOrg = hostOrg.get().getSensors().stream().filter(a -> a.getId().equals(s.getId())).findFirst();
                        sOrg.ifPresent(a -> s.setCommand(a.getCommand()));
                    }
                }
            }
        }

        hostRepo.save(host);

        if (scheduleService.isRunning()) {
            scheduleService.updateSchedules();
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void addSensor(Host host, String name, String cmdName) {
        addSensor(host, name, cmdName, Maps.newHashMap());
    }
    private void addSensor(Host host, String name, String cmdName, Map<String, String> props) {
        addSensor(host, name, cmdName, props, Maps.newHashMap());

    }
    private void addSensor(Host host, String name, String cmdName, Map<String, String> props, Map<String, String> secretProps) {
        Command cmd = commandRepo.findByName(cmdName);
        if (cmd != null) {
            Sensor sensor = Sensor.builder().host(host).command(cmd).name(name).properties(props).secretProperties(secretProps).build();
            host.getSensors().add(sensor);
        }
    }

    @GetMapping("/hosts/{hid}/commands/{cid}/run")
    @ResponseBody
    public CommandResult runCommand(@PathVariable("hid") String hostId, @PathVariable("cid") String commandId ) {
        Optional<Host> host = hostRepo.findById(hostId);
        if (host.isPresent()) {
            for (Command c : host.get().getCommands()) {
                if (c.getId().equals(commandId)) {
                    ProcessBuilder pb = new ProcessBuilder();
                    pb.command(SNMUtils.prepareCmd(c.getExec(), host.get(), Maps.newHashMap(), Maps.newHashMap()));
                    try {
                        Process process = pb.start();
                        int returnCode = process.waitFor();
                        String msg = IOUtils.toString(process.getInputStream(), "UTF-8");
                        log.trace("Execute sensor {} with result: {}", c.getName(), msg.trim());
                        return new CommandResult(returnCode==0? Status.OK:Status.WARN, msg);
                    } catch (IOException | InterruptedException e) {
                        log.warn("Error during command '{}' execution. Reason: {}", c.getName(), e.getMessage());
                        return new CommandResult(Status.CRITICAL, e.getMessage());
                    }
                }
            }
        }
        return new CommandResult(Status.UNKNOWN, "Command not found.");
    }

    @GetMapping("/hostoverview")
    @ResponseBody
    public HostOverview getHostOverview() {
        List<Host> hosts = Lists.newArrayList(hostRepo.findAll());
        long ok = 0;
        long warn = 0;
        long critical = 0;
        long unknown = 0;
        for (Host h : hosts) {
           switch (hostRepo.getStatus(h)) {
               case OK: ok++; break;
               case WARN: warn++; break;
               case CRITICAL: critical++; break;
               default: unknown++;
           }
        }
        return new HostOverview( Long.valueOf(hosts.size()), ok, warn, critical, unknown);
    }
}
