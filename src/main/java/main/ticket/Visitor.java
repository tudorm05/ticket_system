package main.ticket;

public interface Visitor {
    double visit(Bug bug);
    double visit(UiFeedback uiFeedback);
    double visit(FeatureRequest featureRequest);
}
