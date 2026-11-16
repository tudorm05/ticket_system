package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.reports.ImpactReportValue;
import main.reports.Report;
import main.ticket.Ticket;
import main.ticket.Visitor;

import java.util.List;

public final class GenerateCustomerImpactReport extends Command {
    private final ObjectMapper mapper;
    public GenerateCustomerImpactReport(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ObjectNode reportNode = mapper.createObjectNode();
        ObjectNode ticketsByTypeNode = mapper.createObjectNode();
        ObjectNode ticketsByPriorityNode = mapper.createObjectNode();
        ObjectNode riskByTypeNode = mapper.createObjectNode();
        Visitor impactVisitor = new ImpactReportValue();
        Report impactReport = new Report();
        List<Ticket> tickets = Database.getInstance().getImpactReportTickets();
        for (Ticket currentTicket : tickets) {
            double value = currentTicket.accept(impactVisitor);
            impactReport.addTicketData(currentTicket, value);
        }
        impactReport.finalizedScores();
        reportNode.put("totalTickets", impactReport.getTotalTickets());
        ticketsByTypeNode.put("BUG", impactReport.getBugCount());
        ticketsByTypeNode.put("FEATURE_REQUEST", impactReport.getFeatureRequestCount());
        ticketsByTypeNode.put("UI_FEEDBACK", impactReport.getUiFeedbackCount());
        ticketsByPriorityNode.put("LOW", impactReport.getLowCount());
        ticketsByPriorityNode.put("MEDIUM", impactReport.getMediumCount());
        ticketsByPriorityNode.put("HIGH", impactReport.getHighCount());
        ticketsByPriorityNode.put("CRITICAL", impactReport.getCriticalCount());
        riskByTypeNode.put("BUG", impactReport.getBugAverage());
        riskByTypeNode.put("FEATURE_REQUEST", impactReport.getFeatureRequestAverage());
        riskByTypeNode.put("UI_FEEDBACK", impactReport.getUiFeedbackAverage());
        reportNode.set("ticketsByType", ticketsByTypeNode);
        reportNode.set("ticketsByPriority", ticketsByPriorityNode);
        reportNode.set("customerImpactByType", riskByTypeNode);
        node.set("report", reportNode);
        outputs.add(node);
    }
}
