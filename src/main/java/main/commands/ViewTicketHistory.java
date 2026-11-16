package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Comment;
import main.ticket.Ticket;
import main.ticket.TicketAction;
import main.users.User;

import java.util.Comparator;
import java.util.List;

public final class ViewTicketHistory extends Command {
    private final ObjectMapper mapper;
    public ViewTicketHistory(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ArrayNode ticketsArray = mapper.createArrayNode();
        User commandUser = Database.getInstance().findUser(command.getUsername());
        List<Ticket> userTickets = commandUser.getTicketsHistory();
        userTickets.sort(new Comparator<Ticket>() {
            @Override
            public int compare(final Ticket ticket1, final Ticket ticket2) {
                int dateComp = ticket1.getCreatedAt().compareTo(ticket2.getCreatedAt());
                if (dateComp != 0) {
                    return dateComp;
                }
                return ticket1.getId() - ticket2.getId();
            }
        });
        for (Ticket currentTicket : userTickets) {
            ObjectNode ticketNode = mapper.createObjectNode();
            ticketNode.put("id", currentTicket.getId());
            ticketNode.put("title", currentTicket.getTitle());
            ticketNode.put("status", currentTicket.getStatus());
            ArrayNode actionsArray = mapper.createArrayNode();
            for (TicketAction currentAction : currentTicket.getActions()) {
                ObjectNode actionNode = mapper.createObjectNode();
                if (currentAction.getAction().equals("ADDED_TO_MILESTONE")) {
                    actionNode.put("milestone", currentAction.getMilestoneName());
                }
                if (currentAction.getAction().equals("STATUS_CHANGED")) {
                    actionNode.put("from", currentAction.getChangedFrom());
                    actionNode.put("to", currentAction.getChangedTo());
                }
                actionNode.put("by", currentAction.getActionBy());
                actionNode.put("timestamp", currentAction.getTimestamp());
                actionNode.put("action", currentAction.getAction());
                actionsArray.add(actionNode);
            }
            ticketNode.set("actions", actionsArray);
            ticketsArray.add(ticketNode);
            ArrayNode commentsArray = mapper.createArrayNode();
            for (Comment currentComment : currentTicket.getComments()) {
                ObjectNode commentNode = mapper.createObjectNode();
                commentNode.put("author", currentComment.getAuthor());
                commentNode.put("content", currentComment.getContent());
                commentNode.put("createdAt", currentComment.getCreatedAt());
                commentsArray.add(commentNode);
            }
            ticketNode.set("comments", commentsArray);
        }
        node.set("ticketHistory", ticketsArray);
        outputs.add(node);
    }
}
