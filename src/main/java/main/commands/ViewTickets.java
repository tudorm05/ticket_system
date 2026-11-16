package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import main.fileio.CommandInput;
import main.database.Database;
import main.ticket.Comment;
import main.ticket.Ticket;
import main.users.User;

import java.util.List;

public final class ViewTickets extends Command {
    private final ObjectMapper mapper;
    public ViewTickets(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        ArrayNode ticketsArray = mapper.createArrayNode();
        User commandUser = Database.getInstance().findUser(command.getUsername());
        List<Ticket> userTickets = commandUser.getTickets();
        for (Ticket currentTicket : userTickets) {
            ObjectNode ticketNode =  mapper.createObjectNode();
            ArrayNode commentsArray = mapper.createArrayNode();
            ticketNode.put("id", currentTicket.getId());
            ticketNode.put("type", currentTicket.getType());
            ticketNode.put("title", currentTicket.getTitle());
            ticketNode.put("businessPriority", currentTicket.getBusinessPriority());
            ticketNode.put("status", currentTicket.getStatus());
            ticketNode.put("createdAt", currentTicket.getCreatedAt());
            ticketNode.put("assignedAt", currentTicket.getAssignedAt());
            ticketNode.put("solvedAt", currentTicket.getSolvedAt());
            ticketNode.put("assignedTo", currentTicket.getAssignedTo());
            ticketNode.put("reportedBy", currentTicket.getReportedBy());
            for (Comment currentComment: currentTicket.getComments()) {
                ObjectNode commentNode = mapper.createObjectNode();
                commentNode.put("author", currentComment.getAuthor());
                commentNode.put("content", currentComment.getContent());
                commentNode.put("createdAt", currentComment.getCreatedAt());
                commentsArray.add(commentNode);
            }
            ticketNode.set("comments", commentsArray);
            ticketsArray.add(ticketNode);
        }
        node.set("tickets", ticketsArray);
        outputs.add(node);
    }
}
