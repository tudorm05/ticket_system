package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.BugCreator;
import main.ticket.FeatureRequestCreator;
import main.ticket.UiFeedbackCreator;
import main.ticket.Ticket;
import main.ticket.TicketFactory;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class ReportTicket extends Command {
    private static final int MAX_REPORT = 12;
    private final ObjectMapper mapper;
    public ReportTicket(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        TicketFactory factory = obtainCreator(command.getParams().getType());
        Ticket ticket = factory.createTicket(command.getParams());
        boolean anonymousTicket = (command.getParams().getReportedBy() == null
                || command.getParams().getReportedBy().isEmpty());
        if (anonymousTicket && !command.getParams().getType().equals("BUG")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Anonymous reports are only allowed for tickets of type BUG.");
            outputs.add(node);
            return;
        }
        if (Database.getInstance().findUser(command.getUsername()) == null) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "The user " + command.getUsername() + " does not exist.");
            outputs.add(node);
            return;
        }
        if (Database.getInstance().getStartingDate() == null) {
            Database.getInstance().setStartingDate(command.getTimestamp());
        }
        LocalDate now = LocalDate.parse(command.getTimestamp());
        LocalDate future = LocalDate.parse(Database.getInstance().getStartingDate());
        int daysBetween = (int) ChronoUnit.DAYS.between(future, now) + 1;
        if (daysBetween >= MAX_REPORT) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Tickets can only be reported during testing phases.");
            outputs.add(node);
            return;
        }
        ticket.setCreatedAt(command.getTimestamp());
        Database.getInstance().addTicket(ticket);
    }
    private TicketFactory obtainCreator(final String type) {
        switch (type) {
            case "BUG":
                return new BugCreator();
            case "FEATURE_REQUEST":
                return new FeatureRequestCreator();
            case "UI_FEEDBACK":
                return new UiFeedbackCreator();
            default:
                throw new IllegalArgumentException("Wrong type :P");
        }
    }
}
