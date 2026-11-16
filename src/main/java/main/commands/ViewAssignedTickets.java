package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Comment;
import main.ticket.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

public final class ViewAssignedTickets extends Command {
    private final ObjectMapper mapper;
    public ViewAssignedTickets(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        User currentUser = Database.getInstance().findUser(command.getUsername());
        List<Ticket> userTickets = currentUser.getAssignedTickets();
        ArrayNode ticketsArray = mapper.createArrayNode();
        userTickets.sort(new Comparator<Ticket>() {
            @Override
            public int compare(final Ticket ticket1, final Ticket ticket2) {
                int businessPriorityComparison = ticket2.getBusinessPriorityInt()
                        - ticket1.getBusinessPriorityInt();
                if (businessPriorityComparison != 0) {
                    return businessPriorityComparison;
                }
                int dateComparison = ticket1.getCreatedAt().compareTo(ticket2.getCreatedAt());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return ticket1.getId() - ticket2.getId();
            }
        });
        for (Ticket currentTicket : userTickets) {
            ObjectNode ticketNode = mapper.createObjectNode();
            ArrayNode commentsArray = mapper.createArrayNode();
            ticketNode.put("id", currentTicket.getId());
            ticketNode.put("type", currentTicket.getType());
            ticketNode.put("title", currentTicket.getTitle());
            ticketNode.put("businessPriority", currentTicket.getBusinessPriority());
            ticketNode.put("status", currentTicket.getStatus());
            ticketNode.put("createdAt", currentTicket.getCreatedAt());
            ticketNode.put("assignedAt", currentTicket.getAssignedAt());
            ticketNode.put("reportedBy", currentTicket.getReportedBy());
            for (Comment currentComment : currentTicket.getComments()) {
                ObjectNode commentNode = mapper.createObjectNode();
                commentNode.put("author", currentComment.getAuthor());
                commentNode.put("content", currentComment.getContent());
                commentNode.put("createdAt", currentComment.getCreatedAt());
                commentsArray.add(commentNode);
            }
            ticketNode.set("comments", commentsArray);
            ticketsArray.add(ticketNode);
        }
        node.set("assignedTickets", ticketsArray);
        outputs.add(node);
    }
}
