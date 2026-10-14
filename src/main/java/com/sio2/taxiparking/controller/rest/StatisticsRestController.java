package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.response.StatisticsResponse;
import com.sio2.taxiparking.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {
    private final StatisticsService statistics;
    public StatisticsRestController(StatisticsService statistics) { this.statistics = statistics; }

    @GetMapping("/revenue")
    public StatisticsResponse revenue(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to) {
        return statistics.calculer(from, to);
    }

    @GetMapping("/activity")
    public StatisticsResponse activity(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to) {
        return statistics.calculer(from, to);
    }
}
