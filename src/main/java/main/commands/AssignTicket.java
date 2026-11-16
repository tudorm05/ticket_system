package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.fileio.CommandInput;
import main.database.Database;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.ticket.TicketAction;
import main.users.Developer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AssignTicket extends Command {
    private final ObjectMapper mapper;
    public AssignTicket(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        Ticket ticketToAssign = Database.getInstance().getTicketById(command.getTicketID());
        if (ticketToAssign == null) {
            return;
        }
        Developer developer = (Developer) Database.getInstance().findUser(command.getUsername());
        if (!developer.checkExpertise(ticketToAssign.getExpertiseArea())) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Developer " + command.getUsername()
                        + " cannot assign ticket " + command.getTicketID()
                        + " due to expertise area. Required: "
                        + requiredExpertise(ticketToAssign.getExpertiseArea())
                        + "; Current: " + developer.getExpertiseArea() + ".");
            outputs.add(node);
            return;
        }
        if (!developer.checkSeniority(ticketToAssign)) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Developer " + command.getUsername()
                        + " cannot assign ticket " + command.getTicketID()
                        + " due to seniority level. Required: "
                        + requiredSeniority(ticketToAssign)
                        + "; Current: " + developer.getSeniority() + ".");
            outputs.add(node);
            return;
        }
        if (!ticketToAssign.getStatus().equals("OPEN")) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Only OPEN tickets can be assigned.");
            outputs.add(node);
            return;
        }
        String milestoneName = ticketToAssign.getAssignedMilestoneName();
        Milestone ticketMilestone = Database.getInstance().getMilestoneByName(milestoneName);
        if (!ticketMilestone.getAssignedDevs().contains(command.getUsername())) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Developer "
                        + command.getUsername()
                        + " is not assigned to milestone "
                        + ticketToAssign.getAssignedMilestoneName() + ".");
            outputs.add(node);
            return;
        }
        if (ticketMilestone.getIsBlocked()) {
            ObjectNode node = createNode(mapper, command);
            node.put("error", "Cannot assign ticket "
                        + ticketToAssign.getId() + " from blocked milestone "
                        + ticketToAssign.getAssignedMilestoneName() + ".");
            outputs.add(node);
            return;
        }
        ticketToAssign.setAssignedTo(command.getUsername());
        ticketToAssign.setAssignedAt(command.getTimestamp());
        ticketToAssign.setStatus("IN_PROGRESS");
        TicketAction action = new TicketAction.Builder("ASSIGNED",
                command.getUsername(), command.getTimestamp()).build();
        ticketToAssign.getActions().add(action);
        TicketAction action1 = new TicketAction.Builder("STATUS_CHANGED",
                command.getUsername(),
                command.getTimestamp()).changedFrom("OPEN").changedTo("IN_PROGRESS").build();
        ticketToAssign.getActions().add(action1);
    }

    /**
     * daca un developer nu are expertiza necesara
     * se returneaza ce expertiza ar putea avea
     * @param ticketExpertise expertiza necesara
     *                        pentru a atribui ticketul
     * @return lista cu expertizele posibile
     */
    public String requiredExpertise(final String ticketExpertise) {
        List<String> expertises = new ArrayList<>();
        if (ticketExpertise.equals("FRONTEND")) {
            expertises.add("FRONTEND");
            expertises.add("DESIGN");
        } else if (ticketExpertise.equals("BACKEND")) {
            expertises.add("BACKEND");
        } else if (ticketExpertise.equals("DEVOPS")) {
            expertises.add("DEVOPS");
        } else if (ticketExpertise.equals("DESIGN")) {
            expertises.add("DESIGN");
            expertises.add("FRONTEND");
        } else if (ticketExpertise.equals("DB")) {
            expertises.add("BACKEND");
            expertises.add("DB");
        }
        expertises.add("FULLSTACK");
        return expertises.stream().sorted().collect(Collectors.joining(", "));
    }

    /**
     * daca un developer nu are seniority-ul
     * necesar se returneaza ce seniority ar
     * putea avea
     * @param ticket ticketul pe care incearca
     *               sa il asigneze
     * @return seniority-ul pe care
     * l-ar putea avea sa ia acest ticket
     */
    public String requiredSeniority(final Ticket ticket) {
        List<String> seniorityList = new ArrayList<>();
        if (ticket.getBusinessPriority().equals("HIGH")
                || ticket.getType().equals("FEATURE_REQUEST")) {
            seniorityList.add("MID");
        }
        seniorityList.add("SENIOR");
        return seniorityList.stream().sorted().collect(Collectors.joining(", "));
    }
}
