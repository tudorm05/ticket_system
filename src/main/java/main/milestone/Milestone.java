package main.milestone;

import main.database.Database;
import main.fileio.CommandInput;
import main.ticket.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

public final class Milestone extends Subject {
    private static final double MAX_PERCENTAGE = 100;
    private String name;
    private String[] blockingFor;
    private String dueDate;
    private List<Integer> tickets;
    private List<String> assignedDevs;
    private String createdBy;
    private String createdAt;
    private String status;
    private String completedDate = null;
    private boolean isBlocked;
    private boolean warningSent = false;
    public Milestone(final CommandInput commandInput) {
        this.name = commandInput.getName();
        this.blockingFor = commandInput.getBlockingFor();
        this.dueDate = commandInput.getDueDate();
        this.tickets = commandInput.getTickets();
        this.assignedDevs = commandInput.getAssignedDevs();
        this.createdBy = commandInput.getUsername();
        this.createdAt = commandInput.getTimestamp();
        this.status = "ACTIVE";
        this.isBlocked = false;
    }

    /**
     * metoda pentru a cauta ticketele
     * care nu sunt in starea closed
     * @return id-urile ticketele care nu sunt closed
     */
    public List<Integer> getOpenTickets() {
        List<Integer> openTicketsIds = new ArrayList<>();
        for (int currentTicketId : tickets) {
            Ticket currentTicket = Database.getInstance().getTicketById(currentTicketId);
            if (currentTicket != null && !currentTicket.getStatus().equals("CLOSED")) {
                openTicketsIds.add(currentTicketId);
            }
        }
        return openTicketsIds;
    }

    /**
     * metoda pentru a cauta toate ticketele
     * in starea closed
     * @return id-urile ticketelor closed
     */
    public List<Integer> getClosedTickets() {
        List<Integer> closedTicketsIds = new ArrayList<>();
        for (int currentTicketId : tickets) {
            Ticket currentTicket = Database.getInstance().getTicketById(currentTicketId);
            if (currentTicket != null && currentTicket.getStatus().equals("CLOSED")) {
                closedTicketsIds.add(currentTicketId);
            }
        }
        return closedTicketsIds;
    }

    /**
     * calculeaza completionPercentage pentru un milestone
     * @param closedTicketsIds id-urile ticketelor closed
     * @return raportul dintre numarul ticketelor closed
     * si numarul total al ticketelor
     */
    public double calculateCompletionPercentage(final List<Integer> closedTicketsIds) {
        double completionPercentage = 0.0;
        completionPercentage = (double) closedTicketsIds.size() / tickets.size();
        return Math.round(completionPercentage * MAX_PERCENTAGE) / MAX_PERCENTAGE;
    }

    /**
     * calculeaza numarul de zile pana la dueDate
     * mai intai verifica daca exista completedDay
     * adica timestampul in care a trecut in starea resolved
     * @param currentDay timestampul comenzii practic
     * @return numarul de zile pana la dueDate
     * sau 0 daca a trecut de dueDate
     */
    public int calculateDaysUntilDue(final String currentDay) {
        LocalDate now;
        if (completedDate == null) {
             now = LocalDate.parse(currentDay);
        } else {
            now = LocalDate.parse(completedDate);
        }
        LocalDate due = LocalDate.parse(dueDate);
        int daysBetween = (int) ChronoUnit.DAYS.between(now, due) + 1;
        if (daysBetween < 0) {
            return 0;
        }
        return daysBetween;
    }

    /**
     * similara cu comanda anterioara
     * @param currentDay timestampul comenzii
     * @return la fel pentru overdueBy
     */
    public int calculateOverdueBy(final String currentDay) {
        LocalDate now;
        if (completedDate == null) {
            now = LocalDate.parse(currentDay);
        } else {
            now = LocalDate.parse(completedDate);
        }
        LocalDate due = LocalDate.parse(this.dueDate);
        int daysBetween = (int) ChronoUnit.DAYS.between(due, now) + 1;
        if (daysBetween < 0) {
            return 0;
        }
        return daysBetween;
    }

    public List<String> getAssignedDevs() {
        return assignedDevs;
    }

    public List<Integer> getTickets() {
        return tickets;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public String[] getBlockingFor() {
        return blockingFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setTickets(final List<Integer> tickets) {
        this.tickets = tickets;
    }

    public void setAssignedDevs(final List<String> assignedDevs) {
        this.assignedDevs = assignedDevs;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(final boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(final boolean blocked) {
        isBlocked = blocked;
    }

    public void setCompletedDate(final String completedDate) {
        this.completedDate = completedDate;
    }

    public boolean isWarningSent() {
        return warningSent;
    }

    public void setWarningSent(final boolean warningSent) {
        this.warningSent = warningSent;
    }
}
