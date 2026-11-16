package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.reports.EfficiencyReportValue;
import main.reports.Report;
import main.ticket.Ticket;
import main.ticket.Visitor;

import java.util.List;

public final class GenerateResolutionEfficiencyReport extends Command {
    private final ObjectMapper mapper;
    public GenerateResolutionEfficiencyReport(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ObjectNode reportNode = mapper.createObjectNode();
        ObjectNode ticketsByTypeNode = mapper.createObjectNode();
        ObjectNode ticketsByPriorityNode = mapper.createObjectNode();
        ObjectNode riskByTypeNode = mapper.createObjectNode();
        Visitor efficiencyVisitor = new EfficiencyReportValue();
        Report efficiencyReport = new Report();
        List<Ticket> tickets = Database.getInstance().getEfficiencyReportTickets();
        for (Ticket currentTicket : tickets) {
            double value = currentTicket.accept(efficiencyVisitor);
            efficiencyReport.addTicketData(currentTicket, value);
        }
        efficiencyReport.finalizedScores();
        reportNode.put("totalTickets", efficiencyReport.getTotalTickets());
        ticketsByTypeNode.put("BUG", efficiencyReport.getBugCount());
        ticketsByTypeNode.put("FEATURE_REQUEST", efficiencyReport.getFeatureRequestCount());
        ticketsByTypeNode.put("UI_FEEDBACK", efficiencyReport.getUiFeedbackCount());
        ticketsByPriorityNode.put("LOW", efficiencyReport.getLowCount());
        ticketsByPriorityNode.put("MEDIUM", efficiencyReport.getMediumCount());
        ticketsByPriorityNode.put("HIGH", efficiencyReport.getHighCount());
        ticketsByPriorityNode.put("CRITICAL", efficiencyReport.getCriticalCount());
        riskByTypeNode.put("BUG", efficiencyReport.getBugAverage());
        riskByTypeNode.put("FEATURE_REQUEST", efficiencyReport.getFeatureRequestAverage());
        riskByTypeNode.put("UI_FEEDBACK", efficiencyReport.getUiFeedbackAverage());
        reportNode.set("ticketsByType", ticketsByTypeNode);
        reportNode.set("ticketsByPriority", ticketsByPriorityNode);
        reportNode.set("efficiencyByType", riskByTypeNode);
        node.set("report", reportNode);
        outputs.add(node);
    }
}
