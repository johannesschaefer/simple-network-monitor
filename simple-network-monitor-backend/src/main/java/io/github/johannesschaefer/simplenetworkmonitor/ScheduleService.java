package io.github.johannesschaefer.simplenetworkmonitor;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sample;
import io.github.johannesschaefer.simplenetworkmonitor.entities.SampleType;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Sensor;
import io.github.johannesschaefer.simplenetworkmonitor.entities.Status;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SampleRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SampleTypeRepository;
import io.github.johannesschaefer.simplenetworkmonitor.repos.SensorRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleService {
    @Autowired
    private Logger log;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private SensorRepository sensorRepo;

    @Autowired
    private SampleRepository sampleRepo;

    @Autowired
    private SampleTypeRepository sampleTypeRepo;

    private boolean running = false;

    public void updateSchedule() {
        if (isRunning()) {
            startSchedules();
        }
    }

    public void startSchedules() {
        taskScheduler.getScheduledThreadPoolExecutor().getQueue().clear();

        List<Sensor> activeSensors = sensorRepo.findByActiveTrue();

        for (Sensor s : activeSensors) {
            addSensor(s);
        }
        running = true;
    }

    public void addSensor(Sensor s) {
        if (!s.isActive()) {
            return;
        }
        long interval = s.getInterval();
        Date startTime = new Date();
        startTime.setTime(startTime.getTime() + Math.round(Math.random() * interval));
        taskScheduler.scheduleWithFixedDelay(() -> runCommand(s), startTime, interval);
    }

    private void runCommand(Sensor s) {
        if (!s.isActive()) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(SNMUtils.prepareCmd(s.getCommand().getExec(), s.getHost(), s.getProperties(), s.getSecretProperties()));
        List<Sample> samples = Lists.newArrayList();
        try {
            Process process = pb.start();
            int returnCode = process.waitFor();
            String msg = IOUtils.toString(process.getInputStream(), "UTF-8");
            log.trace("Execute sensor {} with result: {}", s.getName(), msg.trim());

            samples.addAll(parse(s, msg, returnCode));
        } catch (IOException | InterruptedException e) {
            log.warn("Error during command '{}' execution, sensor '{}' will be disabled. Reason: {}", s.getCommand().getName(), s.getName(), e.getMessage());

            SampleType type = SampleType.builder().name("exception").sensor(s).build();
            type = sampleTypeRepo.findOrCreateSampleType(type);
            samples.add(Sample.builder().msg(e.getMessage()).sensor(s).status(Status.UNKNOWN).type(type).build());
            s.setActive(false);
        }

        samples.forEach(sampleRepo::save);

        s.getSamples().addAll(samples);
        sensorRepo.save(s);
    }

    private List<Sample> parse(Sensor sensor, String msg, int returnCode) {
        List<Sample> samples = Lists.newArrayList();

        Status s = Status.values()[returnCode];
        if (s != Status.UNKNOWN) {
            String[] split1 = msg.split("\\|");
            if (split1.length == 2) {
                String[] split2 = split1[1].split(" ");
                for (String sp : split2) {
                    Sample samp = parseString(sp, s, split1[0], sensor);
                    if (samp != null) {
                        samples.add(samp);
                    }
                }
            } else {
                SampleType type = SampleType.builder().name("error").sensor(sensor).build();
                type = sampleTypeRepo.findOrCreateSampleType(type);

                samples.add(Sample.builder().type(type).status(s).msg(msg).sensor(sensor).build());
            }
        } else {
            SampleType type = SampleType.builder().name("unknown error").sensor(sensor).build();
            type = sampleTypeRepo.findOrCreateSampleType(type);

            samples.add(Sample.builder().type(type).status(s).msg(msg).sensor(sensor).build());
        }
        return samples;
    }

    private Sample parseString(String sp, Status status, String msg, Sensor sensor) {
        String typeName;
        Double value = null;
        Double warn;
        Double critical;
        Double min;
        Double max;
        String unit = null;

        int sep1 = sp.indexOf('=');
        if (sep1<1) {
            return null;
        }
        typeName = sp.substring(0, sep1);
        String[] split = sp.substring(sep1 + 1).split(";");
        if (split.length >= 1) {
            String valueStr = split[0];
            int i = valueStr.length();
            for (; i >= 0; i--) {
                if (Character.isDigit(valueStr.charAt(i - 1)) || valueStr.charAt(i - 1) == '.') {
                    break;
                }
            }
            value = Double.valueOf(valueStr.substring(0, i));
            unit = Strings.emptyToNull(valueStr.substring(i).trim());
        }
        warn = getValue(split, 1);
        critical = getValue(split, 2);
        min = getValue(split, 3);
        max = getValue(split, 4);

        SampleType type = SampleType.builder().name(typeName).unit(unit).sensor(sensor).build();
        type = sampleTypeRepo.findOrCreateSampleType(type);

        return Sample.builder().type(type).value(value).warn(warn).critical(critical).min(min).max(max).unit(unit).status(status).msg(msg).sensor(sensor).build();
    }

    private Double getValue(String[] split, int index) {
        if (split.length >= index + 1 && !Strings.isNullOrEmpty(split[index])) {
            return Double.valueOf(split[index]);
        }
        return null;
    }

    public void stopSchedules() {
        taskScheduler.getScheduledThreadPoolExecutor().getQueue().clear();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
