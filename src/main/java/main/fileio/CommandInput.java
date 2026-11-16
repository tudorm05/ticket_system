package main.fileio;

import main.ticket.Filters;

import java.util.List;
public final class CommandInput {
    private String command;
    private String username;
    private String timestamp;
    private TicketInput params;
    private String name;
    private String dueDate;
    private String[] blockingFor;
    private List<Integer> tickets;
    private List<String> assignedDevs;
    private int ticketID;
    private String comment;
    private Filters filters;

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public TicketInput getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    public List<String> getAssignedDevs() {
        return assignedDevs;
    }


    public List<Integer> getTickets() {
        return tickets;
    }

    public String[] getBlockingFor() {
        return blockingFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getComment() {
        return comment;
    }

    public Filters getFilters() {
        return filters;
    }
}
