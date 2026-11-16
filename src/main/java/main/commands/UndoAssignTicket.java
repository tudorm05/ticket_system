package main.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Ticket;
import main.ticket.TicketAction;

import java.util.List;

public final class UndoAssignTicket extends Command {
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Ticket ticket = Database.getInstance().getTicketById(command.getTicketID());
        if (ticket == null) {
            return;
        }
        ticket.setAssignedTo("");
        ticket.setAssignedAt("");
        ticket.setStatus("OPEN");
        TicketAction ticketAction = new TicketAction.Builder("DE-ASSIGNED",
                command.getUsername(), command.getTimestamp()).build();
        ticket.getActions().add(ticketAction);
    }
}
