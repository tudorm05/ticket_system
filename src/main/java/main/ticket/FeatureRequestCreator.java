package main.ticket;

import main.fileio.TicketInput;

public final class FeatureRequestCreator extends TicketFactory {
    @Override
    public Ticket createTicket(final TicketInput ticketInput) {
        return new FeatureRequest(ticketInput);
    }
}
