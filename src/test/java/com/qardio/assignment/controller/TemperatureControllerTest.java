package com.qardio.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.model.Temperature;
import com.qardio.assignment.repository.TemperatureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TemperatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Before
    public void initTestData() {
        publishTestData();
    }

    @Test
    public void publishTemperatureTest() throws Exception {
        // Given
        var dto = TemperatureDto
                .builder()
                .timestamp(1659390089000l)
                .value(12)
                .build();
        var jsonBody = getJsonBody(Collections.singletonList(dto));

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/temperature/publish")
                .contentType(APPLICATION_JSON)
                .content(jsonBody))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getHourlyTemperaturesTest() throws Exception {
        // Given
        var fromTimeStamp = "1661068800000";// for 2022/08/21 08:00:00

        var expectedTimestamps = new ArrayList<>(Collections.singletonList(1661068800000l));
        var expectedTemperatures = new ArrayList<>(Collections.singletonList(14.0));

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/temperature/hourly")
                .param("from_timestamp", fromTimeStamp)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamps").value(expectedTimestamps))
                .andExpect(jsonPath("$.temperatures").value(expectedTemperatures));

    }

    @Test
    public void getDailyTemperaturesTest() throws Exception {
        // Given
        var fromTimeStamp = "1661157000000";// for 2022/08/22 08:30:00

        var expectedTimestamps = new ArrayList<>(Arrays.asList(1661157000000l, 1661158800000l));
        var expectedTemperatures = new ArrayList<>(Arrays.asList(16.0, 18.0));

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/temperature/daily")
                .param("from_timestamp", fromTimeStamp)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamps").value(expectedTimestamps))
                .andExpect(jsonPath("$.temperatures").value(expectedTemperatures));
    }

    /**
     * -------------UTC Time----------------
     * 1660902881000 for 2022/08/19 09:54:41
     * 1660917600000 for 2022/08/19 14:00:00
     * 1660924800000 for 2022/08/19 16:00:00
     * 1660978800000 for 2022/08/20 07:00:00
     * 1661068800000 for 2022/08/21 08:00:00
     * 1661157000000 for 2022/08/22 08:30:00
     * 1661158800000 for 2022/08/22 09:00:00
     * -------------------------------------
     */
    private void publishTestData() {
        var temperature1 = Temperature
                .builder()
                .timestamp(1660902881000l)
                .tempValue(7)
                .build();
        var temperature2 = Temperature
                .builder()
                .timestamp(1660917600000l)
                .tempValue(8)
                .build();
        var temperature3 = Temperature
                .builder()
                .timestamp(1660924800000l)
                .tempValue(10)
                .build();
        var temperature4 = Temperature
                .builder()
                .timestamp(1660978800000l)
                .tempValue(12)
                .build();
        var temperature5 = Temperature
                .builder()
                .timestamp(1661068800000l)
                .tempValue(14)
                .build();
        var temperature6 = Temperature
                .builder()
                .timestamp(1661157000000l)
                .tempValue(16)
                .build();
        var temperature7 = Temperature
                .builder()
                .timestamp(1661158800000l)
                .tempValue(18)
                .build();
        var temperatures = Arrays.asList(temperature1, temperature2, temperature3, temperature4,
                temperature4, temperature5, temperature6, temperature7);
        temperatures.forEach(temperature -> temperatureRepository.save(temperature));
    }

    private String getJsonBody(List<TemperatureDto> dtos) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(dtos);
    }
}
