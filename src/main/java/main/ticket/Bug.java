package main.ticket;

import main.fileio.TicketInput;

public final class Bug extends Ticket {
    private static final int RARE_VALUE = 1;
    private static final int OCCASIONAL_VALUE = 2;
    private static final int FREQUENT_VALUE = 3;
    private static final int ALWAYS_VALUE = 4;
    private static final int MINOR_VALUE = 1;
    private static final int MODERATE_VALUE = 2;
    private static final int MAJOR_VALUE = 3;
    private String expectedBehavior;
    private String actualBehavior;
    private String frequency;
    private String severity;
    private String environment;
    private int errorCode;

    public Bug(final TicketInput ticketInput) {
        super(ticketInput);
        expectedBehavior = ticketInput.getExpectedBehavior();
        actualBehavior = ticketInput.getActualBehavior();
        frequency = ticketInput.getFrequency();
        severity = ticketInput.getSeverity();
        environment = ticketInput.getEnvironment();
        errorCode = ticketInput.getErrorCode();
    }

    @Override
    public double accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    public String getExpectedBehavior() {
        return expectedBehavior;
    }

    public String getActualBehavior() {
        return actualBehavior;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getSeverity() {
        return severity;
    }

    public String getEnvironment() {
        return environment;
    }

    public int getErrorCode() {
        return errorCode;
    }

    /**
     * metoda returneaza o valoare in functie
     * de ce frecventa are bugul
     * @return o valoarea dintr 1, 2, 3 sau 4
     */
    public double frequencyValue() {
        if (frequency.equals("RARE")) {
            return RARE_VALUE;
        } else if (frequency.equals("OCCASIONAL")) {
            return OCCASIONAL_VALUE;
        } else if (frequency.equals("FREQUENT")) {
            return FREQUENT_VALUE;
        } else {
            return ALWAYS_VALUE;
        }
    }

    /**
     * metoda returneaza o valoare in functie
     * de severitatea bugului
     * @return 1, 2, sau 3
     */
    public double severityValue() {
        if (severity.equals("MINOR")) {
            return MINOR_VALUE;
        } else if (severity.equals("MODERATE")) {
            return MODERATE_VALUE;
        } else {
            return MAJOR_VALUE;
        }
    }
}
