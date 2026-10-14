package com.sio2.taxiparking.service;
import com.sio2.taxiparking.dto.response.StatisticsResponse;
import java.time.LocalDate;
public interface StatisticsService {
    StatisticsResponse calculer(LocalDate du, LocalDate au);
}
