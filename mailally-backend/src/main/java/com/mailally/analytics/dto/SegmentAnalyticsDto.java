package com.mailally.analytics.dto;

/**
 * Analytics breakdown for Segments (size distribution, dynamic vs static counts).
 */
public class SegmentAnalyticsDto {

    private long totalSegments;
    private long staticSegments;
    private long dynamicSegments;
    private String largestSegmentName;
    private int largestSegmentCount;
    private String smallestSegmentName;
    private int smallestSegmentCount;
    private double averageContactsPerSegment;

    public SegmentAnalyticsDto() {}

    public SegmentAnalyticsDto(long totalSegments, long staticSegments, long dynamicSegments,
                               String largestSegmentName, int largestSegmentCount, String smallestSegmentName,
                               int smallestSegmentCount, double averageContactsPerSegment) {
        this.totalSegments = totalSegments;
        this.staticSegments = staticSegments;
        this.dynamicSegments = dynamicSegments;
        this.largestSegmentName = largestSegmentName;
        this.largestSegmentCount = largestSegmentCount;
        this.smallestSegmentName = smallestSegmentName;
        this.smallestSegmentCount = smallestSegmentCount;
        this.averageContactsPerSegment = averageContactsPerSegment;
    }

    public long getTotalSegments() { return totalSegments; }
    public void setTotalSegments(long totalSegments) { this.totalSegments = totalSegments; }
    public long getStaticSegments() { return staticSegments; }
    public void setStaticSegments(long staticSegments) { this.staticSegments = staticSegments; }
    public long getDynamicSegments() { return dynamicSegments; }
    public void setDynamicSegments(long dynamicSegments) { this.dynamicSegments = dynamicSegments; }
    public String getLargestSegmentName() { return largestSegmentName; }
    public void setLargestSegmentName(String largestSegmentName) { this.largestSegmentName = largestSegmentName; }
    public int getLargestSegmentCount() { return largestSegmentCount; }
    public void setLargestSegmentCount(int largestSegmentCount) { this.largestSegmentCount = largestSegmentCount; }
    public String getSmallestSegmentName() { return smallestSegmentName; }
    public void setSmallestSegmentName(String smallestSegmentName) { this.smallestSegmentName = smallestSegmentName; }
    public int getSmallestSegmentCount() { return smallestSegmentCount; }
    public void setSmallestSegmentCount(int smallestSegmentCount) { this.smallestSegmentCount = smallestSegmentCount; }
    public double getAverageContactsPerSegment() { return averageContactsPerSegment; }
    public void setAverageContactsPerSegment(double averageContactsPerSegment) { this.averageContactsPerSegment = averageContactsPerSegment; }

    public static SegmentAnalyticsDtoBuilder builder() { return new SegmentAnalyticsDtoBuilder(); }

    public static class SegmentAnalyticsDtoBuilder {
        private long totalSegments;
        private long staticSegments;
        private long dynamicSegments;
        private String largestSegmentName;
        private int largestSegmentCount;
        private String smallestSegmentName;
        private int smallestSegmentCount;
        private double averageContactsPerSegment;

        SegmentAnalyticsDtoBuilder() {}

        public SegmentAnalyticsDtoBuilder totalSegments(long totalSegments) { this.totalSegments = totalSegments; return this; }
        public SegmentAnalyticsDtoBuilder staticSegments(long staticSegments) { this.staticSegments = staticSegments; return this; }
        public SegmentAnalyticsDtoBuilder dynamicSegments(long dynamicSegments) { this.dynamicSegments = dynamicSegments; return this; }
        public SegmentAnalyticsDtoBuilder largestSegmentName(String largestSegmentName) { this.largestSegmentName = largestSegmentName; return this; }
        public SegmentAnalyticsDtoBuilder largestSegmentCount(int largestSegmentCount) { this.largestSegmentCount = largestSegmentCount; return this; }
        public SegmentAnalyticsDtoBuilder smallestSegmentName(String smallestSegmentName) { this.smallestSegmentName = smallestSegmentName; return this; }
        public SegmentAnalyticsDtoBuilder smallestSegmentCount(int smallestSegmentCount) { this.smallestSegmentCount = smallestSegmentCount; return this; }
        public SegmentAnalyticsDtoBuilder averageContactsPerSegment(double averageContactsPerSegment) { this.averageContactsPerSegment = averageContactsPerSegment; return this; }

        public SegmentAnalyticsDto build() {
            return new SegmentAnalyticsDto(totalSegments, staticSegments, dynamicSegments, largestSegmentName,
                    largestSegmentCount, smallestSegmentName, smallestSegmentCount, averageContactsPerSegment);
        }
    }
}
