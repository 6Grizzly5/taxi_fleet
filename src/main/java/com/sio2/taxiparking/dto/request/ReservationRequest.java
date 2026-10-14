package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ReservationRequest(
	Long vehiculeId,
	Long placeId,
	@NotNull LocalDateTime arriveePrevue,
	@NotNull LocalDateTime departPrevu) {
}
