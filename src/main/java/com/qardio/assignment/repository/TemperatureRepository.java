package com.qardio.assignment.repository;

import com.qardio.assignment.model.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemperatureRepository extends JpaRepository<Temperature, Long> {

    @Query("SELECT t FROM Temperature t WHERE timestamp >= ?1 and timestamp <= ?2 order by timestamp")
    List<Temperature> findTemperatures(Long fromTimestamp, Long toTimestamp);
}
