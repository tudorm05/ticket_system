package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ViewMilestones extends Command {
    private final ObjectMapper mapper;

    public ViewMilestones(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        User commandUser = Database.getInstance().findUser(command.getUsername());
        List<Milestone> milestones = commandUser.getMilestones();
        ArrayNode milestonesArray = mapper.createArrayNode();
        milestones.sort(new Comparator<Milestone>() {
            @Override
            public int compare(final Milestone milestone1, final Milestone milestone2) {
                int dateComparison = milestone1.getDueDate().compareTo(milestone2.getDueDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                return milestone1.getName().compareTo(milestone2.getName());
            }
        });
        for (Milestone currentMilestone : milestones) {
            List<Repartition> repartitions = obtainRepartitions(currentMilestone);
            repartitions.sort(new Comparator<Repartition>() {
                @Override
                public int compare(final Repartition repartition1, final Repartition repartition2) {
                    int sizeCompare = repartition1.assignedTickets.size()
                            - repartition2.assignedTickets.size();
                    if (sizeCompare != 0) {
                        return sizeCompare;
                    }
                    return repartition1.developer.compareTo(repartition2.developer);
                }
            });
            ObjectNode milestoneNode = mapper.createObjectNode();
            milestoneNode.put("name", currentMilestone.getName());
            milestoneNode.set("blockingFor", mapper.valueToTree(currentMilestone.getBlockingFor()));
            milestoneNode.put("dueDate", currentMilestone.getDueDate());
            milestoneNode.put("createdAt", currentMilestone.getCreatedAt());
            milestoneNode.set("tickets",
                    mapper.valueToTree(currentMilestone.getTickets()));
            milestoneNode.set("assignedDevs",
                    mapper.valueToTree(currentMilestone.getAssignedDevs()));
            milestoneNode.put("createdBy", currentMilestone.getCreatedBy());
            milestoneNode.put("status", currentMilestone.getStatus());
            milestoneNode.put("isBlocked", currentMilestone.getIsBlocked());
            milestoneNode.put("daysUntilDue",
                    currentMilestone.calculateDaysUntilDue(command.getTimestamp()));
            milestoneNode.put("overdueBy",
                    currentMilestone.calculateOverdueBy(command.getTimestamp()));
            milestoneNode.set("openTickets", mapper.valueToTree(currentMilestone.getOpenTickets()));
            milestoneNode.set("closedTickets",
                    mapper.valueToTree(currentMilestone.getClosedTickets()));
            milestoneNode.put("completionPercentage",
                    currentMilestone.calculateCompletionPercentage(currentMilestone.
                            getClosedTickets()));
            milestoneNode.set("repartition", mapper.valueToTree(repartitions));
            milestonesArray.add(milestoneNode);
        }
        node.set("milestones", milestonesArray);
        outputs.add(node);
    }

    /**
     * metoda pentru a forma repartitiile
     * sunt necesare pentru output
     *
     * @param milestone milestone-ul curent
     * @return repartitiile
     */
    public List<Repartition> obtainRepartitions(final Milestone milestone) {
        List<Repartition> repartitions = new ArrayList<>();
        for (String currentDeveloper : milestone.getAssignedDevs()) {
            List<Integer> ticketIds = new ArrayList<>();
            for (int currentTicketId : milestone.getTickets()) {
                Ticket currentTicket = Database.getInstance().getTicketById(currentTicketId);
                if (currentTicket == null) {
                    break;
                }
                if (currentDeveloper.equals(currentTicket.getAssignedTo())) {
                    ticketIds.add(currentTicketId);
                }
            }
            repartitions.add(new Repartition(currentDeveloper, ticketIds));
        }
        return repartitions;
    }

    private static class Repartition {
        private final String developer;
        private final List<Integer> assignedTickets;

        Repartition(final String developer, final List<Integer> assignedTickets) {
            this.developer = developer;
            this.assignedTickets = assignedTickets;
        }

        public String getDeveloper() {
            return developer;
        }

        public List<Integer> getAssignedTickets() {
            return assignedTickets;
        }
    }
}
