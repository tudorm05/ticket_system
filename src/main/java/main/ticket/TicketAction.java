package main.ticket;

public final class TicketAction {
    private String action;
    private String actionBy;
    private String timestamp;
    private String changedFrom;
    private String changedTo;
    private String removedFrom;
    private String milestoneName;
    public static class Builder {
        private String action;
        private String actionBy;
        private String timestamp;
        private String changedFrom = null;
        private String changedTo = null;
        private String milestoneName = null;
        public Builder(final String action, final String actionBy, final String timestamp) {
            this.action = action;
            this.actionBy = actionBy;
            this.timestamp = timestamp;
        }
        public Builder changedFrom(final String changedFrom) {
            this.changedFrom = changedFrom;
            return this;
        }
        public Builder changedTo(final String changedTo) {
            this.changedTo = changedTo;
            return this;
        }
        public Builder milestoneName(final String milestoneName) {
            this.milestoneName = milestoneName;
            return this;
        }
        public TicketAction build() {
            return new TicketAction(this);
        }
    }
    private TicketAction(final Builder builder) {
        this.action = builder.action;
        this.actionBy = builder.actionBy;
        this.timestamp = builder.timestamp;
        this.changedFrom = builder.changedFrom;
        this.changedTo = builder.changedTo;
        this.milestoneName = builder.milestoneName;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getChangedFrom() {
        return changedFrom;
    }

    public void setChangedFrom(String changedFrom) {
        this.changedFrom = changedFrom;
    }

    public String getChangedTo() {
        return changedTo;
    }

    public void setChangedTo(String changedTo) {
        this.changedTo = changedTo;
    }

    public String getRemovedFrom() {
        return removedFrom;
    }

    public void setRemovedFrom(String removedFrom) {
        this.removedFrom = removedFrom;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }
}
