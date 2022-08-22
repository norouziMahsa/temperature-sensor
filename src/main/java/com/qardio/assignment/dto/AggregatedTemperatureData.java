package com.qardio.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AggregatedTemperatureData {

    private final List<Long> timestamps = new ArrayList<>();
    private final List<Double> temperatures = new ArrayList<>();

    public void addTimestamp(long timestamp) {
        timestamps.add(timestamp);
    }

    public void addTemperature(double temperature) {
        temperatures.add(temperature);
    }
}
