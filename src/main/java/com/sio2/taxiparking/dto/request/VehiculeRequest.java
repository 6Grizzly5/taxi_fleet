package com.sio2.taxiparking.dto.request;
import jakarta.validation.constraints.*;
public record VehiculeRequest(
    @NotBlank String immatriculation,
    @NotBlank String marque,
    @NotBlank String modele,
    @NotNull @Min(1990) Integer annee,
    @NotNull @Min(1) Integer capacite,
    @NotNull @PositiveOrZero Double kilometrage
) {}
