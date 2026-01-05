package com.example.demo.modules.User.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("DRIVER")
public class DriverEntity extends UserEntity{

    @Column(unique = true, nullable = false, length = 11)
    private String cnh;

    @Column(nullable = false, length = 7)
    private String vehicleRegistration;

    @Column(nullable = false)
    private String modelVehicle;

    @Column(nullable = false)
    private String yearVehicle;

    @Column(nullable = false)
    private String colorVehicle;
}
