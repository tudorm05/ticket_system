package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Ticket;
import main.ticket.TicketAction;

import java.util.List;

public final class UndoChangeStatus extends Command {
    private final ObjectMapper mapper;
    public UndoChangeStatus(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Ticket ticketToChange = Database.getInstance().getTicketById(command.getTicketID());
        TicketAction ticketAction;
        if (ticketToChange == null) {
            return;
        }
        if (!ticketToChange.getAssignedTo().equals(command.getUsername())) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Ticket "
                    + command.getTicketID()
                    + " is not assigned to developer "
                    + command.getUsername() + ".");
            outputs.add(node);
            return;
        }
        if (ticketToChange.getStatus().equals("IN_PROGRESS")) {
            return;
        } else if (ticketToChange.getStatus().equals("CLOSED")) {
            ticketToChange.setStatus("RESOLVED");
            ticketAction = new TicketAction.Builder("STATUS_CHANGED",
                    command.getUsername(),
                    command.getTimestamp()).
                    changedFrom("CLOSED").
                    changedTo("RESOLVED").build();
        } else {
            ticketToChange.setStatus("IN_PROGRESS");
            ticketToChange.setSolvedAt(command.getTimestamp());
            ticketAction = new TicketAction.Builder("STATUS_CHANGED",
                    command.getUsername(),
                    command.getTimestamp()).
                    changedFrom("RESOLVED").
                    changedTo("IN_PROGRESS").build();
        }
        ticketToChange.getActions().add(ticketAction);
    }
}

