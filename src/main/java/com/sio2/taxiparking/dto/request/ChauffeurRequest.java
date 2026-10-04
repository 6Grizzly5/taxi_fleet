package com.sio2.taxiparking.dto.request;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record ChauffeurRequest(
    @NotBlank @Size(max=100) String nom,
    @NotBlank String telephone,
    @NotBlank String numeroPermis,
    @NotNull @FutureOrPresent LocalDate expirationPermis,
    @NotNull LocalDate dateEmbauche,
    Long vehiculeId
) {}
