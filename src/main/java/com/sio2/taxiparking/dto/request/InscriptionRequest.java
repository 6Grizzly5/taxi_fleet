package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;

public record InscriptionRequest(
	@NotBlank @Size(min = 2, max = 100) String nom,
	@NotBlank String telephone,
	@NotBlank @Email String email,
	@NotBlank @Size(min = 6) String motDePasse) {
}
