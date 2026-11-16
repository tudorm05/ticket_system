package main.reports;


import main.ticket.Bug;
import main.ticket.FeatureRequest;
import main.ticket.UiFeedback;
import main.ticket.Visitor;

public final class ImpactReportValue implements Visitor {
    private static final double MAX_BUG = 48;
    private static final double MAX_PERCENT = 100;
    @Override
    public double visit(final Bug bug) {
        double frequency = bug.frequencyValue();
        double severity = bug.severityValue();
        double businessPriority = bug.getBusinessPriorityInt();
        double impact = frequency * severity * businessPriority;
        impact = (impact * MAX_PERCENT) / MAX_BUG;
        return Math.round(impact * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final UiFeedback uiFeedback) {
        double businessValue = uiFeedback.businessValueScore();
        double impact = businessValue * uiFeedback.getUsabilityScore();
        return Math.round(impact * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final FeatureRequest featureRequest) {
        double businessValue = featureRequest.businessValueScore();
        double customerDemand = featureRequest.customerDemandValue();
        double impact = businessValue * customerDemand;
        return Math.round(impact * MAX_PERCENT) / MAX_PERCENT;
    }
}
