package com.chrisma.devtest.oauth2api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeletePhoneContact implements Serializable {
    @Schema(example = "Jane Doe", required = true)
    private String name;
    @Schema(example = "081211223344", required = true)
    private String phone;
}
