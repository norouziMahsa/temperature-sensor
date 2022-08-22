package com.qardio.assignment.controller;

import com.qardio.assignment.dto.AggregatedTemperatureData;
import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/temperature")
@RequiredArgsConstructor
public class TemperatureController {

    private final TemperatureService temperatureService;

    @PostMapping(value = "/publish")
    public void publish(@RequestBody List<TemperatureDto> temperatureDtos) {
        temperatureService.publishTemperature(temperatureDtos);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/hourly")
    public AggregatedTemperatureData getHourlyTemperatures(@RequestParam("from_timestamp") Long fromTimestamp) {
        return temperatureService.getHourlyTemperatures(fromTimestamp);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/daily")
    public AggregatedTemperatureData getDailyTemperatures(@RequestParam("from_timestamp") Long fromTimestamp) {
        return temperatureService.getDailyTemperatures(fromTimestamp);
    }
}
