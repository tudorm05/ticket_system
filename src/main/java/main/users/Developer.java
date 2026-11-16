package main.users;

import main.database.Database;
import main.fileio.UserInput;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.ticket.TicketAction;

import java.util.ArrayList;
import java.util.List;

public final class Developer extends User implements Observer {
    private String hireDate;
    private String expertiseArea;
    private String seniority;
    private List<String> notifications = new ArrayList<>();
    public Developer(final UserInput userInput) {
        super(userInput);
        this.hireDate = userInput.getHireDate();
        this.expertiseArea = userInput.getExpertiseArea();
        this.seniority = userInput.getSeniority();
    }

    @Override
    public List<Ticket> getTickets() {
        List<Ticket> myVisibleTickets = new ArrayList<>();
        List<Milestone> milestones = Database.getInstance().getMilestones();
        for (Milestone currentMilestone : milestones) {
            List<String> developers = currentMilestone.getAssignedDevs();
            for (String currentDeveloper : developers) {
                if (this.getUsername().equals(currentDeveloper)) {
                    List<Integer> milestoneIds = currentMilestone.getTickets();
                    for (int currentId : milestoneIds) {
                        Ticket currentTicket = Database.getInstance().getTicketById(currentId);
                        if (currentTicket != null && currentTicket.getStatus().equals("OPEN")) {
                            myVisibleTickets.add(currentTicket);
                        }
                    }
                }
            }
        }
        return myVisibleTickets;
    }

    @Override
    public List<Milestone> getMilestones() {
        List<Milestone> milestones = Database.getInstance().getMilestones();
        List<Milestone> developerMilestone = new ArrayList<>();
        for (Milestone currentMilestone : milestones) {
            List<String> assignedDevelopers = currentMilestone.getAssignedDevs();
            for (String currentDeveloper : assignedDevelopers) {
                if (this.getUsername().equals(currentDeveloper)) {
                    developerMilestone.add(currentMilestone);
                }
            }
        }
        return developerMilestone;
    }

    @Override
    public List<Ticket> getTicketsHistory() {
        List<Ticket> tickets = Database.getInstance().getTickets();
        List<Ticket> ticketList = new ArrayList<>();
        for (Ticket currentTicket : tickets) {
            for (TicketAction currentAction : currentTicket.getActions()) {
                if (currentAction.getAction().equals("ASSIGNED") && this.getUsername().equals(currentAction.getActionBy())) {
                    ticketList.add(currentTicket);
                }
            }
        }
        return ticketList;
    }

    @Override
    public List<Ticket> getAssignedTickets() {
        List<Ticket> tickets = Database.getInstance().getTickets();
        List<Ticket> developerAssignedTickets = new ArrayList<>();
        for (Ticket currentTicket : tickets) {
            if (this.getUsername().equals(currentTicket.getAssignedTo())) {
                developerAssignedTickets.add(currentTicket);
            }
        }
        return developerAssignedTickets;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public String getSeniority() {
        return seniority;
    }
    /**
     *  verifica daca un developer
     *  are expertiseArea necesar
     *  pentru a prelua un ticket
     * @return true sau false
     */
    public boolean checkExpertise(final String ticketExpertiseArea) {
        if (expertiseArea.equals("FULLSTACK")) {
            return true;
        } else if (expertiseArea.equals(ticketExpertiseArea)) {
            return true;
        } else if (ticketExpertiseArea.equals("FRONTEND") && expertiseArea.equals("DESIGN")) {
            return true;
        } else if (ticketExpertiseArea.equals("DESIGN") && expertiseArea.equals("FRONTED")) {
            return true;
        } else if (ticketExpertiseArea.equals("DB") && expertiseArea.equals("BACKEND")) {
            return true;
        }
        return false;
    }
    /**
     *  verifica daca un developer are
     *  seniority-ul bun pentru a prelua un ticket
     * @return true sau false depinde
     */
    public boolean checkSeniority(final Ticket ticket) {
        if (seniority.equals("JUNIOR") && (ticket.getType().equals("FEATURE_REQUEST")
                || ticket.getBusinessPriority().equals("HIGH"))) {
            return false;
        } else if (ticket.getBusinessPriority().equals("CRITICAL") && !seniority.equals("SENIOR")) {
            return false;
        }
        return true;
    }

    @Override
    public void update(final String notification) {
        this.notifications.add(notification);
    }

    public List<String> getNotifications() {
        return new ArrayList<>(this.notifications);
    }

    public void clearNotifications() {
        this.notifications.clear();
    }
}
