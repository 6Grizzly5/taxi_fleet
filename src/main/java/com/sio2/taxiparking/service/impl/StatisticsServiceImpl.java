package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.response.StatisticsResponse;
import com.sio2.taxiparking.entity.Course;
import com.sio2.taxiparking.enumeration.StatutCourse;
import com.sio2.taxiparking.repository.CourseRepository;
import com.sio2.taxiparking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final CourseRepository courses;
    @Value("${taxi.commission-rate:0.10}") private BigDecimal commissionRate;
    public StatisticsServiceImpl(CourseRepository courses) { this.courses = courses; }

    @Override @Transactional(readOnly = true) public StatisticsResponse calculer(LocalDate from, LocalDate to) {
        LocalDate start = from == null ? LocalDate.of(LocalDate.now().getYear(), 1, 1) : from;
        LocalDate end = to == null ? LocalDate.now() : to;
        if (end.isBefore(start)) throw new IllegalArgumentException("La date de fin doit être postérieure ou égale à la date de début");
        List<Course> period = courses.findAll().stream()
                .filter(c -> c.getDateHeure() != null && !c.getDateHeure().toLocalDate().isBefore(start) && !c.getDateHeure().toLocalDate().isAfter(end))
                .toList();
        List<Course> completed = period.stream().filter(c -> c.getStatut() == StatutCourse.TERMINEE).toList();
        List<Course> cancelled = period.stream().filter(c -> c.getStatut() == StatutCourse.ANNULEE).toList();
        BigDecimal revenue = completed.stream().map(c -> money(c.getTarif())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal commission = revenue.multiply(commissionRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal driverRevenue = revenue.subtract(commission).setScale(2, RoundingMode.HALF_UP);
        BigDecimal average = completed.isEmpty() ? BigDecimal.ZERO.setScale(2) : revenue.divide(BigDecimal.valueOf(completed.size()), 2, RoundingMode.HALF_UP);
        double distance = completed.stream().mapToDouble(c -> c.getDistance() == null ? 0 : c.getDistance()).sum();
        double cancelRate = period.isEmpty() ? 0 : Math.round(cancelled.size() * 10000.0 / period.size()) / 100.0;
        Map<String, BigDecimal> byDriver = groupMoney(completed, c -> c.getChauffeur() == null ? "Non attribué" : c.getChauffeur().getNom());
        Map<String, BigDecimal> byVehicle = groupMoney(completed, c -> c.getVehicule() == null ? "Non attribué" : c.getVehicule().getImmatriculation());
        Map<String, BigDecimal> byDay = groupMoney(completed, c -> c.getDateHeure().toLocalDate().toString());
        Map<String, BigDecimal> byMonth = groupMoney(completed, c -> c.getDateHeure().toLocalDate().withDayOfMonth(1).toString().substring(0,7));
        Map<String, Long> driverActivity = completed.stream().filter(c -> c.getChauffeur()!=null).collect(Collectors.groupingBy(c -> c.getChauffeur().getNom(), TreeMap::new, Collectors.counting()));
        Map<String, Long> vehicleUse = completed.stream().filter(c -> c.getVehicule()!=null).collect(Collectors.groupingBy(c -> c.getVehicule().getImmatriculation(), TreeMap::new, Collectors.counting()));
        String busiestDriver = driverActivity.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Aucune donnée");
        String mostUsedVehicle = vehicleUse.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Aucune donnée");
        return new StatisticsResponse(start, end, period.size(), completed.size(), cancelled.size(), revenue, commission, driverRevenue,
                Math.round(distance*100.0)/100.0, cancelRate, average, busiestDriver, mostUsedVehicle,
                byDriver, byVehicle, byDay, byMonth, driverActivity, vehicleUse);
    }
    private BigDecimal money(Double amount) { return amount == null ? BigDecimal.ZERO : BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP); }
    private Map<String, BigDecimal> groupMoney(List<Course> data, java.util.function.Function<Course,String> key) {
        return data.stream().collect(Collectors.groupingBy(key, TreeMap::new, Collectors.reducing(BigDecimal.ZERO,
                c -> money(c.getTarif()), BigDecimal::add)));
    }
}
