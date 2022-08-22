package com.qardio.assignment.producer;

import com.qardio.assignment.dto.TemperatureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, TemperatureDto> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topic;

    public void sendMessage(final TemperatureDto temperatureDto) {
        log.info(String.format("#### -> Producing message -> %s", temperatureDto.toString()));
        this.kafkaTemplate.send(topic, temperatureDto);
    }
}
