package com.chrisma.devtest.oauth2api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdatePhoneContact implements Serializable {
    @Schema(example = "Jane Doe", required = true)
    private String currentName;
    @Schema(example = "081211223344", required = true)
    private String currentPhone;
    @Schema(example = "Jane D.", required = true)
    private String newName;
    @Schema(example = "081212345678", required = true)
    private String newPhone;
}
