package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Ticket;

import java.util.List;

public final class UndoAddComment extends Command {
    private final ObjectMapper mapper;
    public UndoAddComment(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Ticket ticketToUncomment = Database.getInstance().getTicketById(command.getTicketID());
        if (ticketToUncomment == null) {
            return;
        }
        if (ticketToUncomment.getReportedBy().equals("")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Comments are not allowed on anonymous tickets.");
            outputs.add(node);
            return;
        }
        for (int i = ticketToUncomment.getComments().size() - 1; i >= 0; i--) {
            if (ticketToUncomment.getComments().get(i).getAuthor().equals(command.getUsername())) {
                ticketToUncomment.getComments().remove(i);
                break;
            }
        }
    }
}
