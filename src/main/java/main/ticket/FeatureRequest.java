package main.ticket;

import main.fileio.TicketInput;

public final class FeatureRequest extends Ticket {
    private static final double S_VALUE = 1;
    private static final double M_VALUE = 3;
    private static final double L_VALUE = 6;
    private static final double XL_VALUE = 10;
    private static final double LOW_VALUE = 1;
    private static final double MEDIUM_VALUE = 3;
    private static final double HIGH_VALUE = 6;
    private static final double VERY_HIGH_VALUE = 9;
    private String businessValue;
    private String customerDemand;
    public FeatureRequest(final TicketInput ticketInput) {
        super(ticketInput);
        businessValue = ticketInput.getBusinessValue();
        customerDemand = ticketInput.getCustomerDemand();
    }

    @Override
    public double accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    public String getBusinessValue() {
        return businessValue;
    }

    public String getCustomerDemand() {
        return customerDemand;
    }

    /**
     * metoda returneaza o valoare
     * in functie de ce businessValue
     * are feature requestul
     * @return 1, 3, 6 sau 10
     */
    public double businessValueScore() {
        if (businessValue.equals("S")) {
            return S_VALUE;
        } else if (businessValue.equals("M")) {
            return M_VALUE;
        } else if (businessValue.equals("L")) {
            return L_VALUE;
        } else {
            return XL_VALUE;
        }
    }

    /**
     * metoda returneaza o valoare
     * in functie de ce customerDemand
     * are feature requestul
     * @return 1, 3, 6, 9
     */
    public double customerDemandValue() {
        if (customerDemand.equals("LOW")) {
            return LOW_VALUE;
        } else if (customerDemand.equals("MEDIUM")) {
            return MEDIUM_VALUE;
        } else if (customerDemand.equals("HIGH")) {
            return HIGH_VALUE;
        } else {
            return VERY_HIGH_VALUE;
        }
    }
}
