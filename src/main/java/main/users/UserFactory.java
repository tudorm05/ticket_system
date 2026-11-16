package main.users;

import main.fileio.UserInput;

public abstract class UserFactory {
    /**
     * metoda pentru design patternul factory
     * @return
     */
    public abstract User createUser(UserInput userInput);
    /**
     * creeaza un nou utilizator in functie
     * de ce rol are acesta
     * @return
     */
    public static UserFactory obtainCreator(final String role) {
        switch (role) {
            case "REPORTER":
                return new ReporterCreator();
            case "DEVELOPER":
                return new DeveloperCreator();
            case "MANAGER":
                return new ManagerCreator();
            default:
                throw new IllegalArgumentException("Wrong type :P");
        }
    }
}
