package com.qardio.assignment.service;

import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.mapper.TemperatureMapper;
import com.qardio.assignment.model.Temperature;
import com.qardio.assignment.producer.Producer;
import com.qardio.assignment.repository.TemperatureRepository;
import com.qardio.assignment.service.impl.TemperatureServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class TemperatureServiceTest {

    @InjectMocks
    private TemperatureServiceImpl temperatureService;

    @Mock
    private Producer producer;

    @Mock
    private TemperatureMapper temperatureMapper;

    @Mock
    private TemperatureRepository temperatureRepository;

    @Test
    public void publishTemperatureTest() {
        // Given
        var dto = TemperatureDto
                .builder()
                .timestamp(1659390089000l)
                .value(7)
                .build();
        var dtos = Collections.singletonList(dto);

        doNothing().when(producer).sendMessage(any());

        // When
        temperatureService.publishTemperature(dtos);

        // Then
        verify(producer, times(1)).sendMessage(eq(dto));
    }

    @Test
    public void saveTemperatureTest() {
        // Given
        var dto = TemperatureDto
                .builder()
                .timestamp(1659390089000l)
                .value(7)
                .build();
        var expectedEntityAfterConversion = Temperature
                .builder()
                .timestamp(dto.getTimestamp())
                .tempValue(dto.getValue())
                .build();

        when(temperatureMapper.convertToEntity(any())).thenCallRealMethod();
        when(temperatureRepository.save(any())).thenReturn(expectedEntityAfterConversion);

        // When
        temperatureService.saveTemperature(dto);

        // Then
        verify(temperatureMapper, times(1)).convertToEntity(eq(dto));
        verify(temperatureRepository, times(1)).save(eq(expectedEntityAfterConversion));
    }

    @Test
    public void getHourlyTemperaturesTest() {
        // Given
        // 1661157000000 for 2022/08/22 08:30:00
        var temperature1 = Temperature
                .builder()
                .timestamp(1661157000000l)
                .tempValue(18.02)
                .build();
        // 1661158800000 for 2022/08/22 09:00:00
        var temperature2 = Temperature
                .builder()
                .timestamp(1661158800000l)
                .tempValue(7)
                .build();
        var temperatures = Arrays.asList(temperature1, temperature2);

        //for 2022/08/22 08:20:00
        var fromTimeStamp = 1661156400000l;

        var expectedTimestamps = new ArrayList<>(Arrays.asList(1661157000000l, 1661158800000l));
        var expectedTemperatures = new ArrayList<>(Arrays.asList(18.02, 7.0));

        when(temperatureRepository.findTemperatures(any(), any())).thenReturn(temperatures);

        // When
        var hourlyTemperatures = temperatureService.getHourlyTemperatures(fromTimeStamp);

        // Then
        assertEquals(expectedTimestamps, hourlyTemperatures.getTimestamps());
        assertEquals(expectedTemperatures, hourlyTemperatures.getTemperatures());
    }

    @Test
    public void getDailyTemperaturesTest() {
        // Given
        // 1660917600000 for 2022/08/19 14:00:00
        var temperature1 = Temperature
                .builder()
                .timestamp(1660917600000l)
                .tempValue(28.07)
                .build();
        // 1660924800000 for 2022/08/19 16:00:00
        var temperature2 = Temperature
                .builder()
                .timestamp(1660924800000l)
                .tempValue(17)
                .build();
        var temperatures = Arrays.asList(temperature1, temperature2);

        //for 2022/08/19 08:00:00
        var fromTimeStamp = 1660896000000l;

        var expectedTimestamps = new ArrayList<>(Arrays.asList(1660917600000l, 1660924800000l));
        var expectedTemperatures = new ArrayList<>(Arrays.asList(28.07, 17.0));

        when(temperatureRepository.findTemperatures(any(), any())).thenReturn(temperatures);

        // When
        var hourlyTemperatures = temperatureService.getDailyTemperatures(fromTimeStamp);

        // Then
        assertEquals(expectedTimestamps, hourlyTemperatures.getTimestamps());
        assertEquals(expectedTemperatures, hourlyTemperatures.getTemperatures());
    }
}
