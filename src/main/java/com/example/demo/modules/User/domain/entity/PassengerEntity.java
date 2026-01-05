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
@Table(name="passengers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PASSENGER")
public class PassengerEntity extends UserEntity{

    @Column(nullable=false, unique=true, length=11)
    private String cpf;

    @Column(nullable=false, length=14)
    private String phone;
}