package com.qardio.assignment.mapper;

import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.model.Temperature;
import org.springframework.stereotype.Component;

@Component
public class TemperatureMapper {

    public Temperature convertToEntity(TemperatureDto temperatureDto) {
        return Temperature
                .builder()
                .timestamp(temperatureDto.getTimestamp())
                .tempValue(temperatureDto.getValue())
                .build();
    }
}
