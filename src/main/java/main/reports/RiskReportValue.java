package main.reports;

import main.ticket.Bug;
import main.ticket.FeatureRequest;
import main.ticket.UiFeedback;
import main.ticket.Visitor;

public final class RiskReportValue implements Visitor {
    private static final double MAX_BUG = 12;
    private static final double MAX_UI_FEEDBACK = 11;
    private static final double MAX_FEATURE_REQUEST = 20;
    private static final double MAX_PERCENT = 100;
    @Override
    public double visit(final Bug bug) {
        double frequency = bug.frequencyValue();
        double severity = bug.severityValue();
        double risk = severity * frequency;
        risk = (risk * MAX_PERCENT) / MAX_BUG;
        return Math.round(risk * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final UiFeedback uiFeedback) {
        double businessValue = uiFeedback.businessValueScore();
        double usabilityScore = uiFeedback.getUsabilityScore();
        double risk = (MAX_UI_FEEDBACK - usabilityScore) * businessValue;
        return Math.round(risk * MAX_PERCENT) / MAX_PERCENT;
    }

    @Override
    public double visit(final FeatureRequest featureRequest) {
        double businessValue = featureRequest.businessValueScore();
        double customerDemand = featureRequest.customerDemandValue();
        double risk = businessValue + customerDemand;
        risk = (risk * MAX_PERCENT) / MAX_FEATURE_REQUEST;
        return Math.round(risk * MAX_PERCENT) / MAX_PERCENT;
    }
}
