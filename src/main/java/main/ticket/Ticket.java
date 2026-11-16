package main.ticket;

import main.fileio.TicketInput;

import java.util.ArrayList;
import java.util.List;

public abstract class Ticket {
    private static final int LOW_VALUE = 1;
    private static final int MEDIUM_VALUE = 2;
    private static final int HIGH_VALUE = 3;
    private static final int CRITICAL_VALUE = 4;
    private static Integer startingId = 0;
    private Integer id;
    private String type;
    private String title;
    private String businessPriority;
    private String status;
    private String expertiseArea;
    private String description;
    private String reportedBy;
    private String assignedAt;
    private String solvedAt;
    private String assignedTo;
    private String createdAt;
    private String assignedMilestoneName;
    private List<Comment> comments;
    private List<TicketAction> actions;
    public Ticket(final TicketInput ticketInput) {
        this.id = startingId;
        this.type = ticketInput.getType();
        this.title = ticketInput.getTitle();
        this.status = "OPEN";
        this.expertiseArea = ticketInput.getExpertiseArea();
        this.description = ticketInput.getDescription();
        this.reportedBy = ticketInput.getReportedBy();
        this.assignedAt = ticketInput.getAssignedAt();
        this.solvedAt = ticketInput.getSolvedAt();
        this.assignedTo = ticketInput.getAssignedTo();
        this.comments = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.assignedAt = "";
        this.assignedTo = "";
        this.solvedAt = "";
        if (ticketInput.getReportedBy().isEmpty()) {
            this.businessPriority = "LOW";
        } else {
            this.businessPriority = ticketInput.getBusinessPriority();
        }
        startingId++;
    }

    /**
     * pentru design pattern visitor
     * @param visitor
     * @return
     */
    public abstract double accept(Visitor visitor);

    /**
     * o folosesc pentru a reseta
     * indexul de start al ticketelor
     * atunci cand incepe un nou test
     */
    public static void resetIdCounter() {
        startingId = 0;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }
    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }
    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }
    /**
     *
     * @return
     */
    public String getBusinessPriority() {
        return businessPriority;
    }
    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }
    /**
     *
     * @return
     */
    public String getExpertiseArea() {
        return expertiseArea;
    }
    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     *
     * @return
     */
    public String getReportedBy() {
        return reportedBy;
    }
    /**
     *
     * @return
     */
    public String getAssignedAt() {
        return assignedAt;
    }
    /**
     *
     * @return
     */
    public String getSolvedAt() {
        return solvedAt;
    }
    /**
     *
     * @return
     */
    public String getAssignedTo() {
        return assignedTo;
    }
    /**
     *
     * @return
     */
    public List<Comment> getComments() {
        return comments;
    }
    /**
     *
     * @return
     */
    public String getCreatedAt() {
        return createdAt;
    }
    /**
     *
     * @return
     */
    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }
    /**
     *
     * @return
     */
    public void setAssignedAt(final String assignedAt) {
        this.assignedAt = assignedAt;
    }
    /**
     *
     * @return
     */
    public void setAssignedTo(final String assignedTo) {
        this.assignedTo = assignedTo;
    }
    /**
     *
     * @return
     */
    public void setStatus(final String status) {
        this.status = status;
    }
    /**
     *
     * @return
     */
    public String getAssignedMilestoneName() {
        return assignedMilestoneName;
    }
    /**
     *
     * @return
     */
    public void setBusinessPriority(final String businessPriority) {
        this.businessPriority = businessPriority;
    }
    /**
     *
     * @return
     */
    public void setAssignedMilestoneName(final String assignedMilestoneName) {
        this.assignedMilestoneName = assignedMilestoneName;
    }
    /**
     *tranforma businessPriority dintr-un string
     * intr-un int pentru a face mai usoara sortarea
     * @return
     */
    public int getBusinessPriorityInt() {
        if (this.businessPriority.equals("CRITICAL")) {
            return CRITICAL_VALUE;
        } else if (this.businessPriority.equals("HIGH")) {
            return HIGH_VALUE;
        } else if (this.businessPriority.equals("MEDIUM")) {
            return MEDIUM_VALUE;
        } else {
            return LOW_VALUE;
        }
    }
    /**
     *
     * @return
     */
    public void setComments(final List<Comment> comments) {
        this.comments = comments;
    }
    /**
     *
     * @return
     */
    public void setSolvedAt(final String solvedAt) {
        this.solvedAt = solvedAt;
    }
    /**
     *
     * @return
     */
    public List<TicketAction> getActions() {
        return actions;
    }
}
