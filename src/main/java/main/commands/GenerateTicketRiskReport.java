package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.reports.Report;
import main.reports.RiskReportValue;
import main.ticket.Ticket;
import main.ticket.Visitor;

import java.util.List;

public final class GenerateTicketRiskReport extends Command {
    private static final int MAX_NEGLIGIBLE = 25;
    private static final int MAX_MODERATE = 50;
    private static final int MAX_SIGNIFICANT = 75;
    private final ObjectMapper mapper;
    public GenerateTicketRiskReport(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ObjectNode reportNode = mapper.createObjectNode();
        ObjectNode ticketsByTypeNode = mapper.createObjectNode();
        ObjectNode ticketsByPriorityNode = mapper.createObjectNode();
        ObjectNode riskByTypeNode = mapper.createObjectNode();
        Visitor riskVisitor = new RiskReportValue();
        Report report = new Report();
        List<Ticket> tickets = Database.getInstance().getImpactReportTickets();
        for (Ticket currentTicket : tickets) {
            double value = currentTicket.accept(riskVisitor);
            report.addTicketData(currentTicket, value);
        }
        report.finalizedScores();
        reportNode.put("totalTickets", report.getTotalTickets());
        ticketsByTypeNode.put("BUG", report.getBugCount());
        ticketsByTypeNode.put("FEATURE_REQUEST", report.getFeatureRequestCount());
        ticketsByTypeNode.put("UI_FEEDBACK", report.getUiFeedbackCount());
        ticketsByPriorityNode.put("LOW", report.getLowCount());
        ticketsByPriorityNode.put("MEDIUM", report.getMediumCount());
        ticketsByPriorityNode.put("HIGH", report.getHighCount());
        ticketsByPriorityNode.put("CRITICAL", report.getCriticalCount());
        riskByTypeNode.put("BUG", risk(report.getBugAverage()));
        riskByTypeNode.put("FEATURE_REQUEST", risk(report.getFeatureRequestAverage()));
        riskByTypeNode.put("UI_FEEDBACK", risk(report.getUiFeedbackAverage()));
        reportNode.set("ticketsByType", ticketsByTypeNode);
        reportNode.set("ticketsByPriority", ticketsByPriorityNode);
        reportNode.set("riskByType", riskByTypeNode);
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
}
