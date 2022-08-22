package com.qardio.assignment.service.impl;

import com.qardio.assignment.dto.AggregatedTemperatureData;
import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.mapper.TemperatureMapper;
import com.qardio.assignment.model.Temperature;
import com.qardio.assignment.producer.Producer;
import com.qardio.assignment.repository.TemperatureRepository;
import com.qardio.assignment.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static javax.management.timer.Timer.ONE_DAY;
import static javax.management.timer.Timer.ONE_HOUR;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final Producer producer;
    private final TemperatureMapper mapper;
    private final TemperatureRepository temperatureRepository;

    @Override
    public void publishTemperature(List<TemperatureDto> temperatureDtos) {
        temperatureDtos.forEach(producer::sendMessage);
    }

    @CacheEvict(value = {"hourly-temperatures", "daily-temperatures"}, allEntries = true)
    public void saveTemperature(TemperatureDto temperatureDto) {
        temperatureRepository.save(mapper.convertToEntity(temperatureDto));
    }

    @Override
    @Cacheable("hourly-temperatures")
    public AggregatedTemperatureData getHourlyTemperatures(Long fromTimestamp) {
        return getTemperatures(fromTimestamp, ONE_HOUR);
    }

    @Override
    @Cacheable("daily-temperatures")
    public AggregatedTemperatureData getDailyTemperatures(Long fromTimestamp) {
        return getTemperatures(fromTimestamp, ONE_DAY);
    }

    private AggregatedTemperatureData getTemperatures(Long fromTimestamp, Long offset) {
        var temperatures = temperatureRepository.findTemperatures(fromTimestamp, fromTimestamp + offset);
        return getAggregateTemperatures(temperatures);
    }

    private AggregatedTemperatureData getAggregateTemperatures(List<Temperature> temperatures) {
        var aggregatedTemperatureData = new AggregatedTemperatureData();
        for (Temperature temperature : temperatures) {
            aggregatedTemperatureData.addTimestamp(temperature.getTimestamp());
            aggregatedTemperatureData.addTemperature(temperature.getTempValue());
        }
        return aggregatedTemperatureData;
    }
}
