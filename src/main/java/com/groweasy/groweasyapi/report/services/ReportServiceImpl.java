package com.groweasy.groweasyapi.report.services;

import com.groweasy.groweasyapi.loginregister.services.AuthService;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.repository.DeviceDataRepository;
import com.groweasy.groweasyapi.report.model.dto.ReportResponse;
import com.groweasy.groweasyapi.report.model.entities.Report;
import com.groweasy.groweasyapi.report.model.entities.StatisticalAnalysis;
import com.groweasy.groweasyapi.report.model.enums.RecommendationEnum;
import com.groweasy.groweasyapi.report.repository.ReportRepository;
import com.groweasy.groweasyapi.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AuthService authService;
    private final ReportRepository reportRepository;
    private final DeviceDataRepository deviceDataRepository;

    @Override
    public ReportResponse generateReport() {

        Long userId = authService.getAuthenticatedUser().id();
        Sensor sensor = deviceDataRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No data found for the user"));

        StatisticalAnalysis analysis = new StatisticalAnalysis();
        analysis.performAnalysis(sensor.getMetrics());

        Report report = reportRepository.findByUserId(userId)
                .orElseGet(() -> Report.builder()
                        .generationDate(LocalDate.now())
                        .data(analysis.getResult())
                        .recommendation(RecommendationEnum.LOW)
                        .statisticalAnalysis(analysis)
                        .user(sensor.getUser())
                        .build());

        Report newReport = updateReport(report, analysis);

        return ReportResponse.fromEntity(newReport);
    }

    private Report updateReport(Report report, StatisticalAnalysis analysis) {
        report.setGenerationDate(LocalDate.now());
        report.setData(analysis.getResult());
        report.setRecommendation(RecommendationEnum.LOW);
        report.setStatisticalAnalysis(analysis);

        return reportRepository.save(report);
    }

    @Override
    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        return ReportResponse.fromEntity(report);
    }

    @Override
    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);
    }

    @Override
    public File exportReport(Long reportId, String format) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        return exportToFile(report, format);
    }

    private File exportToFile(Report report, String format) {
        // Implementar la exportación a PDF.
        return null;
    }


    //    private void generatePDF(String filePath) throws FileNotFoundException, DocumentException {
//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream(filePath));
//        document.open();
//        document.add(new com.itextpdf.text.Paragraph("Reporte de Datos del Dispositivo"));
//        // Agregar más contenido al PDF aquí...
//        document.close();
//    }
}
