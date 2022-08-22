package com.qardio.assignment.consumer;

import com.qardio.assignment.dto.TemperatureDto;
import com.qardio.assignment.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    private final TemperatureService temperatureService;

    @KafkaListener(topics = "${kafka.topic.name}")
    public void consume(TemperatureDto temperatureDto) {
        log.info(String.format("#### -> Consumed message -> %s", temperatureDto.toString()));
        temperatureService.saveTemperature(temperatureDto);
    }
}
