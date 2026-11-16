package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class CommandCreator {
    private CommandCreator() {
    }

    /**
     * metoda folosit pentru a crea comanda
     * @param command comanda care va executata
     * @param mapper mapperul care scrie outputul
     * @return creeaza comdana
     */
    public static Command createCommand(final String command, final ObjectMapper mapper) {
        switch (command) {
            case "reportTicket":
                return new ReportTicket(mapper);
            case "viewTickets":
                return new ViewTickets(mapper);
            case "createMilestone":
                return new CreateMilestone(mapper);
            case "viewMilestones":
                return new ViewMilestones(mapper);
            case "assignTicket":
                return new AssignTicket(mapper);
            case "viewAssignedTickets":
                return new ViewAssignedTickets(mapper);
            case "undoAssignTicket":
                return new UndoAssignTicket();
            case "addComment":
                return new AddComment(mapper);
            case "undoAddComment":
                return new UndoAddComment(mapper);
            case "changeStatus":
                return new ChangeStatus(mapper);
            case "undoChangeStatus":
                return new UndoChangeStatus(mapper);
            case "viewTicketHistory":
                return new ViewTicketHistory(mapper);
            case "viewNotifications":
                return new ViewNotifications(mapper);
            case "generateCustomerImpactReport":
                return new GenerateCustomerImpactReport(mapper);
            case "generateResolutionEfficiencyReport":
                return new GenerateResolutionEfficiencyReport(mapper);
            case "generateTicketRiskReport":
                return new GenerateTicketRiskReport(mapper);
            case "appStabilityReport":
                return new AppStabilityReport(mapper);
            case "search":
                return new Search();
            case "generatePerformanceReport":
                return new GeneratePerformanceReport();
            case "startTestingPhase":
                return new StartingTestPhase();
            default:
                throw new IllegalStateException("Unexpected value :P" + command);
        }
    }
}
