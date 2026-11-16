package main.ticket;

import main.fileio.TicketInput;

public final class BugCreator extends TicketFactory {
    @Override
    public Ticket createTicket(final TicketInput ticketInput) {
        return new Bug(ticketInput);
    }
}
