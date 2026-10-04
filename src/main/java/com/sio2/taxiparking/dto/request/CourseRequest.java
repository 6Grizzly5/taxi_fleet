package com.sio2.taxiparking.dto.request;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record CourseRequest(
    @NotNull Long clientId,
    @NotBlank String depart,
    @NotBlank String destination,
    @NotNull @Positive Double distance,
    @NotNull @Positive Double tarif,
    @NotNull LocalDateTime dateHeure
) {}
