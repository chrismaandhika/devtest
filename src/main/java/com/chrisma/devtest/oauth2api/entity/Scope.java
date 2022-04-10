package com.chrisma.devtest.oauth2api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "oauth2api_scope")
@IdClass(Scope.class)
@Setter
@Getter
public class Scope implements Serializable {
    @Id
    @Column(name = "role")
    private String role;

    @Id
    @Column(name = "scope")
    private String scope;
}
