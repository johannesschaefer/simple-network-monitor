package io.github.johannesschaefer.simplenetworkmonitor.controller;

import io.github.johannesschaefer.simplenetworkmonitor.repos.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Controller
public class SampleController {
    @Autowired
    private SampleRepository sampleRepo;
/*
    @GetMapping("/samples")
    @ResponseBody
    public void find(@RequestParam("sensorId") String sensorId, @RequestParam("typeId") String typeId) {
        //return sampleRepo
    }
*/
}
