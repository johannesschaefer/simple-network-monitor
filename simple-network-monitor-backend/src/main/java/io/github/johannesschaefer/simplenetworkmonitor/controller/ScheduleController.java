package io.github.johannesschaefer.simplenetworkmonitor.controller;

import io.github.johannesschaefer.simplenetworkmonitor.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/schedule/start")
    @ResponseBody
    public void start() {
        scheduleService.startSchedules();
    }

    @GetMapping("/schedule/stop")
    @ResponseBody
    public void stop() {
        scheduleService.stopSchedules();
    }

    @GetMapping("/schedule/isrunning")
    @ResponseBody
    public Boolean isRunning() {
        return scheduleService.isRunning();
    }
}
