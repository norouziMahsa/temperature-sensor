package com.qardio.assignment.repository;

import com.qardio.assignment.model.Temperature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static javax.management.timer.Timer.ONE_DAY;
import static javax.management.timer.Timer.ONE_HOUR;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TemperatureRepositoryTest {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Before
    public void initTestData() {
        publishTestData();
    }

    @Test
    public void findTemperaturesTestWhenAskedForHourly() {
        // Given
        var fromTimeStamp = 1661157000000l;// for 2022/08/22 08:30:00
        var expectedNumberOfTemperatures = 2;

        // When
        var temperatures = temperatureRepository
                .findTemperatures(fromTimeStamp, fromTimeStamp + ONE_HOUR);

        // Then
        assertFalse(temperatures.isEmpty());
        assertEquals(expectedNumberOfTemperatures, temperatures.size());
        assertTrue(temperatures.get(0).getTimestamp() >= fromTimeStamp
                && temperatures.get(0).getTimestamp() <= fromTimeStamp + ONE_HOUR);
    }


    @Test
    public void findTemperaturesTestWhenAskedForDaily() {
        // Given
        var fromTimeStamp = 1660917600000l;// for 2022/08/19 14:00:00
        var expectedNumberOfTemperatures = 3;

        // When
        var temperatures = temperatureRepository
                .findTemperatures(fromTimeStamp, fromTimeStamp + ONE_DAY);

        // Then
        assertFalse(temperatures.isEmpty());
        assertEquals(expectedNumberOfTemperatures, temperatures.size());
        assertTrue(temperatures.get(1).getTimestamp() >= fromTimeStamp
                && temperatures.get(1).getTimestamp() <= fromTimeStamp + ONE_DAY);
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
}
