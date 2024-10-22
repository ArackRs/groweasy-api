package com.groweasy.groweasyapi.report.model.entities;

import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
public class StatisticalAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String result;

    public void performAnalysis(List<Metric> metrics) {
        if (metrics.isEmpty()) {
            this.result = "No se encontraron métricas para analizar.";
            return;
        }

        String temperatureAnalysis = analyzeMetric(metrics, SensorType.TEMPERATURE);
        String humidityAnalysis = analyzeMetric(metrics, SensorType.HUMIDITY);
        String lightAnalysis = analyzeMetric(metrics, SensorType.LIGHT);
        String periodAnalysis = analyzePeriod(metrics);

        int count = metrics.size() / 3;

        // Unificar el resultado
        this.result = String.format(
                "%s\n%s\n%s\n%s\nCantidad de registros analizados: %d",
                temperatureAnalysis, humidityAnalysis, lightAnalysis, periodAnalysis, count
        );
    }

    private String analyzeMetric(List<Metric> metrics, SensorType type) {
        List<Metric> filteredMetrics = metrics.stream()
                .filter(metric -> metric.getType() == type)
                .toList();

        if (filteredMetrics.isEmpty()) {
            return String.format("No hay datos disponibles para %s.", type.name().toLowerCase());
        }

        double avg = filteredMetrics.stream().mapToDouble(Metric::getValue).average().orElse(0);
        double max = filteredMetrics.stream().mapToDouble(Metric::getValue).max().orElse(0);
        double min = filteredMetrics.stream().mapToDouble(Metric::getValue).min().orElse(0);

        String unit = filteredMetrics.getFirst().getUnit();

        return String.format(
                "%s promedio: %.2f %s, Máxima: %.2f %s, Mínima: %.2f %s",
                type.name().toLowerCase(), avg, unit, max, unit, min, unit
        );
    }

    private String analyzePeriod(List<Metric> metrics) {
        LocalDateTime startDate = metrics.stream()
                .map(Metric::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime endDate = metrics.stream()
                .map(Metric::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return String.format("Período de análisis: %s a %s", startDate, endDate);
    }
}

