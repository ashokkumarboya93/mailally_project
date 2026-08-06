package com.mailally.analytics.dto;

import java.util.List;

/**
 * Standardized chart JSON response payload suitable for frontend libraries (Chart.js, Recharts, ApexCharts).
 * Supports LINE, BAR, PIE, DONUT, AREA charts.
 */
public class ChartDataDto {

    private String chartType; // LINE, BAR, PIE, DONUT, AREA
    private String title;
    private List<String> labels;
    private List<ChartDatasetDto> datasets;

    public ChartDataDto() {}

    public ChartDataDto(String chartType, String title, List<String> labels, List<ChartDatasetDto> datasets) {
        this.chartType = chartType;
        this.title = title;
        this.labels = labels;
        this.datasets = datasets;
    }

    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public List<ChartDatasetDto> getDatasets() { return datasets; }
    public void setDatasets(List<ChartDatasetDto> datasets) { this.datasets = datasets; }

    public static class ChartDatasetDto {
        private String label;
        private List<Number> data;

        public ChartDatasetDto() {}

        public ChartDatasetDto(String label, List<Number> data) {
            this.label = label;
            this.data = data;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public List<Number> getData() { return data; }
        public void setData(List<Number> data) { this.data = data; }
    }
}
