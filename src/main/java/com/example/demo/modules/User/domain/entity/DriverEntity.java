package com.example.demo.modules.User.domain.entity;

import com.example.demo.modules.User.domain.enums.PixKeyType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "pix_key", nullable = true, unique = true)
    private String pixKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "pix_key_type", nullable = true)
    private PixKeyType pixKeyType;
}
