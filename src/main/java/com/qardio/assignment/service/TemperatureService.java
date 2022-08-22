package com.qardio.assignment.service;

import com.qardio.assignment.dto.AggregatedTemperatureData;
import com.qardio.assignment.dto.TemperatureDto;

import java.util.List;

public interface TemperatureService {

    void publishTemperature(List<TemperatureDto> temperatureDtos);

    void saveTemperature(TemperatureDto temperatureDto);

    AggregatedTemperatureData getHourlyTemperatures(Long fromTimestamp);

    AggregatedTemperatureData getDailyTemperatures(Long fromTimestamp);
}
