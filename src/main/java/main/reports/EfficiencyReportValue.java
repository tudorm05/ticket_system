package main.reports;

import main.ticket.Bug;
import main.ticket.FeatureRequest;
import main.ticket.UiFeedback;
import main.ticket.Visitor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class EfficiencyReportValue implements Visitor {
    private static final double MAX_BUG = 70;
    private static final double MAX_UI_FEEDBACK_FEATURE_REQUEST = 20;
    private static final double MAX_DAYS = 10;
    private static final double MAX_PERCENT = 100;
    @Override
    public double visit(final Bug bug) {
        double frequency = bug.frequencyValue();
        double severity = bug.severityValue();
        LocalDate now = LocalDate.parse(bug.getSolvedAt());
        LocalDate past = LocalDate.parse(bug.getAssignedAt());
        int daysBetween = (int) ChronoUnit.DAYS.between(past, now) + 1;
        double efficiency = (frequency + severity) * MAX_DAYS / daysBetween;
        efficiency = (efficiency * MAX_PERCENT) / MAX_BUG;
        return Math.round(efficiency * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final UiFeedback uiFeedback) {
        double usabilityScore = uiFeedback.getUsabilityScore();
        double businessValue = uiFeedback.businessValueScore();
        LocalDate now = LocalDate.parse(uiFeedback.getSolvedAt());
        LocalDate past = LocalDate.parse(uiFeedback.getAssignedAt());
        int daysBetween = (int) ChronoUnit.DAYS.between(past, now) + 1;
        double efficiency = (usabilityScore + businessValue) / daysBetween;
        efficiency = (efficiency * MAX_PERCENT) / MAX_UI_FEEDBACK_FEATURE_REQUEST;
        return Math.round(efficiency * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final FeatureRequest featureRequest) {
        double customerDemand = featureRequest.customerDemandValue();
        double businessValue = featureRequest.businessValueScore();
        LocalDate now = LocalDate.parse(featureRequest.getSolvedAt());
        LocalDate past = LocalDate.parse(featureRequest.getAssignedAt());
        int daysBetween = (int) ChronoUnit.DAYS.between(past, now) + 1;
        double efficiency = (customerDemand + businessValue) / daysBetween;
        efficiency = (efficiency * MAX_PERCENT) / MAX_UI_FEEDBACK_FEATURE_REQUEST;
        return Math.round(efficiency * MAX_PERCENT) / MAX_PERCENT;
    }
}
