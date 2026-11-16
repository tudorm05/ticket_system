package main.ticket;

import java.util.List;

public final class Filters {
    private String type;
    private String businessPriority;
    private String searchType;
    private String createdAt;
    private String createdBefore;
    private String createdAfter;
    private String availableForAssignment;
    private List<String> keywords;
    private String expertiseArea;
    private String seniority;
    private String performanceScoreAbove;
    private String performanceScoreBelow;

    public String getBusinessPriority() {
        return businessPriority;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBefore() {
        return createdBefore;
    }

    public String getCreatedAfter() {
        return createdAfter;
    }

    public String getAvailableForAssignment() {
        return availableForAssignment;
    }

    public String getSearchType() {
        return searchType;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public String getSeniority() {
        return seniority;
    }

    public String getPerformanceScoreAbove() {
        return performanceScoreAbove;
    }

    public String getPerformanceScoreBelow() {
        return performanceScoreBelow;
    }
}
