package main.ticket;

import main.fileio.TicketInput;

public final class UiFeedbackCreator extends TicketFactory {
    @Override
    public Ticket createTicket(final TicketInput ticketInput) {
        return new UiFeedback(ticketInput);
    }
}
