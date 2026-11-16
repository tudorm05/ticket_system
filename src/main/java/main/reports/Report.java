package main.reports;

import main.ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

public final class Report {
    private int totalTickets = 0;
    private int bugCount = 0;
    private int featureRequestCount = 0;
    private int uiFeedbackCount = 0;
    private int lowCount = 0;
    private int mediumCount = 0;
    private int highCount = 0;
    private int criticalCount = 0;
    private List<Double> bugValues = new ArrayList<>();
    private List<Double> featureRequestValues = new ArrayList<>();
    private List<Double> uiFeedbackValues = new ArrayList<>();
    private double bugAverage;
    private double featureRequestAverage;
    private double uiFeedbackAverage;

    /**
     * verifica ce fel de ticket e si creste numarul
     * total din tickete din care face parte
     * si scorul aceste
     * @param ticket ticketul adaugat
     * @param value valoarea in procent a ticketului
     */
    public void addTicketData(final Ticket ticket, final double value) {
        totalTickets++;
        if (ticket.getType().equals("BUG")) {
            bugCount++;
            bugValues.add(value);
        } else if (ticket.getType().equals("FEATURE_REQUEST")) {
            featureRequestCount++;
            featureRequestValues.add(value);
        } else if (ticket.getType().equals("UI_FEEDBACK")) {
            uiFeedbackCount++;
            uiFeedbackValues.add(value);
        }
        String businessPriority = ticket.getBusinessPriority();
        if (businessPriority.equals("LOW")) {
            lowCount++;
        } else if (businessPriority.equals("MEDIUM")) {
            mediumCount++;
        } else if (businessPriority.equals("HIGH")) {
            highCount++;
        } else if (businessPriority.equals("CRITICAL")) {
            criticalCount++;
        }
    }

    /**
     * calculeaza media fiecarui tip de ticket
     */
    public void finalizedScores() {
        bugAverage = calculateAverage(bugValues);
        featureRequestAverage = calculateAverage(featureRequestValues);
        uiFeedbackAverage = calculateAverage(uiFeedbackValues);
    }

    /**
     * calculeaza media ticketelor de un tip
     * formula e data pe ocw
     * @param scores procentele ticketelor
     * @return media
     */
    public double calculateAverage(final List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getBugCount() {
        return bugCount;
    }

    public int getFeatureRequestCount() {
        return featureRequestCount;
    }

    public int getUiFeedbackCount() {
        return uiFeedbackCount;
    }

    public int getLowCount() {
        return lowCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public int getHighCount() {
        return highCount;
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public double getBugAverage() {
        return bugAverage;
    }

    public double getFeatureRequestAverage() {
        return featureRequestAverage;
    }

    public double getUiFeedbackAverage() {
        return uiFeedbackAverage;
    }
}
