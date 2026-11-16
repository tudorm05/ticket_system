package main.users;

import main.database.Database;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public final class Reporter extends User {
    public Reporter(final UserInput userInput) {
        super(userInput);
    }
    @Override
    public List<Ticket> getTickets() {
        List<Ticket> reportedTickets = new ArrayList<>();
        List<Ticket> tickets = Database.getInstance().getTickets();
        for (Ticket currentTicket : tickets) {
            if (this.getUsername().equals(currentTicket.getReportedBy())) {
                reportedTickets.add(currentTicket);
            }
        }
        return  reportedTickets;
    }
    @Override
    public List<Milestone> getMilestones() {
        return new ArrayList<>();
    }

    @Override
    public List<Ticket> getTicketsHistory() {
        return new ArrayList<>();
    }
}
