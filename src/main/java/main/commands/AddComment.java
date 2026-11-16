package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Comment;
import main.ticket.Ticket;
import main.users.User;

import java.util.List;

public final class AddComment extends Command {
    private static final int MAX_LENGTH = 10;
    private final ObjectMapper mapper;
    public AddComment(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Ticket ticket = Database.getInstance().getTicketById(command.getTicketID());
        User user = Database.getInstance().findUser(command.getUsername());
        if (ticket == null) {
            return;
        }
        if (ticket.getReportedBy().equals("")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Comments are not allowed on anonymous tickets.");
            outputs.add(node);
            return;
        }
        if (user.getRole().equals("REPORTER") && ticket.getStatus().equals("CLOSED")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Reporters cannot comment on CLOSED tickets.");
            outputs.add(node);
            return;
        }
        if (command.getComment().length() < MAX_LENGTH) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Comment must be at least 10 characters long.");
            outputs.add(node);
            return;
        }
        if (user.getRole().equals("DEVELOPER") && !user.getAssignedTickets().contains(ticket)) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Ticket " + command.getTicketID()
                        + " is not assigned to the developer " + command.getUsername() + ".");
            outputs.add(node);
            return;
        }
        if (user.getRole().equals("REPORTER")
                && !ticket.getReportedBy().equals(command.getUsername())) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Reporter " + command.getUsername()
                        + " cannot comment on ticket " + command.getTicketID() + ".");
            outputs.add(node);
            return;
        }
        Comment comment = new Comment(command);
        ticket.getComments().add(comment);
    }
}
