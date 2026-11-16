package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.reports.ImpactReportValue;
import main.reports.Report;
import main.reports.RiskReportValue;
import main.ticket.Ticket;
import main.ticket.Visitor;

import java.util.List;

public final class AppStabilityReport extends Command {
    private static final int MAX_NEGLIGIBLE = 25;
    private static final int MAX_MODERATE = 50;
    private static final int MAX_SIGNIFICANT = 75;
    private static final int MAX_IMPACT = 50;
    private final ObjectMapper mapper;
    public AppStabilityReport(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ObjectNode reportNode = mapper.createObjectNode();
        ObjectNode ticketsByTypeNode = mapper.createObjectNode();
        ObjectNode ticketsByPriorityNode = mapper.createObjectNode();
        ObjectNode riskByTypeNode = mapper.createObjectNode();
        ObjectNode impactByTypeNode = mapper.createObjectNode();
        Visitor riskVisitor = new RiskReportValue();
        Report riskReport = new Report();
        Visitor impactVisitor = new ImpactReportValue();
        Report impactReport = new Report();
        List<Ticket> tickets = Database.getInstance().getImpactReportTickets();
        for (Ticket currentTicket : tickets) {
            double riskValue = currentTicket.accept(riskVisitor);
            riskReport.addTicketData(currentTicket, riskValue);
            double impactValue = currentTicket.accept(impactVisitor);
            impactReport.addTicketData(currentTicket, impactValue);
        }
        riskReport.finalizedScores();
        impactReport.finalizedScores();
        reportNode.put("totalOpenTickets", riskReport.getTotalTickets());
        ticketsByTypeNode.put("BUG", riskReport.getBugCount());
        ticketsByTypeNode.put("FEATURE_REQUEST", riskReport.getFeatureRequestCount());
        ticketsByTypeNode.put("UI_FEEDBACK", riskReport.getUiFeedbackCount());
        ticketsByPriorityNode.put("LOW", riskReport.getLowCount());
        ticketsByPriorityNode.put("MEDIUM", riskReport.getMediumCount());
        ticketsByPriorityNode.put("HIGH", riskReport.getHighCount());
        ticketsByPriorityNode.put("CRITICAL", riskReport.getCriticalCount());
        riskByTypeNode.put("BUG", risk(riskReport.getBugAverage()));
        riskByTypeNode.put("FEATURE_REQUEST", risk(riskReport.getFeatureRequestAverage()));
        riskByTypeNode.put("UI_FEEDBACK", risk(riskReport.getUiFeedbackAverage()));
        impactByTypeNode.put("BUG", impactReport.getBugAverage());
        impactByTypeNode.put("FEATURE_REQUEST", impactReport.getFeatureRequestAverage());
        impactByTypeNode.put("UI_FEEDBACK", impactReport.getUiFeedbackAverage());
        reportNode.set("openTicketsByType", ticketsByTypeNode);
        reportNode.set("openTicketsByPriority", ticketsByPriorityNode);
        reportNode.set("riskByType", riskByTypeNode);
        reportNode.set("impactByType", impactByTypeNode);
        reportNode.put("appStability", checkStability(riskReport, impactReport));
        node.set("report", reportNode);
        outputs.add(node);
    }

    /**
     * transforma riscul unui ticket din
     * valoare exprimata in procenta intr-un
     * string
     * @param value risc exprimat in procente
     * @return risc exprimat prin string
     */
    public String risk(final double value) {
        if (value < MAX_NEGLIGIBLE) {
            return "NEGLIGIBLE";
        } else if (value < MAX_MODERATE) {
            return "MODERATE";
        } else if (value < MAX_SIGNIFICANT) {
            return "SIGNIFICANT";
        } else {
            return "MAJOR";
        }
    }

    /**
     * verifica riscul ticketelor si impactul acestora
     * daca se respecta conditiile din cerinta
     * aplicatia este considerata stable
     * @param riskReport riscul ticketelor
     * @param impactReport impactul ticketelor
     * @return stabilitatea aplicati
     */
    public String checkStability(final Report riskReport, final Report impactReport) {
        String bugRisk = risk(riskReport.getBugAverage());
        String uiFeedbackRisk = risk(riskReport.getUiFeedbackAverage());
        String featureRequestRisk = risk(riskReport.getFeatureRequestAverage());
        double bugAverage = impactReport.getBugAverage();
        double uiFeedbackAverage = impactReport.getUiFeedbackAverage();
        double featureRequestAverage = impactReport.getFeatureRequestAverage();
        if (riskReport.getTotalTickets() == 0) {
            return "STABLE";
        } else if (bugRisk.equals("SIGNIFICANT")
                || uiFeedbackRisk.equals("SIGNIFICANT")
                || featureRequestRisk.equals("SIGNIFICANT")) {
            return "UNSTABLE";
        } else if (bugRisk.equals("NEGLIGIBLE")
                && uiFeedbackRisk.equals("NEGLIGIBLE")
                && featureRequestRisk.equals("NEGLIGIBLE")
                && bugAverage < MAX_IMPACT
                && uiFeedbackAverage < MAX_IMPACT
                && featureRequestAverage < MAX_IMPACT) {
            return "STABLE";
        } else {
            return "PARTIALLY STABLE";
        }
    }
}
