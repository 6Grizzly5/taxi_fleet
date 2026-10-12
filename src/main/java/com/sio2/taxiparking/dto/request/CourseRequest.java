package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;

public record CourseRequest(
	Long clientId,
	@NotBlank String depart,
	@NotBlank String destination,
	@NotNull @Positive Integer nombrePersonnes,
	@NotNull @Positive Double tarif) {
}
