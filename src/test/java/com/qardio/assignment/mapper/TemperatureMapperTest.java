package com.qardio.assignment.mapper;

import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.model.Temperature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class TemperatureMapperTest {

    @InjectMocks
    TemperatureMapper temperatureMapper;

    @Test
    public void convertToEntityTest() {
        // Given
        TemperatureDto dto = TemperatureDto
                .builder()
                .timestamp(1660924800000l)
                .value(7.25)
                .build();

        Temperature expectedTemperature = Temperature
                .builder()
                .timestamp(1660924800000l)
                .tempValue(7.25)
                .build();

        // When
        Temperature temperature = temperatureMapper.convertToEntity(dto);

        // Then
        assertNotNull(temperature);
        assertEquals(expectedTemperature.getTimestamp(), temperature.getTimestamp());
        assertEquals(expectedTemperature.getTempValue(), temperature.getTempValue(), 0);
    }
}
