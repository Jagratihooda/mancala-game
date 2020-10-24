package com.bol.mancala.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

/**
 * Player DTO class
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
}
