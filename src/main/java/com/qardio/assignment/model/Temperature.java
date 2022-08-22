package com.qardio.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@Entity
@Table(name = "temperature")
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {

    @Id
    @Column(name = "time_stamp")
    private long timestamp;

    @Column(name = "temp_value")
    private double tempValue;
}
