package com.sio2.taxiparking.dto.request;
import jakarta.validation.constraints.*;
public record ClientRequest(
    @NotBlank String nom,
    @NotBlank String telephone,
    @NotBlank @Email String email
) {}
