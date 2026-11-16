package main.users;

import main.database.Database;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public final class Manager extends User {
    private String hiringDate;
    private String[] subordinates;
    public Manager(final UserInput userInput) {
        super(userInput);
        this.hiringDate = userInput.getHireDate();
        this.subordinates = userInput.getSubordinates();
    }
    @Override
    public List<Ticket> getTickets() {
        return Database.getInstance().getTickets();
    }
    @Override
    public List<Milestone> getMilestones() {
        List<Milestone> milestones = Database.getInstance().getMilestones();
        List<Milestone> managerMilestones = new ArrayList<>();
        for (Milestone currentMilestone : milestones) {
            if (currentMilestone.getCreatedBy().equals(this.getUsername())) {
                managerMilestones.add(currentMilestone);
            }
        }
        return managerMilestones;
    }

    @Override
    public List<Ticket> getTicketsHistory() {
        List<Ticket> tickets = new ArrayList<>();
        List<Milestone> milestones = getMilestones();
        for (Milestone currentMilestone : milestones) {
            for (Integer currentId : currentMilestone.getTickets()) {
                tickets.add(Database.getInstance().getTicketById(currentId));
            }
        }
        return tickets;
    }
}
