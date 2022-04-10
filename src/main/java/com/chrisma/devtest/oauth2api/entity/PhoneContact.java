package com.chrisma.devtest.oauth2api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "phone_contact")
@Getter
@Setter
public class PhoneContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    @Schema(example = "John Doe", required = true)
    private String name;

    @Column(name = "phone")
    @Schema(example = "081212345678", required = true)
    private String phone;
}
