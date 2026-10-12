package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ChauffeurInscriptionRequest(
	@NotBlank @Size(max = 100) String nom,
	@NotBlank String telephone,
	@NotBlank @Email String email,
	@NotBlank @Size(min = 6) String motDePasse,
	@NotBlank String numeroPermis,
	@NotNull @FutureOrPresent LocalDate expirationPermis,
	@NotBlank String immatriculation,
	@NotBlank String marque,
	@NotBlank String modele,
	@NotNull @Min(1900) Integer annee,
	@NotNull @Positive Integer capacite) {
}
