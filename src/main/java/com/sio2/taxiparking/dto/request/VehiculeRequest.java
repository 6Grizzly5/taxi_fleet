package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;

public record VehiculeRequest(
	@NotBlank String immatriculation,
	@NotBlank String marque,
	@NotBlank String modele,
	@NotNull @Min(1900) Integer annee,
	@NotNull @Positive Integer capacite,
	@NotNull @PositiveOrZero Double kilometrage) {
}
