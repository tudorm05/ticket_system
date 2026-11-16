package main.ticket;

import main.fileio.TicketInput;

public final class UiFeedback extends Ticket {
    private static final int S_VALUE = 1;
    private static final int M_VALUE = 3;
    private static final int L_VALUE = 6;
    private static final int XL_VALUE = 10;
    private String uiElementId;
    private String businessValue;
    private int usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;
    public UiFeedback(final TicketInput ticketInput) {
        super(ticketInput);
        uiElementId = ticketInput.getUiElementId();
        businessValue = ticketInput.getBusinessValue();
        usabilityScore = ticketInput.getUsabilityScore();
        screenshotUrl = ticketInput.getScreenshotUrl();
        suggestedFix = ticketInput.getSuggestedFix();
    }

    @Override
    public double accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    public String getUiElementId() {
        return uiElementId;
    }

    public String getBusinessValue() {
        return businessValue;
    }

    public int getUsabilityScore() {
        return usabilityScore;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

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
}
