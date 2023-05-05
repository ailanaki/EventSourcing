package org.example.statistics;


public class AverageStatistics {
    private final double visitsPerDay;
    private final double averageVisitMinutes;

    public AverageStatistics(double visitsPerDay, double averageVisitMinutes) {
        this.visitsPerDay = visitsPerDay;
        this.averageVisitMinutes = averageVisitMinutes;
    }

    public double getVisitsPerDay() {
        return visitsPerDay;
    }

    public double getAverageVisitMinutes() {
        return averageVisitMinutes;
    }

}