package main.database;

import main.fileio.CommandInput;
import main.milestone.Milestone;
import main.ticket.Ticket;
import main.users.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

public final class Database {
    private static final Database INSTANCE = new Database();
    private static final int  MAX_DAYS = 3;
    private List<Ticket> tickets = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Milestone> milestones = new ArrayList<>();
    private String startingDate;
    private Database() {
    }
    public static Database getInstance() {
        return INSTANCE;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
    /**
     * cauta un ticket in database dupa id
     * @param id id-ul ticketului cautat
     * @return ticketul sau null dac nu exista
     */
    public Ticket getTicketById(final int id) {
        Ticket ticketToFind = null;
        for (Ticket currentTicket : tickets) {
            if (currentTicket.getId() == id) {
                ticketToFind = currentTicket;
                break;
            }
        }
        return ticketToFind;
    }

    /**
     * adauga in database un ticket
     * @param ticket ticketul pe care
     *               vrem sa il adaugam
     */
    public void addTicket(final Ticket ticket) {
        this.tickets.add(ticket);
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }

    /**
     * gaseste un user din database dupa nume
     * @param username numele user-ului
     * @return user-ul sau null daca nu exista
     */
    public User findUser(final String username) {
        for (User userToFind : users) {
            if (userToFind.getUsername().equals(username)) {
                return userToFind;
            }
        }
        return null;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(final String startingDate) {
        this.startingDate = startingDate;
    }

    /**
     * gaseste un miletone dupa numele acestuia
     * @param name numele milestone-ului cautat
     * @return milestone-ul sau null daca nu exista
     */
    public Milestone getMilestoneByName(final String name) {
        Milestone milestoneToFind = null;
        for (Milestone currentMilestone : milestones) {
            if (currentMilestone.getName().equals(name)) {
                milestoneToFind = currentMilestone;
                break;
            }
        }
        return milestoneToFind;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * adauga un milestone in database
     * @param milestone milestone-ul pe care
     *                  vrem sa il adaugam
     */
    public void addMilestone(final Milestone milestone) {
        this.milestones.add(milestone);
    }

    /**
     * updateaza businessPriority-ul ticketelor
     * deoarece acesta creste la 3 zile
     * @param command comanda data, avem
     *                nevoie pentru timestamp
     */
    public static void updateBusinessPriority(final CommandInput command) {
        LocalDate now = LocalDate.parse(command.getTimestamp());
        List<Ticket> tickets = Database.getInstance().getTickets();
        for (Ticket currentTicket : tickets) {
            if (currentTicket.getAssignedMilestoneName() == null) {
                continue;
            } else if (currentTicket.getStatus().equals("CLOSED")) {
                continue;
            } else if (Database.getInstance().
                    getMilestoneByName(currentTicket.
                            getAssignedMilestoneName()).getIsBlocked()) {
                continue;
            }
            LocalDate due = LocalDate.parse(Database.getInstance().
                    getMilestoneByName(currentTicket.
                            getAssignedMilestoneName()).getDueDate());
            int daysBetweenDue = (int) ChronoUnit.DAYS.between(now, due) + 1;
            if (daysBetweenDue <= 1) {
                currentTicket.setBusinessPriority("CRITICAL");
                continue;
            }
            LocalDate past = LocalDate.parse(Database.getInstance().
                    getMilestoneByName(currentTicket.
                            getAssignedMilestoneName()).getCreatedAt());
            int daysBetween = (int) ChronoUnit.DAYS.between(past, now) + 1;
            int n = daysBetween / MAX_DAYS;
            for (int i = 0; i < n; i++) {
                if (currentTicket.getBusinessPriority().equals("LOW")) {
                    currentTicket.setBusinessPriority("MEDIUM");
                } else if (currentTicket.getBusinessPriority().equals("MEDIUM")) {
                    currentTicket.setBusinessPriority("HIGH");
                } else if (currentTicket.getBusinessPriority().equals("HIGH")) {
                    currentTicket.setBusinessPriority("CRITICAL");
                }
            }
        }
    }

    /**
     * cauta in database ticketele pentru report
     * @return ticketele cu starea open sau in_progress
     */
    public List<Ticket> getImpactReportTickets() {
        List<Ticket> ticketsList = new ArrayList<>();
        for (Ticket currentTicket : tickets) {
            if (currentTicket.getStatus().equals("OPEN")
                    || currentTicket.getStatus().equals("IN_PROGRESS")) {
                ticketsList.add(currentTicket);
            }
        }
        return ticketsList;
    }

    /**
     * cauta in database ticketele pentru report
     * @return ticketele cu starea resolved sau closed
     */
    public List<Ticket> getEfficiencyReportTickets() {
        List<Ticket> ticketsList = new ArrayList<>();
        for (Ticket currentTicket : tickets) {
            if (currentTicket.getStatus().equals("RESOLVED")
                    || currentTicket.getStatus().equals("CLOSED")) {
                ticketsList.add(currentTicket);
            }
        }
        return ticketsList;
    }

    /**
     *
     * @param timestamp
     */
    public static void updateMilestones(final String timestamp) {
        LocalDate now = LocalDate.parse(timestamp);
        for (Milestone currentMilestone : Database.getInstance().getMilestones()) {
            if (!currentMilestone.getStatus().equals("COMPLETED")) {
                if (currentMilestone.calculateCompletionPercentage(currentMilestone.
                        getClosedTickets()) == 1.0) {
                    currentMilestone.setStatus("COMPLETED");
                    String lastUpdateDate = null;
                    for (int currentTicketId : currentMilestone.getTickets()) {
                        Ticket currentTicket
                                = Database.getInstance().getTicketById(currentTicketId);
                        if (currentTicket != null) {
                            for (main.ticket.TicketAction currentAction
                                    : currentTicket.getActions()) {
                                String destinationStatus = currentAction.getChangedTo();
                                if ("RESOLVED".equals(destinationStatus)
                                        || "CLOSED".equals(destinationStatus)) {
                                    String actionDate = currentAction.getTimestamp();
                                    if (lastUpdateDate == null
                                            || actionDate.compareTo(lastUpdateDate) > 0) {
                                        lastUpdateDate = actionDate;
                                    }
                                }
                            }
                        }
                    }
                    if (lastUpdateDate != null) {
                        currentMilestone.setCompletedDate(lastUpdateDate);
                    } else {
                        currentMilestone.setCompletedDate(timestamp);
                    }
                }
            }
        }
        for (Milestone currentMilestone : Database.getInstance().getMilestones()) {
            if (currentMilestone.getIsBlocked()) {
                boolean isStillBlocked = false;
                for (Milestone potentialBlocker : Database.getInstance().getMilestones()) {
                    if (potentialBlocker.getBlockingFor() != null) {
                        for (String blockedMilestoneName : potentialBlocker.getBlockingFor()) {
                            if (blockedMilestoneName.equals(currentMilestone.getName())) {
                                if (!potentialBlocker.getStatus().equals("COMPLETED")) {
                                    isStillBlocked = true;
                                }
                            }
                        }
                    }
                }
                if (!isStillBlocked) {
                    currentMilestone.setIsBlocked(false);
                    LocalDate dueDate = LocalDate.parse(currentMilestone.getDueDate());
                    String message;
                    if (now.isAfter(dueDate)) {
                        message = "Milestone "
                                + currentMilestone.getName()
                                + " was unblocked after due date. "
                                + "All active tickets are now CRITICAL.";
                    } else {
                        message = "Milestone "
                                + currentMilestone.getName()
                                + " is now unblocked.";
                    }
                    currentMilestone.notifyObservers(message);
                }
            }
        }
        for (Milestone currentMilestone : Database.getInstance().getMilestones()) {
            if (!currentMilestone.getStatus().equals("COMPLETED")) {
                LocalDate dueDate = LocalDate.parse(currentMilestone.getDueDate());
                long daysBetween = ChronoUnit.DAYS.between(now, dueDate);
                if (daysBetween == 1 && !currentMilestone.isWarningSent()) {
                    String message = "Milestone "
                            + currentMilestone.getName()
                            + " is due tomorrow. All unresolved tickets are now CRITICAL.";
                    currentMilestone.notifyObservers(message);
                    currentMilestone.setWarningSent(true);
                }
            }
        }
    }
}
