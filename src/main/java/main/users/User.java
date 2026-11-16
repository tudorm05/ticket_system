package main.users;

import main.milestone.Milestone;
import main.ticket.Ticket;
import main.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String username;
    private String email;
    private String role;
    private List<String> notifications = new ArrayList<>();
    public User(final UserInput userInput) {
        this.username = userInput.getUsername();
        this.email = userInput.getEmail();
        this.role = userInput.getRole();
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
    /**
     * metoda ce este suprascrisa
     * deoarece fiecare rol returneaza
     * altceva
     * @return
     */
    public abstract List<Ticket> getTickets();
    /**
     * metoda suprascrisa
     * din acelasi motiv
     * @return
     */
    public abstract List<Milestone> getMilestones();
    /**
     * metoda suprascrisa
     * din acelasi motiv
     * @return
     */
    public abstract List<Ticket> getTicketsHistory();
    /**
     *  este suprascrisa de developer
     *  pentru a returna ticketele ce
     *  ii sunt asignate
     * @return
     */
    public List<Ticket> getAssignedTickets() {
        return new ArrayList<>();
    }
}
