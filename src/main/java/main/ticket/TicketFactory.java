package main.ticket;

import main.fileio.TicketInput;

public abstract class TicketFactory {
    public abstract Ticket createTicket(TicketInput ticketInput);
}
