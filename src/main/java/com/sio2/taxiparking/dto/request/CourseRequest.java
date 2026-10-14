package com.sio2.taxiparking.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CourseRequest(
        Long clientId,
        @NotBlank @Size(max = 180) String depart,
        @NotBlank @Size(max = 180) String destination,
        @NotNull @Positive Integer nombrePersonnes,
        @NotNull @Positive Double tarif,
        @NotNull @Positive Double distance,
        LocalDateTime dateHeure) {

    public CourseRequest(Long clientId, String depart, String destination, Integer nombrePersonnes, Double tarif) {
        this(clientId, depart, destination, nombrePersonnes, tarif, 1.0, null);
    }
}
