package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.ticket.TicketAction;
import main.users.Developer;
import main.users.User;

import java.util.List;

public final class CreateMilestone extends Command {
    private final ObjectMapper mapper;
    public CreateMilestone(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Milestone milestone = new Milestone(command);
        User commandUser = Database.getInstance().findUser(command.getUsername());
        if (!commandUser.getRole().equals("MANAGER")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "The user does not have permission to execute"
                    + " this command: required role MANAGER; user role "
                    + commandUser.getRole() + ".");
            outputs.add(node);
            return;
        }
        for (Integer currentTicketId : command.getTickets()) {
            Ticket currentTicket = Database.getInstance().getTicketById(currentTicketId);
            if (currentTicket != null && currentTicket.getAssignedMilestoneName() != null) {
                ObjectNode node = createNode(mapper, command);
                node.put("error", "Tickets " + currentTicketId
                        + " already assigned to milestone "
                        + currentTicket.getAssignedMilestoneName() + ".");
                outputs.add(node);
                return;
            }
        }
        Database.getInstance().addMilestone(milestone);
        for (String currentUserName : command.getAssignedDevs()) {
            User currentUser = Database.getInstance().findUser(currentUserName);
            milestone.addObserver((Developer) currentUser);
        }
        for (Integer currentTicketId : command.getTickets()) {
            Ticket currentTicket = Database.getInstance().getTicketById(currentTicketId);
            if (currentTicket != null) {
                TicketAction ticketAction = new TicketAction.Builder("ADDED_TO_MILESTONE",
                        command.getUsername(),
                        command.getTimestamp()).milestoneName(command.getName()).build();
                currentTicket.setAssignedMilestoneName(milestone.getName());
                currentTicket.getActions().add(ticketAction);
            }
        }
        for (String currentMilestoneName : milestone.getBlockingFor()) {
            Milestone blockedMilestone = Database.getInstance().
                    getMilestoneByName(currentMilestoneName);
            blockedMilestone.setIsBlocked(true);
        }
        String msg = "New milestone " + milestone.getName()
                + " has been created with due date " + milestone.getDueDate() + ".";
        milestone.notifyObservers(msg);
    }
}
