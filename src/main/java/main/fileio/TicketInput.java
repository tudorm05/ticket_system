package main.fileio;

import main.ticket.Comment;

import java.util.List;

public final class TicketInput {
    private int id;
    private String type;
    private String title;
    private String businessPriority;
    private String status;
    private String expertiseArea;
    private String description;
    private String reportedBy;
    private String expectedBehavior;
    private String actualBehavior;
    private String frequency;
    private String severity;
    private String environment;
    private int errorCode;
    private String businessValue;
    private String customerDemand;
    private String uiElementId;
    private int usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;
    private String assignedAt;
    private String solvedAt;
    private String assignedTo;
    private String createdAt;
    private List<Comment> comments;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBusinessPriority() {
        return businessPriority;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedBehavior() {
        return expectedBehavior;
    }

    public String getActualBehavior() {
        return actualBehavior;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getSeverity() {
        return severity;
    }

    public String getEnvironment() {
        return environment;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getBusinessValue() {
        return businessValue;
    }

    public String getCustomerDemand() {
        return customerDemand;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public String getSolvedAt() {
        return solvedAt;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUiElementId() {
        return uiElementId;
    }

    public Integer getUsabilityScore() {
        return usabilityScore;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

}
