package org.example.controllers;

import org.example.service.ReportService;
import org.example.statistics.AverageStatistics;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/days")
    public Map<LocalDateTime, Integer> getDayStatistics(@RequestParam int daysBack) {
        return service.getDayStatistics(daysBack);
    }

    @GetMapping("/average")
    public AverageStatistics averageStatistics() {
        return service.averageStatistics();
    }

}